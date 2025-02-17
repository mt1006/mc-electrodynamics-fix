package electrodynamics.client.guidebook.chapters;

import electrodynamics.api.References;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.TextWrapperObject;
import electrodynamics.prefab.utilities.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class ChapterTips extends Chapter {

	private static final ImageWrapperObject LOGO = new ImageWrapperObject(0, 0, 0, 0, 32, 32, 32, 32, new ResourceLocation(References.ID, "textures/item/nugget/nuggetsilver.png"));

	public ChapterTips(Module module) {
		super(module);
	}

	@Override
	public ImageWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return TextUtils.guidebook("chapter.tips");
	}

	@Override
	public void addData() {

		// Energy storage tip
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.tips.tip", 1).withStyle(ChatFormatting.UNDERLINE)).setSeparateStart());
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.tips.tip1")).setIndentions(1).setSeparateStart());

		// Transformer Tip
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.tips.tip", 2).withStyle(ChatFormatting.UNDERLINE)).setNewPage());
		pageData.add(new TextWrapperObject(TextUtils.guidebook("chapter.tips.tip2")).setIndentions(1).setSeparateStart());

	}

}
