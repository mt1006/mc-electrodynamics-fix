package electrodynamics.common.block.subtype;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import electrodynamics.api.ISubtype;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.item.subtype.SubtypeDust;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.item.subtype.SubtypeRawOre;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public enum SubtypeOre implements ISubtype {

	// default mc values in OrePlacements.class

	// min + max + perChunk from copper
	tin(1, 16, 9, -16, 112, 3f, 5f, 1, 1, () -> ElectrodynamicsItems.getItem(SubtypeRawOre.tin), () -> ElectrodynamicsItems.getItem(SubtypeIngot.tin), 0.7, 200, ElectrodynamicsTags.Items.ORE_TIN, ElectrodynamicsTags.Blocks.ORE_TIN),
	// min + max + perChunk based on gold
	silver(2, 8, 9, -64, 48, 4f, 5.5f, 1, 1, () -> ElectrodynamicsItems.getItem(SubtypeRawOre.silver), () -> ElectrodynamicsItems.getItem(SubtypeIngot.silver), 1.0, 200, ElectrodynamicsTags.Items.ORE_SILVER, ElectrodynamicsTags.Blocks.ORE_SILVER),
	// min + max + perChunk based on silver
	lead(2, 15, 6, -64, 32, 4f, 8f, 1, 1, () -> ElectrodynamicsItems.getItem(SubtypeRawOre.lead), () -> ElectrodynamicsItems.getItem(SubtypeIngot.lead), 0.7, 200, ElectrodynamicsTags.Items.ORE_LEAD, ElectrodynamicsTags.Blocks.ORE_LEAD),
	// min + max + perChunkbasedon diamond
	uraninite(3, 6, 8, -64, -32, 10f, 6.5f, 1, 1, () -> ElectrodynamicsItems.getItem(SubtypeRawOre.uranium), null, 0, 0, ElectrodynamicsTags.Items.ORE_URANIUM, ElectrodynamicsTags.Blocks.ORE_URANIUM),
	// min + max + perChunk based on thorianite
	thorianite(3, 15, 8, -32, 32, 10f, 6.5f, 1, 1, () -> ElectrodynamicsItems.getItem(SubtypeRawOre.thorium), null, 0, 0, ElectrodynamicsTags.Items.ORE_THORIUM, ElectrodynamicsTags.Blocks.ORE_THORIUM),
	// not changed
	monazite(2, 5, 8, 0, 32, 6f, 4.5f, 1, 1, null, null, 0, 0, ElectrodynamicsTags.Items.ORE_MONAZITE, ElectrodynamicsTags.Blocks.ORE_MONAZITE),
	// doubled depth + amount
	vanadinite(2, 12, 8, -32, 32, 6f, 4.5f, 1, 1, () -> ElectrodynamicsItems.getItem(SubtypeRawOre.vanadinite), () -> ElectrodynamicsItems.getItem(SubtypeIngot.vanadium), 0.7, 200, ElectrodynamicsTags.Items.ORE_VANADIUM, ElectrodynamicsTags.Blocks.ORE_VANADIUM),
	// increased depth + amount - amonut because of proper sulfur gen
	sulfur(1, 4, 13, -28, 28, 6f, 4.5f, 1, 2, 3, 5, () -> ElectrodynamicsItems.getItem(SubtypeDust.sulfur), null, 0, 0, ElectrodynamicsTags.Items.ORE_SULFUR, ElectrodynamicsTags.Blocks.ORE_SULFUR),
	// not changed
	niter(1, 6, 13, 0, 28, 6f, 4.5f, 1, 2, 1, 3, () -> ElectrodynamicsItems.getItem(SubtypeDust.niter), null, 0, 0, ElectrodynamicsTags.Items.ORE_SALTPETER, ElectrodynamicsTags.Blocks.ORE_SALTPETER),
	// increased height
	aluminum(2, 5, 5, 16, 48, 4f, 4.4f, 1, 1, null, null, 0, 0, ElectrodynamicsTags.Items.ORE_ALUMINUM, ElectrodynamicsTags.Blocks.ORE_ALUMINUM),
	// increased depth + amount
	chromite(3, 5, 8, -10, 32, 10f, 6.5f, 1, 1, () -> ElectrodynamicsItems.getItem(SubtypeRawOre.chromium), null, 0, 0, ElectrodynamicsTags.Items.ORE_CHROMIUM, ElectrodynamicsTags.Blocks.ORE_CHROMIUM),
	// min + max + perChunk based on diamond
	rutile(3, 5, 8, -48, -8, 10f, 6.5f, 1, 1, () -> ElectrodynamicsItems.getItem(SubtypeRawOre.titanium), null, 0, 0, ElectrodynamicsTags.Items.ORE_TITANIUM, ElectrodynamicsTags.Blocks.ORE_TITANIUM),
	// nonchanged
	halite(1, 9, 9, 32, 64, 3f, 5f, 0, 2, 1, 3, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.halite), null, 0, 0, ElectrodynamicsTags.Items.ORE_SALT, ElectrodynamicsTags.Blocks.ORE_SALT),
	// increased depth
	lepidolite(2, 3, 9, -32, 32, 4f, 8f, 1, 1, () -> ElectrodynamicsItems.getItem(SubtypeRawOre.lepidolite), null, 0, 0, ElectrodynamicsTags.Items.ORE_LITHIUM, ElectrodynamicsTags.Blocks.ORE_LITHIUM),
	// unchanged
	molybdenum(1, 9, 4, 50, 60, 3f, 3f, 1, 1, null, () -> ElectrodynamicsItems.getItem(SubtypeIngot.molybdenum), 0.1, 200, ElectrodynamicsTags.Items.ORE_MOLYBDENUM, ElectrodynamicsTags.Blocks.ORE_MOLYBDENUM),
	// increased depth
	fluorite(1, 3, 4, -10, 30, 2f, 2f, 1, 1, () -> ElectrodynamicsItems.getItem(SubtypeRawOre.fluorite), null, 0, 0, ElectrodynamicsTags.Items.ORE_FLUORITE, ElectrodynamicsTags.Blocks.ORE_FLUORITE),
	// increased depth + amount
	sylvite(1, 5, 4, -20, 50, 2f, 2f, 3, 7, 1, 3, () -> ElectrodynamicsItems.getItem(SubtypeCrystal.potassiumchloride), null, 0, 0, ElectrodynamicsTags.Items.ORE_POTASSIUMCHLORIDE, ElectrodynamicsTags.Blocks.ORE_POTASSIUMCHLORIDE);

	public final int harvestLevel;
	public final int veinsPerChunk;
	public final int veinSize;
	public final int minY;
	public final int maxY;
	public final float hardness;
	public final float resistance;
	public final int minXP;
	public final int maxXP;
	public final int minDrop;
	public final int maxDrop;
	// Leaving this null will result in the ore dropping itself when broken
	@Nullable
	public final Supplier<Item> nonSilkLootItem;
	// Leaving this null will result in the ore not smelting into anything
	@Nullable
	public final Supplier<Item> smeltingItem;
	public final double smeltingXp;
	public final int smeltingTime;
	public final TagKey<Item> itemTag;
	public final TagKey<Block> blockTag;

	SubtypeOre(int harvestLevel, int veinsPerChunk, int veinSize, int minY, int maxY, float hardness, float resistance, int minDrop, int maxDrop, Supplier<Item> lootItem, Supplier<Item> smeltingItem, double smeltingXp, int smeltingTime, TagKey<Item> itemTag, TagKey<Block> blockTag) {
		this(harvestLevel, veinsPerChunk, veinSize, minY, maxY, hardness, resistance, 0, 0, minDrop, maxDrop, lootItem, smeltingItem, smeltingXp, smeltingTime, itemTag, blockTag);
	}

	SubtypeOre(int harvestLevel, int veinsPerChunk, int veinSize, int minY, int maxY, float hardness, float resistance, int minXP, int maxXP, int minDrop, int maxDrop, Supplier<Item> lootItem, Supplier<Item> smeltingItem, double smeltingXp, int smeltingTime, TagKey<Item> itemTag, TagKey<Block> blockTag) {

		this.harvestLevel = harvestLevel;
		this.veinsPerChunk = veinsPerChunk;
		this.veinSize = veinSize;
		this.resistance = resistance;
		this.minY = minY;
		this.maxY = maxY;
		this.hardness = hardness;
		this.minXP = minXP;
		this.maxXP = maxXP;
		this.minDrop = minDrop;
		this.maxDrop = maxDrop;
		nonSilkLootItem = lootItem;
		this.smeltingItem = smeltingItem;
		this.smeltingXp = smeltingXp;
		this.smeltingTime = smeltingTime;
		this.itemTag = itemTag;
		this.blockTag = blockTag;
	}

	@Override
	public String tag() {
		return "ore" + name();
	}

	@Override
	public String forgeTag() {
		return "ores/" + name();
	}

	@Override
	public boolean isItem() {
		return false;
	}

	public static SubtypeOre[] getOreForMiningLevel(int level) {
		List<SubtypeOre> values = new ArrayList<>();
		for (SubtypeOre value : values()) {
			if (value.harvestLevel == level) {
				values.add(value);
			}
		}
		return values.toArray(new SubtypeOre[] {});
	}

}
