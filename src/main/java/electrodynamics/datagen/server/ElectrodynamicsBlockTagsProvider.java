package electrodynamics.datagen.server;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeOreDeepslate;
import electrodynamics.common.block.subtype.SubtypePipe;
import electrodynamics.common.block.subtype.SubtypeRawOreBlock;
import electrodynamics.common.block.subtype.SubtypeResourceBlock;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ElectrodynamicsBlockTagsProvider extends BlockTagsProvider {

	public ElectrodynamicsBlockTagsProvider(DataGenerator pGenerator, ExistingFileHelper existingFileHelper) {
		super(pGenerator, References.ID, existingFileHelper);
	}

	@Override
	protected void addTags() {

		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeOre.values())).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeOreDeepslate.values())).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeRawOreBlock.values())).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeMachine.values()))
				.add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeWire.values())).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypePipe.values())).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeGlass.values())).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeResourceBlock.values())).add(ElectrodynamicsBlocks.blockSeismicMarker)
				.add(ElectrodynamicsBlocks.blockLogisticalManager);

		tag(BlockTags.MINEABLE_WITH_HOE).add(ElectrodynamicsBlocks.blockFrame).add(ElectrodynamicsBlocks.blockFrameCorner);

		tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeGlass.values())).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeOre.getOreForMiningLevel(0))).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeOreDeepslate.getOreForMiningLevel(0))).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeRawOreBlock.getForMiningLevel(0)))
				.add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeResourceBlock.getForMiningLevel(0))).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeWire.values())).add(ElectrodynamicsBlocks.blockFrame).add(ElectrodynamicsBlocks.blockFrameCorner);

		tag(BlockTags.NEEDS_STONE_TOOL).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeMachine.values())).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypePipe.values())).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeOre.getOreForMiningLevel(1))).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeOreDeepslate.getOreForMiningLevel(1)))
				.add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeRawOreBlock.getForMiningLevel(1))).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeResourceBlock.getForMiningLevel(1)));

		tag(BlockTags.NEEDS_IRON_TOOL).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeOre.getOreForMiningLevel(2))).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeOreDeepslate.getOreForMiningLevel(2))).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeRawOreBlock.getForMiningLevel(2))).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeResourceBlock.getForMiningLevel(2)));

		tag(BlockTags.NEEDS_DIAMOND_TOOL).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeOre.getOreForMiningLevel(3))).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeOreDeepslate.getOreForMiningLevel(3))).add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeRawOreBlock.getForMiningLevel(3)))
				.add(ElectrodynamicsBlocks.getAllBlockForSubtype(SubtypeResourceBlock.getForMiningLevel(3)));

		for (SubtypeOre ore : SubtypeOre.values()) {
			tag(ore.blockTag).add(ElectrodynamicsBlocks.getBlock(ore));
		}

		for (SubtypeOreDeepslate ore : SubtypeOreDeepslate.values()) {
			tag(ore.blockTag).add(ElectrodynamicsBlocks.getBlock(ore));
		}

		for (SubtypeResourceBlock storage : SubtypeResourceBlock.values()) {
			tag(storage.blockTag).add(ElectrodynamicsBlocks.getBlock(storage));
		}

		for (SubtypeRawOreBlock block : SubtypeRawOreBlock.values()) {
			tag(block.blockTag).add(ElectrodynamicsBlocks.getBlock(block));
		}

		TagAppender<Block> ores = tag(ElectrodynamicsTags.Blocks.ORES);

		for (SubtypeOre ore : SubtypeOre.values()) {
			ores.addTag(ore.blockTag);
		}

	}

}
