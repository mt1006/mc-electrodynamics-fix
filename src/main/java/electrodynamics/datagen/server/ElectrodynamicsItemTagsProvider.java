package electrodynamics.datagen.server;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.block.subtype.SubtypeRawOreBlock;
import electrodynamics.common.block.subtype.SubtypeResourceBlock;
import electrodynamics.common.item.subtype.SubtypeCircuit;
import electrodynamics.common.item.subtype.SubtypeDust;
import electrodynamics.common.item.subtype.SubtypeGear;
import electrodynamics.common.item.subtype.SubtypeImpureDust;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.item.subtype.SubtypeNugget;
import electrodynamics.common.item.subtype.SubtypeOxide;
import electrodynamics.common.item.subtype.SubtypePlate;
import electrodynamics.common.item.subtype.SubtypeRawOre;
import electrodynamics.common.item.subtype.SubtypeRod;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ElectrodynamicsItemTagsProvider extends ItemTagsProvider {

	public ElectrodynamicsItemTagsProvider(DataGenerator generator, BlockTagsProvider provider, ExistingFileHelper existingFileHelper) {
		super(generator, provider, References.ID, existingFileHelper);
	}

	@Override
	protected void addTags() {

		for (SubtypeCircuit circuit : SubtypeCircuit.values()) {
			tag(circuit.tag).add(ElectrodynamicsItems.getItem(circuit));
		}

		for (SubtypeDust dust : SubtypeDust.values()) {
			tag(dust.tag).add(ElectrodynamicsItems.getItem(dust));
		}

		for (SubtypeGear gear : SubtypeGear.values()) {
			tag(gear.tag).add(ElectrodynamicsItems.getItem(gear));
		}

		for (SubtypeImpureDust dust : SubtypeImpureDust.values()) {
			tag(dust.tag).add(ElectrodynamicsItems.getItem(dust));
		}

		for (SubtypeIngot ingot : SubtypeIngot.values()) {
			tag(ingot.tag).add(ElectrodynamicsItems.getItem(ingot));
		}

		for (SubtypeNugget nugget : SubtypeNugget.values()) {
			tag(nugget.tag).add(ElectrodynamicsItems.getItem(nugget));
		}

		for (SubtypeOre ore : SubtypeOre.values()) {
			tag(ore.itemTag).add(ElectrodynamicsItems.getItem(ore));
		}

		for (SubtypeOreDeepslate ore : SubtypeOreDeepslate.values()) {
			tag(ore.itemTag).add(ElectrodynamicsItems.getItem(ore));
		}

		for (SubtypeOxide oxide : SubtypeOxide.values()) {
			tag(oxide.tag).add(ElectrodynamicsItems.getItem(oxide));
		}

		for (SubtypeRod rod : SubtypeRod.values()) {
			tag(rod.tag).add(ElectrodynamicsItems.getItem(rod));
		}

		for (SubtypeRawOre raw : SubtypeRawOre.values()) {
			tag(raw.tag).add(ElectrodynamicsItems.getItem(raw));
		}

		for (SubtypePlate plate : SubtypePlate.values()) {
			tag(plate.tag).add(ElectrodynamicsItems.getItem(plate));
		}

		for (SubtypeOxide oxide : SubtypeOxide.values()) {
			tag(oxide.tag).add(ElectrodynamicsItems.getItem(oxide));
		}

		for (SubtypeResourceBlock storage : SubtypeResourceBlock.values()) {
			tag(storage.itemTag).add(ElectrodynamicsItems.getItem(storage));
		}

		for (SubtypeRawOreBlock block : SubtypeRawOreBlock.values()) {
			tag(block.itemTag).add(ElectrodynamicsItems.getItem(block));
		}

		TagAppender<Item> gears = tag(ElectrodynamicsTags.Items.GEARS);

		for (SubtypeGear gear : SubtypeGear.values()) {
			gears.addTag(gear.tag);
		}

		TagAppender<Item> ingots = tag(ElectrodynamicsTags.Items.INGOTS);

		for (SubtypeIngot ingot : SubtypeIngot.values()) {
			ingots.addTag(ingot.tag);
		}

		TagAppender<Item> ores = tag(ElectrodynamicsTags.Items.ORES);

		for (SubtypeOre ore : SubtypeOre.values()) {
			ores.addTag(ore.itemTag);
		}

		tag(ElectrodynamicsTags.Items.COAL_COKE).add(ElectrodynamicsItems.COAL_COKE.get());

		tag(ElectrodynamicsTags.Items.PLASTIC).add(ElectrodynamicsItems.ITEM_SHEETPLASTIC.get());

		tag(ElectrodynamicsTags.Items.SLAG).add(ElectrodynamicsItems.SLAG.get());

		tag(ElectrodynamicsTags.Items.INSULATES_PLAYER_FEET).add(ElectrodynamicsItems.ITEM_RUBBERBOOTS.get(), ElectrodynamicsItems.ITEM_COMPOSITEBOOTS.get(), ElectrodynamicsItems.ITEM_COMBATBOOTS.get());

	}

}
