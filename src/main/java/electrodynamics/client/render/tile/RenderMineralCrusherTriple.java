package electrodynamics.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import electrodynamics.client.ClientRegister;
import electrodynamics.common.tile.TileMineralCrusherTriple;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenderMineralCrusherTriple extends AbstractTileRenderer<TileMineralCrusherTriple> {

	public RenderMineralCrusherTriple(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(@NotNull TileMineralCrusherTriple tileEntityIn, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.pushPose();
		RenderingUtils.prepareRotationalTileModel(tileEntityIn, matrixStackIn);
		matrixStackIn.translate(0, 1.0 / 16.0, 0);
		double ticks = (tileEntityIn.clientRunningTicks + (tileEntityIn.getProcessor(0).operatingTicks.get() > 0 ? partialTicks : 0)) % 20;
		double progress = ticks < 10.010392739868964 ? Math.sin(0.05 * Math.PI * ticks) : (Math.sin(0.29 * Math.PI * ticks) + 1) / 1.3;
		matrixStackIn.translate(0, progress / 8.0 - 1 / 8.0, 0);
		BakedModel ibakedmodel = getModel(ClientRegister.MODEL_MINERALCRUSHERTRIPLEHANDLE);
		RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		matrixStackIn.popPose();
		ComponentInventory inv = tileEntityIn.getComponent(ComponentType.Inventory);
		ItemStack stack = inv.getInputsForProcessor(1).get(0);
		if (!stack.isEmpty()) {
			Direction dir = tileEntityIn.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
			matrixStackIn.pushPose();
			double scale = stack.getItem() instanceof BlockItem ? 5.3 : 8.0;
			matrixStackIn.translate(0.5 + dir.getStepX() / scale, stack.getItem() instanceof BlockItem ? 0.48 : 0.39, 0.5 + dir.getStepZ() / scale);
			matrixStackIn.scale(0.35f, 0.35f, 0.35f);
			if (!(stack.getItem() instanceof BlockItem)) {
				matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));
			} else {
				matrixStackIn.scale(0.3f, 0.3f, 0.3f);
				matrixStackIn.translate(0, -0.5, 0);
			}
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, 0);
			matrixStackIn.popPose();
		}
		stack = inv.getInputsForProcessor(0).get(0);
		if (!stack.isEmpty()) {
			Direction dir = tileEntityIn.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
			matrixStackIn.pushPose();
			double scale = stack.getItem() instanceof BlockItem ? 5.3 : 8.0;
			matrixStackIn.translate(0.5 + dir.getStepX() / scale - (dir.getStepZ() != 0 ? 0.14 : 0), stack.getItem() instanceof BlockItem ? 0.48 : 0.39, 0.5 + dir.getStepZ() / scale - (dir.getStepX() != 0 ? 0.14 : 0));
			matrixStackIn.scale(0.35f, 0.35f, 0.35f);
			if (!(stack.getItem() instanceof BlockItem)) {
				matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));
			} else {
				matrixStackIn.scale(0.3f, 0.3f, 0.3f);
				matrixStackIn.translate(0, -0.5, 0);
			}
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, 0);
			matrixStackIn.popPose();
		}
		stack = inv.getInputsForProcessor(2).get(0);
		if (!stack.isEmpty()) {
			Direction dir = tileEntityIn.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
			matrixStackIn.pushPose();
			double scale = stack.getItem() instanceof BlockItem ? 5.3 : 8.0;
			matrixStackIn.translate(0.5 + dir.getStepX() / scale + (dir.getStepZ() != 0 ? 0.14 : 0), stack.getItem() instanceof BlockItem ? 0.48 : 0.39, 0.5 + dir.getStepZ() / scale + (dir.getStepX() != 0 ? 0.14 : 0));
			matrixStackIn.scale(0.35f, 0.35f, 0.35f);
			if (!(stack.getItem() instanceof BlockItem)) {
				matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(90));
			} else {
				matrixStackIn.scale(0.3f, 0.3f, 0.3f);
				matrixStackIn.translate(0, -0.5, 0);
			}
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, 0);
			matrixStackIn.popPose();
		}
	}
}
