package electrodynamics.common.tile;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.common.block.VoxelShapes;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerElectrolyticSeparator;
import electrodynamics.common.network.FluidUtilities;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentDirection;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.GenericFluidTile;
import electrodynamics.registers.ElectrodynamicsBlockTypes;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileElectrolyticSeparator extends GenericFluidTile implements ITickableSound {

	public static final int MAX_TANK_CAPACITY = 5000;
	public long clientTicks = 0;

	private static final Direction OXYGEN_DIRECTION = Direction.EAST;
	private static final Direction HYDROGEN_DIRECTION = Direction.WEST;

	private boolean isSoundPlaying = false;

	public TileElectrolyticSeparator(BlockPos worldPos, BlockState blockState) {
		super(ElectrodynamicsBlockTypes.TILE_ELECTROLYTICSEPARATOR.get(), worldPos, blockState);
		addComponent(new ComponentTickable().tickClient(this::tickClient).tickServer(this::tickServer));
		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentElectrodynamic(this).relativeInput(Direction.SOUTH).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 2));
		addComponent(new ComponentFluidHandlerMulti(this).setOutputDirections(OXYGEN_DIRECTION, HYDROGEN_DIRECTION).setInputDirections(Direction.NORTH).setTanks(1, 2, new int[] { MAX_TANK_CAPACITY }, new int[] { MAX_TANK_CAPACITY, MAX_TANK_CAPACITY }).setRecipeType(ElectrodynamicsRecipeInit.ELECTROLYTIC_SEPERATOR_TYPE.get()));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().bucketInputs(1).bucketOutputs(2).upgrades(3)).validUpgrades(ContainerElectrolyticSeparator.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentProcessor(this).canProcess(component -> component.consumeBucket().dispenseBucket().canProcessFluid2FluidRecipe(component, ElectrodynamicsRecipeInit.ELECTROLYTIC_SEPERATOR_TYPE.get())).process(component -> component.processFluid2FluidRecipe(component)));
		addComponent(new ComponentContainerProvider(SubtypeMachine.electrolyticseparator).createMenu((id, player) -> new ContainerElectrolyticSeparator(id, player, getComponent(ComponentType.Inventory), getCoordsArray())));
	}

	public void tickServer(ComponentTickable tickable) {
		// ensures only one fluid per output
		ComponentFluidHandlerMulti handler = getComponent(ComponentType.FluidHandler);
		FluidUtilities.outputToPipe(this, new FluidTank[] { handler.getOutputTanks()[0] }, OXYGEN_DIRECTION);
		FluidUtilities.outputToPipe(this, new FluidTank[] { handler.getOutputTanks()[1] }, HYDROGEN_DIRECTION);
	}

	protected void tickClient(ComponentTickable tickable) {
		if (!shouldPlaySound()) {
			return;
		}
		if (level.random.nextDouble() < 0.15) {
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + level.random.nextDouble(), worldPosition.getY() + level.random.nextDouble() * 0.4 + 0.5, worldPosition.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
		}

		if (!isSoundPlaying) {
			isSoundPlaying = true;
			SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_ELECTROLYTICSEPARATOR.get(), this, true);
		}
	}

	@Override
	public void setNotPlaying() {
		isSoundPlaying = false;
	}

	@Override
	public boolean shouldPlaySound() {
		return this.<ComponentProcessor>getComponent(ComponentType.Processor).isActive();
	}
	
	@Override
	public int getComparatorSignal() {
		return this.<ComponentProcessor>getComponent(ComponentType.Processor).isActive() ? 15 : 0;
	}

	static {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.join(shape, Shapes.box(0.0057870370370369795, 0, 0, 1.0, 0.1875, 1), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.318287037037037, 0.3125, 0.8125, 0.693287037037037, 0.6875, 0.9375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.318287037037037, 0.3125, 0.0625, 0.693287037037037, 0.6875, 0.1875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.443287037037037, 0.8125, 0.25, 0.568287037037037, 0.875, 0.375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.443287037037037, 0.8125, 0.625, 0.568287037037037, 0.875, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.818287037037037, 0.25, 0.3125, 0.943287037037037, 0.625, 0.6875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.0057870370370369795, 0.25, 0.25, 0.06828703703703698, 0.75, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.255787037037037, 0.1875, 0.9375, 0.755787037037037, 0.75, 1), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.255787037037037, 0.1875, 0, 0.755787037037037, 0.75, 0.0625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.943287037037037, 0.1875, 0.25, 1.0, 0.75, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.443287037037037, 0.125, 0.25, 0.568287037037037, 0.8125, 0.375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.443287037037037, 0.1875, 0.625, 0.568287037037037, 0.8125, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.255787037037037, 0.1875, 0.25, 0.693287037037037, 0.3125, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.693287037037037, 0.1875, 0.25, 0.755787037037037, 0.75, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.255787037037037, 0.1875, 0.75, 0.755787037037037, 0.75, 0.8125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.255787037037037, 0.1875, 0.1875, 0.755787037037037, 0.75, 0.25), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.06828703703703698, 0.1875, 0.0625, 0.255787037037037, 1, 0.9375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.474537037037037, 0.875, 0.6875, 0.537037037037037, 0.9375, 0.859375), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.474537037037037, 0.6875, 0.859375, 0.537037037037037, 0.9375, 0.921875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.474537037037037, 0.6875, 0.078125, 0.537037037037037, 0.9375, 0.140625), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.474537037037037, 0.875, 0.140625, 0.537037037037037, 0.9375, 0.3125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.849537037037037, 0.1875, 0.6875, 0.912037037037037, 0.5625, 0.75), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.849537037037037, 0.1875, 0.75, 0.912037037037037, 0.25, 0.875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.474537037037037, 0.1875, 0.8125, 0.849537037037037, 0.25, 0.875), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.849537037037037, 0.1875, 0.25, 0.912037037037037, 0.5625, 0.3125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.849537037037037, 0.1875, 0.125, 0.912037037037037, 0.25, 0.25), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.474537037037037, 0.1875, 0.125, 0.849537037037037, 0.25, 0.1875), BooleanOp.OR);
		VoxelShapes.registerShape(SubtypeMachine.electrolyticseparator, shape, Direction.EAST);
	}

}
