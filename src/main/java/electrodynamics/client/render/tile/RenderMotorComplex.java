package electrodynamics.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import electrodynamics.client.ClientRegister;
import electrodynamics.common.tile.TileMotorComplex;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public class RenderMotorComplex extends AbstractTileRenderer<TileMotorComplex> {

	public RenderMotorComplex(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileMotorComplex tile, float ticks, PoseStack stack, @NotNull MultiBufferSource source, int light, int overlay) {

		stack.pushPose();

		Direction facing = tile.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
		float clientTicks = tile.<ComponentTickable>getComponent(ComponentType.Tickable).getTicks();
		float progressDegrees = 0.0F;

		if (tile.isPowered.get()) {
			progressDegrees = 360.0f * (float) Math.sin(clientTicks / tile.speed.get());
		}

		BakedModel shaft = getModel(ClientRegister.MODEL_MOTORCOMPLEXROTOR);

		switch (facing) {
			case EAST -> {
				stack.translate(0.5, 0.5, 0.5);
				stack.mulPose(new Quaternion(new Vector3f(-1.0F, 0.0F, 0.0F), progressDegrees, true));
			}
			case WEST -> {
				stack.translate(0.9375, 0.5, 0.5);
				stack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), progressDegrees, true));
			}
			case SOUTH -> {
				stack.translate(0.5, 0.5, 0.0625);
				stack.mulPose(new Quaternion(new Vector3f(0, 1F, 0), 90, true));
				stack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), progressDegrees, true));
			}
			case NORTH -> {
				stack.translate(0.5, 0.5, 0.5);
				stack.mulPose(new Quaternion(new Vector3f(0, 1F, 0), 90, true));
				stack.mulPose(new Quaternion(new Vector3f(-1.0F, 0.0F, 0.0F), progressDegrees, true));
			}
			default -> {
			}
		}

		RenderingUtils.renderModel(shaft, tile, RenderType.solid(), stack, source, light, overlay);

		stack.popPose();
	}

}
