package electrodynamics.datagen.client;

import javax.annotation.Nullable;

import electrodynamics.api.References;
import electrodynamics.client.ClientRegister;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypePipe;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.block.subtype.SubtypeWire.WireType;
import electrodynamics.common.item.subtype.SubtypeCeramic;
import electrodynamics.common.item.subtype.SubtypeCircuit;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.item.subtype.SubtypeDrillHead;
import electrodynamics.common.item.subtype.SubtypeDust;
import electrodynamics.common.item.subtype.SubtypeGear;
import electrodynamics.common.item.subtype.SubtypeImpureDust;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.item.subtype.SubtypeNugget;
import electrodynamics.common.item.subtype.SubtypeOxide;
import electrodynamics.common.item.subtype.SubtypePlate;
import electrodynamics.common.item.subtype.SubtypeRawOre;
import electrodynamics.common.item.subtype.SubtypeRod;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ElectrodynamicsItemModelsProvider extends ItemModelProvider {

	public final String modID;

	public ElectrodynamicsItemModelsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper, String modID) {
		super(generator, modID, existingFileHelper);
		this.modID = modID;
	}

	public ElectrodynamicsItemModelsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		this(generator, existingFileHelper, References.ID);
	}

	@Override
	protected void registerModels() {

		layeredItem(ElectrodynamicsItems.COAL_COKE, Parent.GENERATED, itemLoc("coalcoke"));
		layeredItem(ElectrodynamicsItems.ITEM_CERAMICINSULATION, Parent.GENERATED, itemLoc("insulationceramic"));
		layeredBuilder(name(ElectrodynamicsItems.ITEM_COIL), Parent.GENERATED, itemLoc("coil")).transforms().transform(TransformType.GUI).scale(0.8F).end();
		layeredItem(ElectrodynamicsItems.ITEM_INSULATION, Parent.GENERATED, itemLoc("insulation"));
		layeredItem(ElectrodynamicsItems.ITEM_MOLYBDENUMFERTILIZER, Parent.GENERATED, itemLoc("molybdenumfertilizer"));
		layeredItem(ElectrodynamicsItems.ITEM_MOTOR, Parent.GENERATED, itemLoc("motor"));
		layeredItem(ElectrodynamicsItems.ITEM_RAWCOMPOSITEPLATING, Parent.GENERATED, itemLoc("compositeplatingraw"));
		layeredItem(ElectrodynamicsItems.ITEM_SHEETPLASTIC, Parent.GENERATED, itemLoc("sheetplastic"));
		layeredItem(ElectrodynamicsItems.SLAG, Parent.GENERATED, itemLoc("slag"));
		layeredBuilder(name(ElectrodynamicsItems.ITEM_SOLARPANELPLATE), Parent.GENERATED, itemLoc("solarpanelplate")).transforms().transform(TransformType.GUI).scale(0.8F).end();
		layeredItem(ElectrodynamicsItems.ITEM_TITANIUM_COIL, Parent.GENERATED, itemLoc("titaniumheatcoil"));

		layeredItem(ElectrodynamicsItems.ITEM_COMBATHELMET, Parent.GENERATED, itemLoc("armor/combathelmet"));
		layeredItem(ElectrodynamicsItems.ITEM_COMBATCHESTPLATE, Parent.GENERATED, itemLoc("armor/combatchestplate"));
		layeredItem(ElectrodynamicsItems.ITEM_COMBATLEGGINGS, Parent.GENERATED, itemLoc("armor/combatleggings"));
		layeredItem(ElectrodynamicsItems.ITEM_COMBATBOOTS, Parent.GENERATED, itemLoc("armor/combatboots"));
		layeredItem(ElectrodynamicsItems.ITEM_COMPOSITEHELMET, Parent.GENERATED, itemLoc("armor/compositehelmet"));
		layeredItem(ElectrodynamicsItems.ITEM_COMPOSITECHESTPLATE, Parent.GENERATED, itemLoc("armor/compositechestplate"));
		layeredItem(ElectrodynamicsItems.ITEM_COMPOSITELEGGINGS, Parent.GENERATED, itemLoc("armor/compositeleggings"));
		layeredItem(ElectrodynamicsItems.ITEM_COMPOSITEBOOTS, Parent.GENERATED, itemLoc("armor/compositeboots"));
		layeredItem(ElectrodynamicsItems.ITEM_COMPOSITEPLATING, Parent.GENERATED, itemLoc("compositeplating"));
		layeredItem(ElectrodynamicsItems.ITEM_NIGHTVISIONGOGGLES, Parent.GENERATED, itemLoc("armor/nightvisiongoggles"));
		layeredItem(ElectrodynamicsItems.ITEM_JETPACK, Parent.GENERATED, itemLoc("armor/jetpack"));
		layeredItem(ElectrodynamicsItems.ITEM_SERVOLEGGINGS, Parent.GENERATED, itemLoc("armor/servoleggings"));
		layeredItem(ElectrodynamicsItems.ITEM_HYDRAULICBOOTS, Parent.GENERATED, itemLoc("armor/hydraulicboots"));
		layeredItem(ElectrodynamicsItems.ITEM_RUBBERBOOTS, Parent.GENERATED, itemLoc("armor/rubberboots"));

		getBucketModel(ElectrodynamicsItems.ITEM_CANISTERREINFORCED, Parent.FORGE_DEFAULT).fluid(Fluids.WATER).end().texture("base", itemLoc("canisterreinforced/base")).texture("fluid", itemLoc("canisterreinforced/fluid"));
		layeredItem(ElectrodynamicsItems.GUIDEBOOK, Parent.GENERATED, itemLoc("guidebook"));
		layeredBuilder(name(ElectrodynamicsItems.ITEM_MULTIMETER), Parent.GENERATED, itemLoc("multimeter")).transforms().transform(TransformType.GUI).scale(0.9F).end();
		layeredBuilder(name(ElectrodynamicsItems.ITEM_SEISMICSCANNER), Parent.GENERATED, itemLoc("seismicscanner")).transforms().transform(TransformType.GUI).scale(0.75F).end();
		layeredItem(ElectrodynamicsItems.ITEM_WRENCH, Parent.GENERATED, itemLoc("wrench"));
		layeredItem(ElectrodynamicsItems.ITEM_BATTERY, Parent.GENERATED, itemLoc("battery"));
		layeredItem(ElectrodynamicsItems.ITEM_LITHIUMBATTERY, Parent.GENERATED, itemLoc("lithiumbattery"));
		layeredItem(ElectrodynamicsItems.ITEM_CARBYNEBATTERY, Parent.GENERATED, itemLoc("carbynebattery"));
		// TODO make this toggleable?
		layeredItem(ElectrodynamicsItems.ITEM_ELECTRICBATON, Parent.HANDHELD, itemLoc("tools/electricbaton"));
		toggleableItem(ElectrodynamicsItems.ITEM_ELECTRICCHAINSAW, "on", Parent.HANDHELD, Parent.HANDHELD, new ResourceLocation[] { itemLoc("tools/electricchainsaw") }, new ResourceLocation[] { itemLoc("tools/electricchainsawon") });
		toggleableItem(ElectrodynamicsItems.ITEM_ELECTRICDRILL, "on", Parent.HANDHELD, Parent.HANDHELD, new ResourceLocation[] { itemLoc("tools/electricdrill") }, new ResourceLocation[] { itemLoc("tools/electricdrillon") });
		layeredBuilder(name(ElectrodynamicsItems.ITEM_MECHANIZEDCROSSBOW), Parent.GENERATED, itemLoc("tools/mechanizedcrossbow")).transforms().transform(TransformType.THIRD_PERSON_RIGHT_HAND).rotation(-90, 0, -60).translation(2F, 0.1F, -3F).scale(0.9F).end().transform(TransformType.THIRD_PERSON_LEFT_HAND).rotation(-90, 0, 30).translation(2, 0.1F, -3).scale(0.9F).end().transform(TransformType.FIRST_PERSON_RIGHT_HAND).rotation(-90, 0, -55).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end().transform(TransformType.FIRST_PERSON_LEFT_HAND).rotation(-90, 0, 35).translation(1.13F, 3.2F, 1.13F).scale(0.68F).end();

		for (SubtypeCeramic ceramic : SubtypeCeramic.values()) {
			layeredItem(ElectrodynamicsItems.getItem(ceramic), Parent.GENERATED, itemLoc("ceramic/" + ceramic.tag()));
		}

		for (SubtypeCircuit circuit : SubtypeCircuit.values()) {
			layeredItem(ElectrodynamicsItems.getItem(circuit), Parent.GENERATED, itemLoc("circuit/" + circuit.tag()));
		}

		for (SubtypeCrystal crystal : SubtypeCrystal.values()) {
			layeredItem(ElectrodynamicsItems.getItem(crystal), Parent.GENERATED, itemLoc("crystal/" + crystal.tag()));
		}

		for (SubtypeDrillHead drill : SubtypeDrillHead.values()) {
			layeredItem(ElectrodynamicsItems.getItem(drill), Parent.GENERATED, itemLoc("drillhead/" + drill.tag()));
		}

		for (SubtypeDust dust : SubtypeDust.values()) {
			layeredItem(ElectrodynamicsItems.getItem(dust), Parent.GENERATED, itemLoc("dust/" + dust.tag()));
		}

		for (SubtypeGear gear : SubtypeGear.values()) {
			layeredItem(ElectrodynamicsItems.getItem(gear), Parent.GENERATED, itemLoc("gear/" + gear.tag()));
		}

		for (SubtypeImpureDust impure : SubtypeImpureDust.values()) {
			layeredItem(ElectrodynamicsItems.getItem(impure), Parent.GENERATED, itemLoc("impuredust/" + impure.tag()));
		}

		for (SubtypeIngot ingot : SubtypeIngot.values()) {
			layeredItem(ElectrodynamicsItems.getItem(ingot), Parent.GENERATED, itemLoc("ingot/" + ingot.tag()));
		}

		for (SubtypeItemUpgrade upgrade : SubtypeItemUpgrade.values()) {
			layeredBuilder(name(ElectrodynamicsItems.getItem(upgrade)), Parent.GENERATED, itemLoc("upgrade/" + upgrade.tag())).transforms().transform(TransformType.GUI).scale(0.8F).end();
		}

		for (SubtypeNugget nugget : SubtypeNugget.values()) {
			layeredItem(ElectrodynamicsItems.getItem(nugget), Parent.GENERATED, itemLoc("nugget/" + nugget.tag()));
		}

		for (SubtypeOxide oxide : SubtypeOxide.values()) {
			layeredItem(ElectrodynamicsItems.getItem(oxide), Parent.GENERATED, itemLoc("oxide/" + oxide.tag()));
		}

		for (SubtypePlate plate : SubtypePlate.values()) {
			layeredItem(ElectrodynamicsItems.getItem(plate), Parent.GENERATED, itemLoc("plate/" + plate.tag()));
		}

		for (SubtypeRawOre raw : SubtypeRawOre.values()) {
			layeredItem(ElectrodynamicsItems.getItem(raw), Parent.GENERATED, itemLoc("rawore/" + raw.tag()));
		}

		for (SubtypeRod rod : SubtypeRod.values()) {
			layeredItem(ElectrodynamicsItems.getItem(rod), Parent.GENERATED, itemLoc("rod/" + rod.tag()));
		}

		for (SubtypeWire wire : SubtypeWire.values()) {
			if (wire.wireType == WireType.UNINSULATED) {
				layeredBuilder(name(ElectrodynamicsItems.getItem(wire)), Parent.GENERATED, itemLoc("wire/" + wire.tag())).transforms().transform(TransformType.GUI).scale(0.7F).end();
			} else {
				layeredItem(ElectrodynamicsItems.getItem(wire), Parent.GENERATED, itemLoc("wire/" + wire.tag()));
			}
		}

		for (SubtypePipe pipe : SubtypePipe.values()) {
			layeredItem(ElectrodynamicsItems.getItem(pipe), Parent.GENERATED, itemLoc("pipe/" + pipe.tag()));
		}

		simpleBlockItem(ElectrodynamicsBlocks.getBlock(SubtypeMachine.advancedsolarpanel), existingBlock(blockLoc("advancedsolarpanelitem"))).transforms().transform(TransformType.THIRD_PERSON_RIGHT_HAND).rotation(35, 45, 0).translation(0, 2.5F, 0).scale(0.375F).end().transform(TransformType.THIRD_PERSON_LEFT_HAND).rotation(35, 45, 0).translation(0, 2.5F, 0).scale(0.375F).end().transform(TransformType.FIRST_PERSON_RIGHT_HAND).rotation(0, 45, 0).scale(0.4F).end().transform(TransformType.FIRST_PERSON_LEFT_HAND).rotation(0, 225, 0).scale(0.4F).end().transform(TransformType.GUI).rotation(30, 225, 0).translation(0, -3F, 0).scale(0.265F).end();
		simpleBlockItem(ElectrodynamicsBlocks.getBlock(SubtypeMachine.hydroelectricgenerator), existingBlock(blockLoc("hydroelectricgeneratoritem"))).transforms().transform(TransformType.GUI).rotation(30, 225, 0).translation(1.85F, 1.0F, 0).scale(0.55F).end();
		simpleBlockItem(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrusher), existingBlock(blockLoc("mineralcrusheritem")));
		simpleBlockItem(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrusherdouble), existingBlock(blockLoc("mineralcrusherdoubleitem")));
		simpleBlockItem(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralcrushertriple), existingBlock(blockLoc("mineralcrushertripleitem")));
		simpleBlockItem(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrinder), existingBlock(blockLoc("mineralgrinderitem")));
		simpleBlockItem(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrinderdouble), existingBlock(blockLoc("mineralgrinderdoubleitem")));
		simpleBlockItem(ElectrodynamicsBlocks.getBlock(SubtypeMachine.mineralgrindertriple), existingBlock(blockLoc("mineralgrindertripleitem")));
		simpleBlockItem(ElectrodynamicsBlocks.getBlock(SubtypeMachine.motorcomplex), existingBlock(blockLoc("motorcomplexitem")));
		simpleBlockItem(ElectrodynamicsBlocks.getBlock(SubtypeMachine.windmill), existingBlock(blockLoc("windmillitem")));

	}

	public void layeredItem(RegistryObject<Item> item, Parent parent, ResourceLocation... textures) {
		layeredItem(name(item), parent, textures);
	}

	public void layeredItem(Item item, Parent parent, ResourceLocation... textures) {
		layeredItem(name(item), parent, textures);
	}

	public void layeredItem(String name, Parent parent, ResourceLocation... textures) {
		layeredBuilder(name, parent, textures);
	}

	public void toggleableItem(RegistryObject<Item> item, String toggle, Parent parentOff, Parent parentOn, ResourceLocation[] offText, ResourceLocation[] onText) {
		toggleableItem(name(item), toggle, parentOff, parentOn, offText, onText);
	}

	public void toggleableItem(String name, String toggle, Parent parentOff, Parent parentOn, ResourceLocation[] offText, ResourceLocation[] onText) {
		ItemModelBuilder off = layeredBuilder(name, parentOff, offText);
		ItemModelBuilder on = layeredBuilder(name + toggle, parentOn, onText);
		off.override().predicate(ClientRegister.ON, 1.0F).model(on).end();
	}

	public ItemModelBuilder layeredBuilder(String name, Parent parent, ResourceLocation... textures) {
		if (textures == null || textures.length == 0) {
			throw new UnsupportedOperationException("You need to provide at least one texture");
		}
		ItemModelBuilder builder = withExistingParent(name, parent.loc());
		int counter = 0;
		for (ResourceLocation location : textures) {
			builder.texture("layer" + counter, location);
			counter++;
		}
		return builder;
	}

	public DynamicFluidContainerModelBuilder<ItemModelBuilder> getBucketModel(RegistryObject<Item> item, Parent parent) {
		return getBucketModel(name(item), parent);
	}

	public DynamicFluidContainerModelBuilder<ItemModelBuilder> getBucketModel(String name, Parent parent) {
		return withExistingParent(name, parent.loc).customLoader(DynamicFluidContainerModelBuilder::begin);
	}

	public ItemModelBuilder simpleBlockItem(Block block, ModelFile model) {
		return getBuilder(key(block).getPath()).parent(model);
	}

	public ResourceLocation key(Block block) {
		return ForgeRegistries.BLOCKS.getKey(block);
	}

	public ResourceLocation itemLoc(String texture) {
		return modLoc("item/" + texture);
	}

	public ResourceLocation blockLoc(String texture) {
		return modLoc("block/" + texture);
	}

	public String name(RegistryObject<Item> item) {
		return name(item.get());
	}

	public String name(Item item) {
		return ForgeRegistries.ITEMS.getKey(item).getPath();
	}

	public ExistingModelFile existingBlock(RegistryObject<Block> block) {
		return existingBlock(block.getId());
	}

	public ExistingModelFile existingBlock(Block block) {
		return existingBlock(ForgeRegistries.BLOCKS.getKey(block));
	}

	public ExistingModelFile existingBlock(ResourceLocation loc) {
		return getExistingFile(loc);
	}

	public enum Parent {

		GENERATED(),
		HANDHELD(),
		FORGE_DEFAULT("forge", "item/default");

		@Nullable
		private final ResourceLocation loc;

		Parent() {
			loc = null;
		}

		Parent(String id, String loc) {
			this.loc = new ResourceLocation(id, loc);
		}

		public ResourceLocation loc() {
			return loc == null ? new ResourceLocation(toString().toLowerCase()) : loc;
		}
	}

}
