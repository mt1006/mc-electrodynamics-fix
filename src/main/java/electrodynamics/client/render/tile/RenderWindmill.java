package electrodynamics.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import electrodynamics.client.ClientRegister;
import electrodynamics.common.tile.TileWindmill;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;

public class RenderWindmill extends AbstractTileRenderer<TileWindmill> {

	public RenderWindmill(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileWindmill tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		BakedModel ibakedmodel = getModel(ClientRegister.MODEL_WINDMILLBLADES);
		RenderingUtils.prepareRotationalTileModel(tileEntityIn, matrixStackIn);
		matrixStackIn.translate(0, 22.0 / 16.0, 0);
		float partial = (float) (partialTicks * tileEntityIn.rotationSpeed * (tileEntityIn.directionFlag.get() ? 1 : -1));
		matrixStackIn.mulPose(new Quaternion((float) ((tileEntityIn.savedTickRotation + partial) * tileEntityIn.generating.get() * 1.5), 0, 0, true));
		RenderingUtils.renderModel(ibakedmodel, tileEntityIn, RenderType.solid(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}
}
