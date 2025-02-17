package electrodynamics.common.tile.quarry;

import electrodynamics.registers.ElectrodynamicsBlockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileFrame extends BlockEntity {

	public BlockPos ownerQuarryPos = null;

	private static final String KEY = "quarrypos";

	public TileFrame(BlockPos pos, BlockState state) {
		super(ElectrodynamicsBlockTypes.TILE_QUARRY_FRAME.get(), pos, state);
	}

	public void purposefullyDestroyed() {
		if (ownerQuarryPos == null) {
			return;
		}
		BlockEntity entity = level.getBlockEntity(ownerQuarryPos);
		if (entity != null && entity instanceof TileQuarry quarry) {
			quarry.addBrokenFrame(getBlockPos(), getBlockState());
		}
	}

	public void setQuarryPos(BlockPos pos) {
		ownerQuarryPos = pos;
		setChanged();
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (ownerQuarryPos != null) {
			tag.put(KEY, NbtUtils.writeBlockPos(ownerQuarryPos));
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains(KEY)) {
			ownerQuarryPos = NbtUtils.readBlockPos(tag.getCompound(KEY));
		}
	}

}
