package electrodynamics.common.tile;

import com.mojang.math.Vector3f;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerMineralWasher;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.types.GenericFluidTile;
import electrodynamics.registers.ElectrodynamicsBlockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileMineralWasher extends GenericFluidTile {
	public static final int MAX_TANK_CAPACITY = 5000;

	public TileMineralWasher(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsBlockTypes.TILE_MINERALWASHER.get(), worldPosition, blockState);
		addComponent(new ComponentTickable().tickClient(this::tickClient));
		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentElectrodynamic(this).relativeInput(Direction.NORTH).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 4));
		addComponent(new ComponentFluidHandlerMulti(this).setTanks(1, 1, new int[] { MAX_TANK_CAPACITY }, new int[] { MAX_TANK_CAPACITY }).setInputDirections(Direction.EAST).setOutputDirections(Direction.WEST).setRecipeType(ElectrodynamicsRecipeInit.MINERAL_WASHER_TYPE.get()));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().processors(1, 1, 0, 0).bucketInputs(1).bucketOutputs(1).upgrades(3)).relativeSlotFaces(0, Direction.values()).validUpgrades(ContainerMineralWasher.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentProcessor(this).canProcess(component -> component.outputToPipe().consumeBucket().dispenseBucket().canProcessFluidItem2FluidRecipe(component, ElectrodynamicsRecipeInit.MINERAL_WASHER_TYPE.get())).process(component -> component.processFluidItem2FluidRecipe(component)));
		addComponent(new ComponentContainerProvider(SubtypeMachine.mineralwasher).createMenu((id, player) -> new ContainerMineralWasher(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
	}

	@Override
	public AABB getRenderBoundingBox() {
		return super.getRenderBoundingBox().inflate(1);
	}

	protected void tickClient(ComponentTickable tickable) {
		if (this.<ComponentProcessor>getComponent(ComponentType.Processor).isActive()) {
			if (level.random.nextDouble() < 0.15) {
				level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + level.random.nextDouble(), worldPosition.getY() + level.random.nextDouble() * 0.4 + 0.5, worldPosition.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
			ComponentInventory inv = getComponent(ComponentType.Inventory);
			ItemStack input = inv.getInputContents().get(0);
			Vector3f color;
			if (input.getItem() instanceof BlockItem block && block.getBlock().defaultBlockState().is(Blocks.MAGMA_BLOCK)) {
				color = new Vector3f(1f, 0.64706f, 0);
			} else {
				color = new Vector3f(1f, 1f, 0);
			}
			for (int i = 0; i < 2; i++) {
				double x = 0.5 + level.random.nextDouble() * 0.4 - 0.2;
				double y = 0.5 + level.random.nextDouble() * 0.3 - 0.15;
				double z = 0.5 + level.random.nextDouble() * 0.4 - 0.2;
				level.addParticle(new DustParticleOptions(color, 1), worldPosition.getX() + x, worldPosition.getY() + y, worldPosition.getZ() + z, level.random.nextDouble() * 0.2 - 0.1, level.random.nextDouble() * 0.2 - 0.1, level.random.nextDouble() * 0.2 - 0.1);
			}
		}
	}
	
	@Override
	public int getComparatorSignal() {
		return this.<ComponentProcessor>getComponent(ComponentType.Processor).isActive() ? 15 : 0;
	}

}
