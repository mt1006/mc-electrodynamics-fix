package electrodynamics.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import electrodynamics.common.tile.generators.TileCombustionChamber;
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
import org.jetbrains.annotations.NotNull;

public class RenderCombustionChamber extends AbstractTileRenderer<TileCombustionChamber> {

	public RenderCombustionChamber(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileCombustionChamber tileEntityIn, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStackIn.pushPose();
		FluidStack fuel = tileEntityIn.<ComponentFluidHandlerMulti>getComponent(ComponentType.FluidHandler).getFluidInTank(0, true);
		float prog = fuel.getAmount() / (float) TileCombustionChamber.TANK_CAPACITY;
		if (prog > 0) {

			Direction facing = tileEntityIn.getBlockState().getValue(GenericEntityBlock.FACING);
			AABB box;
			if (facing == Direction.EAST || facing == Direction.WEST) {
				box = new AABB(7.0D / 16.0D, 12.0D / 16.0D, 6.0D / 16.0D, 10.0D / 16.0D, 14.0D / 16.0D, 11.0D / 16.0D);
			} else {
				box = new AABB(6.0D / 16.0D, 12.0D / 16.0D, 7.0D / 16.0D, 11.0D / 16.0D, 14.0D / 16.0D, 10.0D / 16.0D);
			}

			VertexConsumer builder = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
			RenderingUtils.renderFluidBox(matrixStackIn, Minecraft.getInstance(), builder, box, fuel, combinedLightIn, combinedOverlayIn);
		}
		matrixStackIn.popPose();
	}

}
