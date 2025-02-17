package electrodynamics.prefab.block;

import java.util.Arrays;
import java.util.List;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.IWrenchable;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public abstract class GenericEntityBlock extends BaseEntityBlock implements IWrenchable {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	protected GenericEntityBlock(Properties properties) {
		super(properties);
	}

	@Override
	public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level lvl, BlockState state, BlockEntityType<T> type) {
		return (l, pos, s, tile) -> {
			if (tile instanceof GenericTile generic && generic.getComponent(ComponentType.Tickable) instanceof ComponentTickable tickable) {
				tickable.performTick(l);
			}
		};
	}

	@Override
	public void onRotate(ItemStack stack, BlockPos pos, Player player) {
		if (player.level.getBlockState(pos).hasProperty(FACING)) {
			BlockState state = rotate(player.level.getBlockState(pos), Rotation.CLOCKWISE_90);
			if (player.level.getBlockEntity(pos) instanceof GenericTile tile) {
				if (tile.hasComponent(ComponentType.Direction)) {
					tile.<ComponentDirection>getComponent(ComponentType.Direction).setDirection(state.getValue(FACING));
				}
			}
			player.level.setBlockAndUpdate(pos, state);
			player.level.updateNeighborsAt(pos, this);
		}
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		if (state.hasProperty(FACING)) {
			return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
		}
		return super.rotate(state, rot);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		if (state.hasProperty(FACING)) {
			return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
		}
		return super.mirror(state, mirrorIn);
	}

	@Override
	public void onPickup(ItemStack stack, BlockPos pos, Player player) {
		Level world = player.level;
		world.destroyBlock(pos, true, player);
	}

	// TODO get this to work
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		BlockEntity tile = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (tile instanceof GenericTile machine && machine != null) {
			ItemStack stack = new ItemStack(this);
			ComponentInventory inv = machine.getComponent(ComponentType.Inventory);
			if (inv != null) {
				Containers.dropContents(machine.getLevel(), machine.getBlockPos(), inv.getItems());
				tile.getCapability(ElectrodynamicsCapabilities.ELECTRODYNAMIC).ifPresent(el -> {
					double joules = el.getJoulesStored();
					if (joules > 0) {
						stack.getOrCreateTag().putDouble("joules", joules);
					}
				});
			}
			return Arrays.asList(stack);

		}
		return super.getDrops(state, builder);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (newState.isAir()) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity != null && entity instanceof GenericTile generic) {
				generic.onBlockDestroyed();
			}
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(state, level, pos, neighbor);
		BlockEntity entity = level.getBlockEntity(pos);
		if (entity != null && entity instanceof GenericTile generic) {
			generic.onNeightborChanged(neighbor);
		}
	}

	@Override
	public void onPlace(BlockState newState, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(newState, level, pos, oldState, isMoving);
		BlockEntity entity = level.getBlockEntity(pos);
		if (entity != null && entity instanceof GenericTile generic) {
			generic.onPlace(oldState, isMoving);
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbor, boolean isMoving) {
		super.neighborChanged(state, level, pos, block, neighbor, isMoving);
		BlockEntity entity = level.getBlockEntity(pos);
		if (entity != null && entity instanceof GenericTile generic) {
			generic.onNeightborChanged(neighbor);
		}

	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState pState) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		BlockEntity entity = level.getBlockEntity(pos);
		if (entity != null && entity instanceof GenericTile generic) {
			return generic.getComparatorSignal();
		}
		return super.getAnalogOutputSignal(state, level, pos);
	}

}
