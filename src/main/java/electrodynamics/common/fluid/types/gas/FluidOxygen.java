package electrodynamics.common.fluid.types.gas;

import electrodynamics.api.References;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.types.SimpleWaterBasedFluidType;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraftforge.fluids.FluidType;

public class FluidOxygen extends FluidNonPlaceable {

	public static final String FORGE_TAG = "oxygen";

	private final FluidType type;

	public FluidOxygen() {
		super(() -> ElectrodynamicsItems.ITEM_CANISTERREINFORCED);
		type = new SimpleWaterBasedFluidType(References.ID, "fluidoxygen");
	}

	@Override
	public FluidType getFluidType() {
		return type;
	}
}
