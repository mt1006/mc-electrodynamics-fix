package electrodynamics.prefab.inventory.container.slot.item.type;

import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import electrodynamics.prefab.screen.component.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.ScreenComponentSlot.SlotType;
import electrodynamics.prefab.utilities.CapabilityUtils;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class SlotFluid extends SlotGeneric {

	public SlotFluid(Container inventory, int index, int x, int y) {
		super(SlotType.NORMAL, IconType.FLUID_DARK, inventory, index, x, y);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		if (super.mayPlace(stack) && CapabilityUtils.hasFluidItemCap(stack)) {
			return true;
		}
		return false;
	}

}
