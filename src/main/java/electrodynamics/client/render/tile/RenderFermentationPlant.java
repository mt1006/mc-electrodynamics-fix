package electrodynamics.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import electrodynamics.common.tile.TileFermentationPlant;
import electrodynamics.prefab.block.GenericEntityBlock;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.utilities.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class RenderFermentationPlant extends AbstractTileRenderer<TileFermentationPlant> {

	public RenderFermentationPlant(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileFermentationPlant tileEntityIn, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.pushPose();
		ComponentFluidHandlerMulti multi = tileEntityIn.getComponent(ComponentType.FluidHandler);

		Direction facing = tileEntityIn.getBlockState().getValue(GenericEntityBlock.FACING);

		FluidStack input = null;
		for (FluidTank tank : multi.getInputTanks()) {
			if (!tank.isEmpty()) {
				input = tank.getFluid();
				break;
			}
		}
		if (input != null) {
			AABB box;
			if (facing == Direction.WEST) {
				box = new AABB(7.0D / 16.0D, 9.0D / 16.0D, 11.0D / 16.0D, 9.0D / 16.0D, 14.0D / 16.0D, 12.0D / 16.0D);
			} else if (facing == Direction.EAST) {
				box = new AABB(7.0D / 16.0D, 9.0D / 16.0D, 4.0D / 16.0D, 9.0D / 16.0D, 14.0D / 16.0D, 5.0D / 16.0D);
			} else if (facing == Direction.SOUTH) {
				box = new AABB(11.0D / 16.0D, 9.0D / 16.0D, 7.0D / 16.0D, 12.0D / 16.0D, 14.0D / 16.0D, 9.0D / 16.0D);
			} else {
				box = new AABB(4.0D / 16.0D, 9.0D / 16.0D, 7.0D / 16.0D, 5.0D / 16.0D, 14.0D / 16.0D, 9.0D / 16.0D);
			}
			VertexConsumer builder = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
			RenderingUtils.renderFluidBox(matrixStackIn, Minecraft.getInstance(), builder, box, input, combinedLightIn, combinedOverlayIn);
		}

		FluidStack output = null;
		for (FluidTank tank : multi.getOutputTanks()) {
			if (!tank.isEmpty()) {
				output = tank.getFluid();
				break;
			}
		}
		if (output != null) {
			AABB box;
			if (facing == Direction.WEST) {
				box = new AABB(6.0D / 16.0D, 5.0D / 16.0D, 2.0D / 16.0D, 10.0D / 16.0D, 7.0D / 16.0D, 7.0D / 16.0D);
			} else if (facing == Direction.EAST) {
				box = new AABB(6.0D / 16.0D, 5.0D / 16.0D, 9.0D / 16.0D, 10.0D / 16.0D, 7.0D / 16.0D, 14.0D / 16.0D);
			} else if (facing == Direction.SOUTH) {
				box = new AABB(2.0D / 16.0D, 5.0D / 16.0D, 6.0D / 16.0D, 7.0D / 16.0D, 7.0D / 16.0D, 10.0D / 16.0D);
			} else {
				box = new AABB(9.0D / 16.0D, 5.0D / 16.0D, 6.0D / 16.0D, 14.0D / 16.0D, 7.0D / 16.0D, 10.0D / 16.0D);
			}
			VertexConsumer builder = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
			RenderingUtils.renderFluidBox(matrixStackIn, Minecraft.getInstance(), builder, box, output, combinedLightIn, combinedOverlayIn);
		}
		matrixStackIn.popPose();

	}

}
