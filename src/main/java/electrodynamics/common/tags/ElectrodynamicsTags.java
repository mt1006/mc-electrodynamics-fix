package electrodynamics.common.tags;

import electrodynamics.api.gas.Gas;
import electrodynamics.common.fluid.types.gas.FluidHydrogen;
import electrodynamics.common.fluid.types.gas.FluidOxygen;
import electrodynamics.common.fluid.types.liquid.FluidClay;
import electrodynamics.common.fluid.types.liquid.FluidEthanol;
import electrodynamics.common.fluid.types.liquid.FluidHydraulic;
import electrodynamics.common.fluid.types.liquid.FluidHydrogenFluoride;
import electrodynamics.common.fluid.types.liquid.FluidPolyethylene;
import electrodynamics.common.fluid.types.liquid.FluidSulfate;
import electrodynamics.common.fluid.types.liquid.FluidSulfuricAcid;
import electrodynamics.registers.ElectrodynamicsRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ElectrodynamicsTags {

	public static void init() {
		Items.init();
		Blocks.init();
		Fluids.init();
	}

	public static class Items {

		public static final TagKey<Item> CIRCUITS_BASIC = forgeTag("circuits/basic");
		public static final TagKey<Item> CIRCUITS_ADVANCED = forgeTag("circuits/advanced");
		public static final TagKey<Item> CIRCUITS_ELITE = forgeTag("circuits/elite");
		public static final TagKey<Item> CIRCUITS_ULTIMATE = forgeTag("circuits/ultimate");

		public static final TagKey<Item> DUST_BRONZE = forgeTag("dusts/bronze");
		public static final TagKey<Item> DUST_COPPER = forgeTag("dusts/copper");
		public static final TagKey<Item> DUST_ENDEREYE = forgeTag("dusts/endereye");
		public static final TagKey<Item> DUST_GOLD = forgeTag("dusts/gold");
		public static final TagKey<Item> DUST_IRON = forgeTag("dusts/iron");
		public static final TagKey<Item> DUST_LEAD = forgeTag("dusts/lead");
		public static final TagKey<Item> DUST_LITHIUM = forgeTag("dusts/lithium");
		public static final TagKey<Item> DUST_MOLYBDENUM = forgeTag("dusts/molybdenum");
		public static final TagKey<Item> DUST_NETHERITE = forgeTag("dusts/netherite");
		public static final TagKey<Item> DUST_OBSIDIAN = forgeTag("dusts/obsidian");
		public static final TagKey<Item> DUST_SALT = forgeTag("dusts/salt");
		public static final TagKey<Item> DUST_SALTPETER = forgeTag("dusts/saltpeter");
		public static final TagKey<Item> DUST_SILICA = forgeTag("dusts/silica");
		public static final TagKey<Item> DUST_SILVER = forgeTag("dusts/silver");
		public static final TagKey<Item> DUST_STEEL = forgeTag("dusts/steel");
		public static final TagKey<Item> DUST_SULFUR = forgeTag("dusts/sulfur");
		public static final TagKey<Item> DUST_SUPERCONDUCTIVE = forgeTag("dusts/superconductive");
		public static final TagKey<Item> DUST_TIN = forgeTag("dusts/tin");
		public static final TagKey<Item> DUST_VANADIUM = forgeTag("dusts/vanadium");

		public static final TagKey<Item> GEAR_BRONZE = forgeTag("gears/bronze");
		public static final TagKey<Item> GEAR_COPPER = forgeTag("gears/copper");
		public static final TagKey<Item> GEAR_IRON = forgeTag("gears/iron");
		public static final TagKey<Item> GEAR_STEEL = forgeTag("gears/steel");
		public static final TagKey<Item> GEAR_TIN = forgeTag("gears/tin");

		public static final TagKey<Item> IMPURE_DUST_COPPER = forgeTag("impuredust/copper");
		public static final TagKey<Item> IMPURE_DUST_GOLD = forgeTag("impuredust/gold");
		public static final TagKey<Item> IMPURE_DUST_IRON = forgeTag("impuredust/iron");
		public static final TagKey<Item> IMPURE_DUST_LEAD = forgeTag("impuredust/lead");
		public static final TagKey<Item> IMPURE_DUST_LITHIUM = forgeTag("impuredust/lithium");
		public static final TagKey<Item> IMPURE_DUST_MOLYBDENUM = forgeTag("impuredust/molybdenum");
		public static final TagKey<Item> IMPURE_DUST_NETHERITE = forgeTag("impuredust/netherite");
		public static final TagKey<Item> IMPURE_DUST_SILVER = forgeTag("impuredust/silver");
		public static final TagKey<Item> IMPURE_DUST_TIN = forgeTag("impuredust/tin");
		public static final TagKey<Item> IMPURE_DUST_VANADIUM = forgeTag("impuredust/vanadium");

		public static final TagKey<Item> INGOT_ALUMINUM = forgeTag("ingots/aluminum");
		public static final TagKey<Item> INGOT_BRONZE = forgeTag("ingots/bronze");
		public static final TagKey<Item> INGOT_CHROMIUM = forgeTag("ingots/chromium");
		public static final TagKey<Item> INGOT_HSLASTEEL = forgeTag("ingots/hslasteel");
		public static final TagKey<Item> INGOT_LEAD = forgeTag("ingots/lead");
		public static final TagKey<Item> INGOT_LITHIUM = forgeTag("ingots/lithium");
		public static final TagKey<Item> INGOT_MOLYBDENUM = forgeTag("ingots/molybdenum");
		public static final TagKey<Item> INGOT_SILVER = forgeTag("ingots/silver");
		public static final TagKey<Item> INGOT_STAINLESSSTEEL = forgeTag("ingots/stainlesssteel");
		public static final TagKey<Item> INGOT_STEEL = forgeTag("ingots/steel");
		public static final TagKey<Item> INGOT_SUPERCONDUCTIVE = forgeTag("ingots/superconductive");
		public static final TagKey<Item> INGOT_TIN = forgeTag("ingots/tin");
		public static final TagKey<Item> INGOT_TITANIUM = forgeTag("ingots/titanium");
		public static final TagKey<Item> INGOT_TITANIUMCARBIDE = forgeTag("ingots/titaniumcarbide");
		public static final TagKey<Item> INGOT_VANADIUM = forgeTag("ingots/vanadium");
		public static final TagKey<Item> INGOT_VANADIUMSTEEL = forgeTag("ingots/vanadiumsteel");

		public static final TagKey<Item> NUGGET_COPPER = forgeTag("nuggets/copper");
		public static final TagKey<Item> NUGGET_HSLASTEEL = forgeTag("nuggets/hslasteel");
		public static final TagKey<Item> NUGGET_SILVER = forgeTag("nuggets/silver");
		public static final TagKey<Item> NUGGET_STAINLESSSTEEL = forgeTag("nuggets/stainlesssteel");
		public static final TagKey<Item> NUGGET_STEEL = forgeTag("nuggets/steel");
		public static final TagKey<Item> NUGGET_SUPERCONDUCTIVE = forgeTag("nuggets/superconductive");
		public static final TagKey<Item> NUGGET_TIN = forgeTag("nuggets/tin");
		public static final TagKey<Item> NUGGET_TITANIUMCARBIDE = forgeTag("nuggets/titaniumcarbide");

		public static final TagKey<Item> ORE_ALUMINUM = forgeTag("ores/aluminum");
		public static final TagKey<Item> ORE_CHROMIUM = forgeTag("ores/chromium");
		public static final TagKey<Item> ORE_COPPER = forgeTag("ores/copper");
		public static final TagKey<Item> ORE_FLUORITE = forgeTag("ores/fluorite");
		public static final TagKey<Item> ORE_LEAD = forgeTag("ores/lead");
		public static final TagKey<Item> ORE_LITHIUM = forgeTag("ores/lithium");
		public static final TagKey<Item> ORE_MOLYBDENUM = forgeTag("ores/molybdenum");
		public static final TagKey<Item> ORE_MONAZITE = forgeTag("ores/monazite");
		public static final TagKey<Item> ORE_POTASSIUMCHLORIDE = forgeTag("ores/potassiumchloride");
		public static final TagKey<Item> ORE_SALT = forgeTag("ores/salt");
		public static final TagKey<Item> ORE_SALTPETER = forgeTag("ores/saltpeter");
		public static final TagKey<Item> ORE_SILVER = forgeTag("ores/silver");
		public static final TagKey<Item> ORE_SULFUR = forgeTag("ores/sulfur");
		public static final TagKey<Item> ORE_THORIUM = forgeTag("ores/thorium");
		public static final TagKey<Item> ORE_TIN = forgeTag("ores/tin");
		public static final TagKey<Item> ORE_TITANIUM = forgeTag("ores/titanium");
		public static final TagKey<Item> ORE_URANIUM = forgeTag("ores/uranium");
		public static final TagKey<Item> ORE_VANADIUM = forgeTag("ores/vanadium");

		public static final TagKey<Item> OXIDE_CALCIUMCARBONATE = forgeTag("oxide/calciumcarbonate");
		public static final TagKey<Item> OXIDE_CHROMIUM = forgeTag("oxide/chromium");
		public static final TagKey<Item> OXIDE_CHROMIUMDISILICIDE = forgeTag("oxide/chromiumdisilicide");
		public static final TagKey<Item> OXIDE_DISULFUR = forgeTag("oxide/disulfur");
		public static final TagKey<Item> OXIDE_DITITANIUM = forgeTag("oxide/dititanium");
		public static final TagKey<Item> OXIDE_SODIUMCARBONATE = forgeTag("oxide/sodiumcarbonate");
		public static final TagKey<Item> OXIDE_SULFURDICHLORIDE = forgeTag("oxide/sulfurdichloride");
		public static final TagKey<Item> OXIDE_THIONYLCHLORIDE = forgeTag("oxide/thionylchloride");
		public static final TagKey<Item> OXIDE_TRISULFUR = forgeTag("oxide/trisulfur");
		public static final TagKey<Item> OXIDE_VANADIUM = forgeTag("oxide/vanadium");

		public static final TagKey<Item> PLATE_ALUMINUM = forgeTag("plates/aluminum");
		public static final TagKey<Item> PLATE_BRONZE = forgeTag("plates/bronze");
		public static final TagKey<Item> PLATE_HSLASTEEL = forgeTag("plates/hslasteel");
		public static final TagKey<Item> PLATE_IRON = forgeTag("plates/iron");
		public static final TagKey<Item> PLATE_LEAD = forgeTag("plates/lead");
		public static final TagKey<Item> PLATE_LITHIUM = forgeTag("plates/lithium");
		public static final TagKey<Item> PLATE_STAINLESSSTEEL = forgeTag("plates/stainlesssteel");
		public static final TagKey<Item> PLATE_STEEL = forgeTag("plates/steel");
		public static final TagKey<Item> PLATE_TITANIUM = forgeTag("plates/titanium");
		public static final TagKey<Item> PLATE_TITANIUMCARBIDE = forgeTag("plates/titaniumcarbide");
		public static final TagKey<Item> PLATE_VANADIUMSTEEL = forgeTag("plates/vanadiumsteel");

		public static final TagKey<Item> RAW_ORE_CHROMIUM = forgeTag("raw_materials/chromium");
		public static final TagKey<Item> RAW_ORE_FLUORITE = forgeTag("raw_materials/fluorite");
		public static final TagKey<Item> RAW_ORE_LEAD = forgeTag("raw_materials/lead");
		public static final TagKey<Item> RAW_ORE_LEPIDOLITE = forgeTag("raw_materials/lepidolite");
		public static final TagKey<Item> RAW_ORE_SILVER = forgeTag("raw_materials/silver");
		public static final TagKey<Item> RAW_ORE_THORIUM = forgeTag("raw_materials/thorium");
		public static final TagKey<Item> RAW_ORE_TIN = forgeTag("raw_materials/tin");
		public static final TagKey<Item> RAW_ORE_TITANIUM = forgeTag("raw_materials/titanium");
		public static final TagKey<Item> RAW_ORE_URANIUM = forgeTag("raw_materials/uranium");
		public static final TagKey<Item> RAW_ORE_VANADIUM = forgeTag("raw_materials/vanadinite");

		public static final TagKey<Item> ROD_HSLASTEEL = forgeTag("rods/hslasteel");
		public static final TagKey<Item> ROD_STAINLESSSTEEL = forgeTag("rods/stainlesssteel");
		public static final TagKey<Item> ROD_STEEL = forgeTag("rods/steel");
		public static final TagKey<Item> ROD_TITANIUMCARBIDE = forgeTag("rods/titaniumcarbide");

		public static final TagKey<Item> STORAGE_BLOCK_ALUMINUM = forgeTag("storage_blocks/aluminum");
		public static final TagKey<Item> STORAGE_BLOCK_BRONZE = forgeTag("storage_blocks/bronze");
		public static final TagKey<Item> STORAGE_BLOCK_CHROMIUM = forgeTag("storage_blocks/chromium");
		public static final TagKey<Item> STORAGE_BLOCK_COPPER = forgeTag("storage_blocks/copper");
		public static final TagKey<Item> STORAGE_BLOCK_HSLASTEEL = forgeTag("storage_blocks/hslasteel");
		public static final TagKey<Item> STORAGE_BLOCK_LEAD = forgeTag("storage_blocks/lead");
		public static final TagKey<Item> STORAGE_BLOCK_SILVER = forgeTag("storage_blocks/silver");
		public static final TagKey<Item> STORAGE_BLOCK_STAINLESSSTEEL = forgeTag("storage_blocks/stainlesssteel");
		public static final TagKey<Item> STORAGE_BLOCK_STEEL = forgeTag("storage_blocks/steel");
		public static final TagKey<Item> STORAGE_BLOCK_TIN = forgeTag("storage_blocks/tin");
		public static final TagKey<Item> STORAGE_BLOCK_TITANIUM = forgeTag("storage_blocks/titanium");
		public static final TagKey<Item> STORAGE_BLOCK_TITANIUMCARBIDE = forgeTag("storage_blocks/titaniumcarbide");
		public static final TagKey<Item> STORAGE_BLOCK_VANADIUMSTEEL = forgeTag("storage_blocks/vanadiumsteel");
		
		public static final TagKey<Item> BLOCK_RAW_ORE_CHROMIUM = forgeTag("storage_blocks/rawchromium");
		public static final TagKey<Item> BLOCK_RAW_ORE_FLUORITE = forgeTag("storage_blocks/rawfluorite");
		public static final TagKey<Item> BLOCK_RAW_ORE_LEAD = forgeTag("storage_blocks/rawlead");
		public static final TagKey<Item> BLOCK_RAW_ORE_LEPIDOLITE = forgeTag("storage_blocks/rawlepidolite");
		public static final TagKey<Item> BLOCK_RAW_ORE_SILVER = forgeTag("storage_blocks/rawsilver");
		public static final TagKey<Item> BLOCK_RAW_ORE_THORIUM = forgeTag("storage_blocks/rawthorium");
		public static final TagKey<Item> BLOCK_RAW_ORE_TIN = forgeTag("storage_blocks/rawtin");
		public static final TagKey<Item> BLOCK_RAW_ORE_TITANIUM = forgeTag("storage_blocks/rawtitanium");
		public static final TagKey<Item> BLOCK_RAW_ORE_URANIUM = forgeTag("storage_blocks/rawuranium");
		public static final TagKey<Item> BLOCK_RAW_ORE_VANADIUM = forgeTag("storage_blocks/rawvanadinite");

		public static final TagKey<Item> COAL_COKE = forgeTag("coal_coke");
		public static final TagKey<Item> PLASTIC = forgeTag("plastic");
		public static final TagKey<Item> SLAG = forgeTag("slag");

		public static final TagKey<Item> GEARS = forgeTag("gears");
		public static final TagKey<Item> INGOTS = forgeTag("ingots");
		public static final TagKey<Item> ORES = forgeTag("ores");
		public static final TagKey<Item> INSULATES_PLAYER_FEET = forgeTag("insulates_player_feet");

		private static void init() {
		}

		private static TagKey<Item> forgeTag(String name) {
			return ItemTags.create(new ResourceLocation("forge", name));
		}
	}

	public static class Blocks {

		public static final TagKey<Block> ORE_ALUMINUM = forgeTag("ores/aluminum");
		public static final TagKey<Block> ORE_CHROMIUM = forgeTag("ores/chromium");
		public static final TagKey<Block> ORE_COPPER = forgeTag("ores/copper");
		public static final TagKey<Block> ORE_FLUORITE = forgeTag("ores/fluorite");
		public static final TagKey<Block> ORE_LEAD = forgeTag("ores/lead");
		public static final TagKey<Block> ORE_LITHIUM = forgeTag("ores/lithium");
		public static final TagKey<Block> ORE_MOLYBDENUM = forgeTag("ores/molybdenum");
		public static final TagKey<Block> ORE_MONAZITE = forgeTag("ores/monazite");
		public static final TagKey<Block> ORE_POTASSIUMCHLORIDE = forgeTag("ores/potassiumchloride");
		public static final TagKey<Block> ORE_SALT = forgeTag("ores/salt");
		public static final TagKey<Block> ORE_SALTPETER = forgeTag("ores/saltpeter");
		public static final TagKey<Block> ORE_SILVER = forgeTag("ores/silver");
		public static final TagKey<Block> ORE_SULFUR = forgeTag("ores/sulfur");
		public static final TagKey<Block> ORE_THORIUM = forgeTag("ores/thorium");
		public static final TagKey<Block> ORE_TIN = forgeTag("ores/tin");
		public static final TagKey<Block> ORE_TITANIUM = forgeTag("ores/titanium");
		public static final TagKey<Block> ORE_URANIUM = forgeTag("ores/uranium");
		public static final TagKey<Block> ORE_VANADIUM = forgeTag("ores/vanadium");

		public static final TagKey<Block> STORAGE_BLOCK_ALUMINUM = forgeTag("storage_blocks/aluminum");
		public static final TagKey<Block> STORAGE_BLOCK_BRONZE = forgeTag("storage_blocks/bronze");
		public static final TagKey<Block> STORAGE_BLOCK_CHROMIUM = forgeTag("storage_blocks/chromium");
		public static final TagKey<Block> STORAGE_BLOCK_COPPER = forgeTag("storage_blocks/copper");
		public static final TagKey<Block> STORAGE_BLOCK_HSLASTEEL = forgeTag("storage_blocks/hslasteel");
		public static final TagKey<Block> STORAGE_BLOCK_LEAD = forgeTag("storage_blocks/lead");
		public static final TagKey<Block> STORAGE_BLOCK_SILVER = forgeTag("storage_blocks/silver");
		public static final TagKey<Block> STORAGE_BLOCK_STAINLESSSTEEL = forgeTag("storage_blocks/stainlesssteel");
		public static final TagKey<Block> STORAGE_BLOCK_STEEL = forgeTag("storage_blocks/steel");
		public static final TagKey<Block> STORAGE_BLOCK_TIN = forgeTag("storage_blocks/tin");
		public static final TagKey<Block> STORAGE_BLOCK_TITANIUM = forgeTag("storage_blocks/titanium");
		public static final TagKey<Block> STORAGE_BLOCK_TITANIUMCARBIDE = forgeTag("storage_blocks/titaniumcarbide");
		public static final TagKey<Block> STORAGE_BLOCK_VANADIUMSTEEL = forgeTag("storage_blocks/vanadiumsteel");
		
		public static final TagKey<Block> BLOCK_RAW_ORE_CHROMIUM = forgeTag("storage_blocks/rawchromium");
		public static final TagKey<Block> BLOCK_RAW_ORE_FLUORITE = forgeTag("storage_blocks/rawfluorite");
		public static final TagKey<Block> BLOCK_RAW_ORE_LEAD = forgeTag("storage_blocks/rawlead");
		public static final TagKey<Block> BLOCK_RAW_ORE_LEPIDOLITE = forgeTag("storage_blocks/rawlepidolite");
		public static final TagKey<Block> BLOCK_RAW_ORE_SILVER = forgeTag("storage_blocks/rawsilver");
		public static final TagKey<Block> BLOCK_RAW_ORE_THORIUM = forgeTag("storage_blocks/rawthorium");
		public static final TagKey<Block> BLOCK_RAW_ORE_TIN = forgeTag("storage_blocks/rawtin");
		public static final TagKey<Block> BLOCK_RAW_ORE_TITANIUM = forgeTag("storage_blocks/rawtitanium");
		public static final TagKey<Block> BLOCK_RAW_ORE_URANIUM = forgeTag("storage_blocks/rawuranium");
		public static final TagKey<Block> BLOCK_RAW_ORE_VANADIUM = forgeTag("storage_blocks/rawvanadinite");

		public static final TagKey<Block> ORES = forgeTag("ores");

		private static void init() {
		}

		private static TagKey<Block> forgeTag(String name) {
			return BlockTags.create(new ResourceLocation("forge", name));
		}

	}

	// Only the Tag objects should ever be visible from this class!
	public static class Fluids {

		public static final TagKey<Fluid> SULFURIC_ACID = forgeTag(FluidSulfuricAcid.FORGE_TAG);
		public static final TagKey<Fluid> ETHANOL = forgeTag(FluidEthanol.FORGE_TAG);
		public static final TagKey<Fluid> HYDROGEN_FLUORIDE = forgeTag(FluidHydrogenFluoride.FORGE_TAG);
		public static final TagKey<Fluid> POLYETHLYENE = forgeTag(FluidPolyethylene.FORGE_TAG);
		public static final TagKey<Fluid> COPPER_SULF = forgeTag(FluidSulfate.FORGE_TAG + "copper");
		public static final TagKey<Fluid> TIN_SULF = forgeTag(FluidSulfate.FORGE_TAG + "tin");
		public static final TagKey<Fluid> SILVER_SULF = forgeTag(FluidSulfate.FORGE_TAG + "silver");
		public static final TagKey<Fluid> LEAD_SULF = forgeTag(FluidSulfate.FORGE_TAG + "lead");
		public static final TagKey<Fluid> VANADIUM_SULF = forgeTag(FluidSulfate.FORGE_TAG + "vanadium");
		public static final TagKey<Fluid> IRON_SULF = forgeTag(FluidSulfate.FORGE_TAG + "iron");
		public static final TagKey<Fluid> GOLD_SULF = forgeTag(FluidSulfate.FORGE_TAG + "gold");
		public static final TagKey<Fluid> LITHIUM_SULF = forgeTag(FluidSulfate.FORGE_TAG + "lithium");
		public static final TagKey<Fluid> MOLYBDENUM_SULF = forgeTag(FluidSulfate.FORGE_TAG + "molybdenum");
		public static final TagKey<Fluid> NETHERITE_SULF = forgeTag(FluidSulfate.FORGE_TAG + "netherite");
		public static final TagKey<Fluid> CLAY = forgeTag(FluidClay.FORGE_TAG);
		public static final TagKey<Fluid> OXYGEN = forgeTag(FluidOxygen.FORGE_TAG);
		public static final TagKey<Fluid> HYDROGEN = forgeTag(FluidHydrogen.FORGE_TAG);
		public static final TagKey<Fluid> HYDRAULIC_FLUID = forgeTag(FluidHydraulic.FORGE_TAG);

		private static void init() {

		}

		private static TagKey<Fluid> forgeTag(String name) {
			return FluidTags.create(new ResourceLocation("forge", name));
		}

	}
	
	public static class Gases {
		
		private static void init() {
			
		}
		
		private static TagKey<Gas> forgeTag(String name) {
			return create(new ResourceLocation("forge", name));
		}
		
		public static TagKey<Gas> create(ResourceLocation loc) {
			return TagKey.create(ElectrodynamicsRegistries.GAS_REGISTRY_KEY, loc);
		}
	}

}
