package electrodynamics.common.block;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.tile.quarry.TileFrame;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockFrame extends BaseEntityBlock {

	// The Hoe is the removal tool to help prevent accidentally breaking the frame
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	private static final VoxelShape FRAME = Block.box(0, 0, 0, 16, 4, 16);
	private static final VoxelShape FRAME_CORNER = Shapes.or(Block.box(4.0D, 0.0D, 4.0D, 12.0D, 12.0D, 12.0D), Block.box(0, 0, 0, 16, 4, 16));

	private final int type;

	public BlockFrame(int type) {
		super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops());
		registerDefaultState(stateDefinition.any().setValue(ElectrodynamicsBlockStates.QUARRY_FRAME_DECAY, Boolean.FALSE).setValue(BlockStateProperties.WATERLOGGED, false).setValue(FACING, Direction.NORTH));
		this.type = type;
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		if (type == 0) {
			return FRAME;
			// room for future expansion
		} else if (type == 1) {
			return FRAME_CORNER;
		}
		return super.getShape(pState, pLevel, pPos, pContext);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return super.getStateForPlacement(context).setValue(BlockStateProperties.WATERLOGGED, fluidstate.getType() == Fluids.WATER).setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootContext.Builder builder) {
		return Collections.emptyList();
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return pState.getValue(ElectrodynamicsBlockStates.QUARRY_FRAME_DECAY);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ElectrodynamicsBlockStates.QUARRY_FRAME_DECAY);
		builder.add(BlockStateProperties.WATERLOGGED);
		builder.add(FACING);
	}

	@Override
	public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
		return state;
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state;
	}

	@Override
	public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
		pLevel.setBlockAndUpdate(pPos, Blocks.AIR.defaultBlockState());
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(BlockStateProperties.WATERLOGGED) == Boolean.TRUE) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}
		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) == Boolean.TRUE ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileFrame(pos, state);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (newState.isAir() && !state.getValue(ElectrodynamicsBlockStates.QUARRY_FRAME_DECAY) && !level.isClientSide) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity != null && entity instanceof TileFrame frame) {
				frame.purposefullyDestroyed();
			}
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}

	public static void writeToNbt(CompoundTag tag, String key, BlockState state) {
		CompoundTag data = new CompoundTag();
		data.putString("facing", state.getValue(FACING).name());
		data.putBoolean("waterlogged", state.getValue(BlockStateProperties.WATERLOGGED));
		data.putBoolean("decay", state.getValue(ElectrodynamicsBlockStates.QUARRY_FRAME_DECAY));
		tag.put(key, data);
	}

	public static BlockState readFromNbt(CompoundTag tag) {
		BlockState state = ElectrodynamicsBlocks.blockFrame.defaultBlockState();
		state.setValue(FACING, Direction.byName(tag.getString("facing")));
		state.setValue(BlockStateProperties.WATERLOGGED, tag.getBoolean("waterlogged"));
		state.setValue(ElectrodynamicsBlockStates.QUARRY_FRAME_DECAY, tag.getBoolean("decay"));
		return state;
	}

}
