package electrodynamics.prefab.inventory.container.slot.item.type;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.inventory.container.slot.item.SlotGeneric;
import electrodynamics.prefab.screen.component.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.ScreenComponentSlot.SlotType;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SlotUpgrade extends SlotGeneric {

	private List<Item> items;

	public SlotUpgrade(Container inventory, int index, int x, int y, SubtypeItemUpgrade... upgrades) {
		super(SlotType.NORMAL, IconType.UPGRADE_DARK, inventory, index, x, y);

		items = new ArrayList<>();
		for (SubtypeItemUpgrade upg : upgrades) {
			items.add(ElectrodynamicsItems.SUBTYPEITEMREGISTER_MAPPINGS.get(upg).get());
		}
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return items != null && items.contains(stack.getItem());
	}

}
