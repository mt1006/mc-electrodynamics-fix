package electrodynamics.common.tile;

import java.util.HashSet;

import electrodynamics.DeferredRegisters;
import electrodynamics.api.capability.electrodynamic.CapabilityElectrodynamic;
import electrodynamics.api.electricity.generator.IElectricGenerator;
import electrodynamics.common.block.BlockMachine;
import electrodynamics.common.inventory.container.ContainerSolarPanel;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.multiblock.IMultiblockTileNode;
import electrodynamics.common.multiblock.Subnode;
import electrodynamics.common.network.ElectricityUtilities;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.object.CachedTileOutput;
import electrodynamics.prefab.utilities.object.TargetValue;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileAdvancedSolarPanel extends GenericTile implements IMultiblockTileNode, IElectricGenerator {
    public TargetValue currentRotation = new TargetValue(0);
    protected CachedTileOutput output;
    private boolean generating = false;
    private double multiplier;

    @Override
    public double getMultiplier() {
	return multiplier;
    }

    @Override
    public void setMultiplier(double val) {
	this.multiplier = val;
    }

    public TileAdvancedSolarPanel(BlockPos worldPosition, BlockState blockState) {
	super(DeferredRegisters.TILE_ADVANCEDSOLARPANEL.get(), worldPosition, blockState);
	addComponent(new ComponentTickable().tickServer(this::tickServer));
	addComponent(new ComponentElectrodynamic(this).output(Direction.DOWN).voltage(CapabilityElectrodynamic.DEFAULT_VOLTAGE * 2));
	addComponent(new ComponentInventory(this).size(1).slotFaces(0, Direction.values())
		.valid((slot, stack, i) -> stack.getItem() instanceof ItemUpgrade));
	addComponent(new ComponentContainerProvider("container.advancedsolarpanel")
		.createMenu((id, player) -> new ContainerSolarPanel(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
    }

    protected void tickServer(ComponentTickable tickable) {
	if (output == null) {
	    output = new CachedTileOutput(level, worldPosition.relative(Direction.DOWN));
	}
	if (tickable.getTicks() % 40 == 0) {
	    output.update();
	    generating = level.canSeeSky(worldPosition.offset(0, 1, 0));
	}
	for (ItemStack stack : this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems()) {
	    if (!stack.isEmpty() && stack.getItem()instanceof ItemUpgrade upgrade) {
		for (int i = 0; i < stack.getCount(); i++) {
		    upgrade.subtype.applyUpgrade.accept(this, null, null);
		}
	    }
	}
	if (level.isDay() && generating && output.valid()) {
	    float mod = 1.0f - Mth.clamp(1.0F - (Mth.cos(level.getTimeOfDay(1f) * ((float) Math.PI * 2f)) * 2.0f + 0.2f), 0.0f, 1.0f);
	    mod *= 1.0f - level.getRainLevel(1f) * 5.0f / 16.0f;
	    mod *= (1.0f - level.getThunderLevel(1f) * 5.0F / 16.0f) * 0.8f + 0.2f;
	    Biome b = level.getBiomeManager().getBiome(getBlockPos());
	    TransferPack pack = TransferPack.ampsVoltage(
		    multiplier * Constants.ADVANCEDSOLARPANEL_AMPERAGE * (b.getBaseTemperature() / 2.0) * mod
			    * (level.isRaining() || level.isThundering() ? 0.7f : 1),
		    this.<ComponentElectrodynamic>getComponent(ComponentType.Electrodynamic).getVoltage());
	    ElectricityUtilities.receivePower(output.getSafe(), Direction.UP, pack, false);
	}
    }

    @Override
    @OnlyIn(value = Dist.CLIENT)
    public AABB getRenderBoundingBox() {
	return super.getRenderBoundingBox().inflate(2);
    }

    @Override
    public HashSet<Subnode> getSubNodes() {
	return BlockMachine.advancedsolarpanelsubnodes;
    }
}
