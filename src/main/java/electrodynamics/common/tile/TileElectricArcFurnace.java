package electrodynamics.common.tile;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerElectricArcFurnace;
import electrodynamics.common.inventory.container.tile.ContainerElectricArcFurnaceDouble;
import electrodynamics.common.inventory.container.tile.ContainerElectricArcFurnaceTriple;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.type.*;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.NBTUtils;
import electrodynamics.registers.ElectrodynamicsBlockTypes;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TileElectricArcFurnace extends GenericTile implements ITickableSound {

	protected BlastingRecipe[] cachedRecipe = null;
	
	private List<BlastingRecipe> cachedRecipes = null;

	private boolean isSoundPlaying = false;

	public TileElectricArcFurnace(BlockPos worldPosition, BlockState blockState) {
		this(SubtypeMachine.electricarcfurnace, 0, worldPosition, blockState);
	}

	public TileElectricArcFurnace(SubtypeMachine machine, int extra, BlockPos worldPosition, BlockState blockState) {
		super(extra == 1 ? ElectrodynamicsBlockTypes.TILE_ELECTRICARCFURNACEDOUBLE.get() : extra == 2 ? ElectrodynamicsBlockTypes.TILE_ELECTRICARCFURNACETRIPLE.get() : ElectrodynamicsBlockTypes.TILE_ELECTRICARCFURNACE.get(), worldPosition, blockState);

		int processorCount = extra + 1;
		int inputsPerProc = 1;
		int outputPerProc = 1;

		addComponent(new ComponentDirection());
		addComponent(new ComponentPacketHandler());
		addComponent(new ComponentTickable().tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this).relativeInput(Direction.NORTH).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * Math.pow(2, extra)).maxJoules(Constants.ELECTRICARCFURNACE_USAGE_PER_TICK * 20 * (extra + 1)));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().processors(processorCount, inputsPerProc, outputPerProc, 0).upgrades(3)).validUpgrades(ContainerElectricArcFurnace.VALID_UPGRADES).valid(machineValidator()).implementMachineInputsAndOutputs());
		addComponent(new ComponentContainerProvider(machine).createMenu((id, player) -> (extra == 0 ? new ContainerElectricArcFurnace(id, player, getComponent(ComponentType.Inventory), getCoordsArray()) : extra == 1 ? new ContainerElectricArcFurnaceDouble(id, player, getComponent(ComponentType.Inventory), getCoordsArray()) : extra == 2 ? new ContainerElectricArcFurnaceTriple(id, player, getComponent(ComponentType.Inventory), getCoordsArray()) : null)));

		for (int i = 0; i <= extra; i++) {
			addProcessor(new ComponentProcessor(this, i, extra + 1).canProcess(this::canProcess).process(this::process).requiredTicks(Constants.ELECTRICARCFURNACE_REQUIRED_TICKS).usage(Constants.ELECTRICARCFURNACE_USAGE_PER_TICK));
		}
		cachedRecipe = new BlastingRecipe[extra + 1];
	}

	protected void process(ComponentProcessor component) {
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		ItemStack input = inv.getInputsForProcessor(component.getProcessorNumber()).get(0);
		ItemStack output = inv.getOutputsForProcessor(component.getProcessorNumber()).get(0);
		ItemStack result = cachedRecipe[component.getProcessorNumber()].getResultItem();
		int index = inv.getOutputSlots().get(component.getProcessorNumber());
		if (!output.isEmpty()) {
			output.setCount(output.getCount() + result.getCount());
			inv.setItem(index, output);
		} else {
			inv.setItem(index, result.copy());
		}
		input.shrink(1);
		inv.setItem(inv.getInputSlotsForProcessor(component.getProcessorNumber()).get(0), input.copy());
		for (ItemStack stack : inv.getUpgradeContents()) {
			if (!stack.isEmpty() && ((ItemUpgrade) stack.getItem()).subtype == SubtypeItemUpgrade.experience) {
				CompoundTag tag = stack.getOrCreateTag();
				tag.putDouble(NBTUtils.XP, tag.getDouble(NBTUtils.XP) + cachedRecipe[component.getProcessorNumber()].getExperience());
				break;
			}
		}
	}

	protected boolean canProcess(ComponentProcessor component) {
		boolean canProcess = checkConditions(component);

		if (BlockEntityUtils.isLit(this) ^ canProcess || isProcessorActive()) {
			BlockEntityUtils.updateLit(this, canProcess || isProcessorActive());
		}

		return canProcess;
	}

	private boolean checkConditions(ComponentProcessor component) {
		component.setShouldKeepProgress(true);
		ComponentInventory inv = getComponent(ComponentType.Inventory);
		ItemStack input = inv.getInputsForProcessor(component.getProcessorNumber()).get(0);
		if (input.isEmpty()) {
			return false;
		}
		
		if (cachedRecipes == null) {
			cachedRecipes = level.getRecipeManager().getAllRecipesFor(RecipeType.BLASTING);
		}
		
		if(cachedRecipe == null) {
			component.setShouldKeepProgress(false);
			return false;
		}

		if (cachedRecipe[component.getProcessorNumber()] == null) {
			cachedRecipe[component.getProcessorNumber()] = getMatchedRecipe(input);
			if (cachedRecipe[component.getProcessorNumber()] == null) {
				component.setShouldKeepProgress(false);
				return false;
			}
			component.operatingTicks.set(0.0);
		}

		if (!cachedRecipe[component.getProcessorNumber()].matches(new SimpleContainer(input), level)) {
			cachedRecipe[component.getProcessorNumber()] = null;
			component.setShouldKeepProgress(false);
			return false;
		}
		
		ComponentElectrodynamic electro = getComponent(ComponentType.Electrodynamic);
		if (electro.getJoulesStored() < component.getUsage() * component.operatingSpeed.get()) {
			return false;
		}
		electro.maxJoules(component.getUsage() * component.operatingSpeed.get() * 10 * component.totalProcessors);

		ItemStack output = inv.getOutputContents().get(component.getProcessorNumber());
		ItemStack result = cachedRecipe[component.getProcessorNumber()].getResultItem();
		return (output.isEmpty() || ItemStack.isSame(output, result)) && output.getCount() + result.getCount() <= output.getMaxStackSize();

	}

	protected void tickClient(ComponentTickable tickable) {
		if (!isProcessorActive()) {
			return;
		}
		if (level.random.nextDouble() < 0.15) {
			Direction direction = this.<ComponentDirection>getComponent(ComponentType.Direction).getDirection();
			double d4 = level.random.nextDouble();
			double d5 = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (direction.getStepX() == -1 ? 0 : 1) : d4;
			double d6 = level.random.nextDouble();
			double d7 = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (direction.getStepZ() == -1 ? 0 : 1) : d4;
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + d5, worldPosition.getY() + d6, worldPosition.getZ() + d7, 0.0D, 0.0D, 0.0D);
		}
		if (!isSoundPlaying) {
			isSoundPlaying = true;
			SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_HUM.get(), this, true);
		}
	}

	@Override
	public void setNotPlaying() {
		isSoundPlaying = false;
	}

	@Override
	public boolean shouldPlaySound() {
		return isProcessorActive();
	}

	private BlastingRecipe getMatchedRecipe(ItemStack stack) {
		for (BlastingRecipe recipe : cachedRecipes) {
			if (recipe.matches(new SimpleContainer(stack), level)) {
				return recipe;
			}
		}
		return null;
	}
	
	@Override
	public int getComparatorSignal() {
		return (int) (((double) getNumActiveProcessors() / (double) Math.max(1, getNumProcessors())) * 15.0);
	}

}
