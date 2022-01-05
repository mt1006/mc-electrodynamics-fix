package electrodynamics.common.tile;

import electrodynamics.DeferredRegisters;
import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.common.inventory.container.tile.ContainerCarbyneBatteryBox;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.network.ElectricityUtilities;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.object.CachedTileOutput;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileCarbyneBatteryBox extends TileBatteryBox {

	public TileCarbyneBatteryBox(BlockPos worldPosition, BlockState blockState) {
		super(DeferredRegisters.TILE_CARBYNEBATTERYBOX.get(), 359.0 * (4 * ElectrodynamicsCapabilities.DEFAULT_VOLTAGE) / 20.0, 80000000,
				worldPosition, blockState);
		forceComponent(new ComponentContainerProvider("container.carbynebatterybox")
				.createMenu((id, player) -> new ContainerCarbyneBatteryBox(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
	}

	@Override
	protected void tickServer(ComponentTickable tickable) {
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		Direction facing = this.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
		if (output == null) {
			output = new CachedTileOutput(level, worldPosition.relative(facing.getOpposite()));
		}
		if (tickable.getTicks() % 40 == 0) {
			output.update();
		}
		receiveLimitLeft = powerOutput * currentCapacityMultiplier;
		if (electro.getJoulesStored() > 0 && output.valid()) {
			electro.joules(electro.getJoulesStored() - ElectricityUtilities
					.receivePower(output.getSafe(), facing, TransferPack
							.joulesVoltage(Math.min(electro.getJoulesStored(), powerOutput * currentCapacityMultiplier), electro.getVoltage()), false)
					.getJoules());
		}
		currentCapacityMultiplier = 1;
		int currentVoltageMultiplier = 1;
		for (ItemStack stack : this.<ComponentInventory>getComponent(ComponentType.Inventory).getItems()) {
			if (!stack.isEmpty() && stack.getItem() instanceof ItemUpgrade upgrade) {
				for (int i = 0; i < stack.getCount(); i++) {
					upgrade.subtype.applyUpgrade.accept(this, null, null);
				}
			}
		}
		electro.maxJoules(maxJoules * currentCapacityMultiplier);
		electro.voltage(480.0 * currentVoltageMultiplier);
		if (electro.getJoulesStored() > electro.getMaxJoulesStored()) {
			electro.joules(electro.getMaxJoulesStored());
		}
		if (tickable.getTicks() % 50 == 0) {
			this.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendCustomPacket();
		}
		electro.drainElectricItem(3);
	}
}