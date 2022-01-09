package electrodynamics.prefab.tile.components.type;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import electrodynamics.api.item.ItemUtils;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.network.FluidUtilities;
import electrodynamics.common.recipe.ElectrodynamicsRecipe;
import electrodynamics.common.recipe.categories.fluid2fluid.Fluid2FluidRecipe;
import electrodynamics.common.recipe.categories.fluid2item.Fluid2ItemRecipe;
import electrodynamics.common.recipe.categories.fluiditem2fluid.FluidItem2FluidRecipe;
import electrodynamics.common.recipe.categories.fluiditem2item.FluidItem2ItemRecipe;
import electrodynamics.common.recipe.categories.item2fluid.Item2FluidRecipe;
import electrodynamics.common.recipe.categories.item2item.Item2ItemRecipe;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.Component;
import electrodynamics.prefab.tile.components.ComponentType;
import electrodynamics.prefab.tile.components.utils.AbstractFluidHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class ComponentProcessor implements Component {
	private GenericTile holder;

	@Override
	public void holder(GenericTile holder) {
		this.holder = holder;
	}

	public double operatingSpeed;
	public double operatingTicks;
	public double usage;
	public long requiredTicks;
	private Predicate<ComponentProcessor> canProcess = component -> false;
	private Consumer<ComponentProcessor> process;
	private Consumer<ComponentProcessor> failed;
	private int processorNumber = 0;

	private ElectrodynamicsRecipe recipe;

	public ComponentProcessor(GenericTile source) {
		holder(source);
		if (!holder.hasComponent(ComponentType.Inventory)) {
			throw new UnsupportedOperationException("You need to implement an inventory component to use the processor component!");
		}
		if (holder.hasComponent(ComponentType.Tickable)) {
			holder.<ComponentTickable>getComponent(ComponentType.Tickable).tickServer(this::tickServer);
		} else {
			throw new UnsupportedOperationException("You need to implement a tickable component to use the processor component!");
		}
		if (holder.hasComponent(ComponentType.PacketHandler)) {
			ComponentPacketHandler handler = holder.getComponent(ComponentType.PacketHandler);
			handler.guiPacketWriter(this::writeGuiPacket);
			handler.guiPacketReader(this::readGuiPacket);
		}
	}

	private void tickServer(ComponentTickable tickable) {
		operatingSpeed = 1;
		if (holder.hasComponent(ComponentType.PacketHandler) && holder.<ComponentTickable>getComponent(ComponentType.Tickable).getTicks() % 20 == 0) {
			holder.<ComponentPacketHandler>getComponent(ComponentType.PacketHandler).sendGuiPacketToTracking();
		}
		ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
		for (ItemStack stack : inv.getUpgradeContents()) {
			if (!stack.isEmpty() && stack.getItem() instanceof ItemUpgrade upgrade) {
				for (int i = 0; i < stack.getCount(); i++) {
					upgrade.subtype.applyUpgrade.accept(holder, this, stack);
				}
			}
		}
		if (holder.hasComponent(ComponentType.Electrodynamic)) {
			ComponentElectrodynamic electro = holder.getComponent(ComponentType.Electrodynamic);
			electro.maxJoules(usage * operatingSpeed * 10);
		}

		if (canProcess.test(this)) {
			operatingTicks += operatingSpeed;
			if (operatingTicks >= requiredTicks) {
				if (process != null) {
					process.accept(this);
				}
				operatingTicks = 0;
			}
			if (holder.hasComponent(ComponentType.Electrodynamic)) {
				ComponentElectrodynamic electro = holder.getComponent(ComponentType.Electrodynamic);
				electro.joules(electro.getJoulesStored() - usage * operatingSpeed);
			}
		} else if (operatingTicks > 0) {
			operatingTicks = 0;
			if (failed != null) {
				failed.accept(this);
			}
		}

	}

	private void writeGuiPacket(CompoundTag nbt) {
		nbt.putDouble("operatingTicks" + processorNumber, operatingTicks);
		nbt.putDouble("joulesPerTick" + processorNumber, usage * operatingSpeed);
		nbt.putLong("requiredTicks" + processorNumber, requiredTicks);
	}

	private void readGuiPacket(CompoundTag nbt) {
		operatingTicks = nbt.getDouble("operatingTicks" + processorNumber);
		usage = nbt.getDouble("joulesPerTick" + processorNumber);
		requiredTicks = nbt.getLong("requiredTicks" + processorNumber);
	}

	public ComponentProcessor process(Consumer<ComponentProcessor> process) {
		this.process = process;
		return this;
	}

	public ComponentProcessor failed(Consumer<ComponentProcessor> failed) {
		this.failed = failed;
		return this;
	}

	public ComponentProcessor canProcess(Predicate<ComponentProcessor> canProcess) {
		this.canProcess = canProcess;
		return this;
	}

	public ComponentProcessor usage(double usage) {
		this.usage = usage;
		return this;
	}

	public double getUsage() {
		return usage;
	}

	public ComponentProcessor requiredTicks(long requiredTicks) {
		this.requiredTicks = requiredTicks;
		return this;
	}

	public ComponentProcessor setProcessorNumber(int number) {
		processorNumber = number;
		return this;
	}

	public int getProcessorNumber() {
		return processorNumber;
	}

	@Override
	public ComponentType getType() {
		return ComponentType.Processor;
	}

	public ComponentProcessor consumeBucket() {
		FluidUtilities.drainItem(holder);
		return this;
	}

	public ComponentProcessor dispenseBucket() {
		FluidUtilities.fillItem(holder);
		return this;
	}

	public ComponentProcessor outputToPipe() {
		AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.FluidHandler);
		FluidUtilities.outputToPipe(holder, handler.getOutputTanks(), AbstractFluidHandler.getDirectionalArray(handler.relativeOutputDirections));
		return this;
	}

	public GenericTile getHolder() {
		return holder;
	}

	public ElectrodynamicsRecipe getRecipe() {
		return recipe;
	}

	public void setRecipe(ElectrodynamicsRecipe recipe) {
		this.recipe = recipe;
	}

	// Instead of checking all at once, we check one at a time; more efficient
	public <T extends Item2ItemRecipe> boolean canProcessItem2ItemRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
		ComponentElectrodynamic electro = holder.getComponent(ComponentType.Electrodynamic);
		if (electro.getJoulesStored() < pr.getUsage()) {
			return false;
		}
		Item2ItemRecipe locRecipe;
		if (!checkExistingRecipe(pr)) {
			locRecipe = (T) ElectrodynamicsRecipe.getRecipe(pr, typeIn);
			if (locRecipe == null) {
				return false;
			}
		} else {
			locRecipe = (T) recipe;
		}

		setRecipe(locRecipe);

		ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
		ItemStack output = inv.getOutputContents().get(processorNumber);
		ItemStack result = recipe.getResultItem();
		boolean isEmpty = output.isEmpty();
		if (!isEmpty && !ItemUtils.testItems(output.getItem(), result.getItem())) {
			return false;
		}

		int locCap = isEmpty ? 64 : output.getMaxStackSize();
		if (locCap < output.getCount() + result.getCount()) {
			return false;
		}

		if (locRecipe.hasItemBiproducts()) {
			boolean itemBiRoom = roomInItemBiSlots(inv.getItemBiContents(), locRecipe.getFullItemBiStacks());
			if (!itemBiRoom) {
				return false;
			}
		}
		if (locRecipe.hasFluidBiproducts()) {
			AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.FluidHandler);
			boolean fluidBiRoom = roomInBiproductTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
			if (!fluidBiRoom) {
				return false;
			}
		}
		return true;
	}

	public <T extends Fluid2ItemRecipe> boolean canProcessFluid2ItemRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
		ComponentElectrodynamic electro = holder.getComponent(ComponentType.Electrodynamic);
		if (electro.getJoulesStored() < pr.getUsage()) {
			return false;
		}
		Fluid2ItemRecipe locRecipe;
		if (!checkExistingRecipe(pr)) {
			locRecipe = (T) ElectrodynamicsRecipe.getRecipe(pr, typeIn);
			if (locRecipe == null) {
				return false;
			}
		} else {
			locRecipe = (T) recipe;
		}
		setRecipe(locRecipe);
		ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
		ItemStack output = inv.getOutputContents().get(processorNumber);
		ItemStack result = recipe.getResultItem();
		boolean isEmpty = output.isEmpty();

		if (!isEmpty && !ItemUtils.testItems(output.getItem(), result.getItem())) {
			return false;
		}

		int locCap = isEmpty ? 64 : output.getMaxStackSize();
		if (locCap < output.getCount() + result.getCount()) {
			return false;
		}
		if (locRecipe.hasItemBiproducts()) {
			boolean itemBiRoom = roomInItemBiSlots(inv.getItemBiContents(), locRecipe.getFullItemBiStacks());
			if (!itemBiRoom) {
				return false;
			}
		}
		if (locRecipe.hasFluidBiproducts()) {
			AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.FluidHandler);
			boolean fluidBiRoom = roomInBiproductTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
			if (!fluidBiRoom) {
				return false;
			}
		}
		return true;
	}

	public <T extends Fluid2FluidRecipe> boolean canProcessFluid2FluidRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
		ComponentElectrodynamic electro = holder.getComponent(ComponentType.Electrodynamic);
		if (electro.getJoulesStored() < pr.getUsage()) {
			return false;
		}
		Fluid2FluidRecipe locRecipe;
		if (!checkExistingRecipe(pr)) {
			locRecipe = (T) ElectrodynamicsRecipe.getRecipe(pr, typeIn);
			if (locRecipe == null) {
				return false;
			}
		} else {
			locRecipe = (T) recipe;
		}
		setRecipe(locRecipe);
		AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.FluidHandler);
		int amtAccepted = handler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), FluidAction.SIMULATE);
		if (amtAccepted < locRecipe.getFluidRecipeOutput().getAmount()) {
			return false;
		}
		if (locRecipe.hasItemBiproducts()) {
			ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
			boolean itemBiRoom = roomInItemBiSlots(inv.getItemBiContents(), locRecipe.getFullItemBiStacks());
			if (!itemBiRoom) {
				return false;
			}
		}
		if (locRecipe.hasFluidBiproducts()) {
			boolean fluidBiRoom = roomInBiproductTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
			if (!fluidBiRoom) {
				return false;
			}
		}
		return true;
	}

	public <T extends Item2FluidRecipe> boolean canProcessItem2FluidRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
		ComponentElectrodynamic electro = holder.getComponent(ComponentType.Electrodynamic);
		if (electro.getJoulesStored() < pr.getUsage()) {
			return false;
		}
		Item2FluidRecipe locRecipe;
		if (!checkExistingRecipe(pr)) {
			locRecipe = (T) ElectrodynamicsRecipe.getRecipe(pr, typeIn);
			if (locRecipe == null) {
				return false;
			}
		} else {
			locRecipe = (T) recipe;
		}
		setRecipe(locRecipe);
		AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.FluidHandler);
		int amtAccepted = handler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), FluidAction.SIMULATE);
		if (amtAccepted < locRecipe.getFluidRecipeOutput().getAmount()) {
			return false;
		}
		if (locRecipe.hasItemBiproducts()) {
			ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
			boolean itemBiRoom = roomInItemBiSlots(inv.getItemBiContents(), locRecipe.getFullItemBiStacks());
			if (!itemBiRoom) {
				return false;
			}
		}
		if (locRecipe.hasFluidBiproducts()) {
			boolean fluidBiRoom = roomInBiproductTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
			if (!fluidBiRoom) {
				return false;
			}
		}
		return true;
	}

	public <T extends FluidItem2FluidRecipe> boolean canProcessFluidItem2FluidRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
		ComponentElectrodynamic electro = holder.getComponent(ComponentType.Electrodynamic);
		if (electro.getJoulesStored() < pr.getUsage()) {
			return false;
		}
		FluidItem2FluidRecipe locRecipe;
		if (!checkExistingRecipe(pr)) {
			locRecipe = (T) ElectrodynamicsRecipe.getRecipe(pr, typeIn);
			if (locRecipe == null) {
				return false;
			}
		} else {
			locRecipe = (T) recipe;
		}
		setRecipe(locRecipe);
		AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.FluidHandler);
		int amtAccepted = handler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), FluidAction.SIMULATE);
		if (amtAccepted < locRecipe.getFluidRecipeOutput().getAmount()) {
			return false;
		}
		if (locRecipe.hasItemBiproducts()) {
			ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
			boolean itemBiRoom = roomInItemBiSlots(inv.getItemBiContents(), locRecipe.getFullItemBiStacks());
			if (!itemBiRoom) {
				return false;
			}
		}
		if (locRecipe.hasFluidBiproducts()) {
			boolean fluidBiRoom = roomInBiproductTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
			if (!fluidBiRoom) {
				return false;
			}
		}
		return true;
	}

	public <T extends FluidItem2ItemRecipe> boolean canProcessFluidItem2ItemRecipe(ComponentProcessor pr, RecipeType<?> typeIn) {
		ComponentElectrodynamic electro = holder.getComponent(ComponentType.Electrodynamic);
		if (electro.getJoulesStored() < pr.getUsage()) {
			return false;
		}
		FluidItem2ItemRecipe locRecipe = (T) ElectrodynamicsRecipe.getRecipe(pr, typeIn);
		if (locRecipe == null) {
			return false;
		}
		setRecipe(locRecipe);
		ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
		ItemStack output = inv.getOutputContents().get(processorNumber);
		ItemStack result = recipe.getResultItem();
		boolean isEmpty = output.isEmpty();

		if (!isEmpty && !ItemUtils.testItems(output.getItem(), result.getItem())) {
			return false;
		}

		int locCap = isEmpty ? 64 : output.getMaxStackSize();
		if (locCap < output.getCount() + result.getCount()) {
			return false;
		}
		if (locRecipe.hasItemBiproducts()) {
			boolean itemBiRoom = roomInItemBiSlots(inv.getItemBiContents(), locRecipe.getFullItemBiStacks());
			if (!itemBiRoom) {
				return false;
			}
		}
		if (locRecipe.hasFluidBiproducts()) {
			AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.FluidHandler);
			boolean fluidBiRoom = roomInBiproductTanks(handler.getOutputTanks(), locRecipe.getFullFluidBiStacks());
			if (!fluidBiRoom) {
				return false;
			}
		}
		return true;
	}

	/*
	 * CONVENTIONS TO NOTE:
	 * 
	 * Biproducts will be output in the order they appear in the recipe JSON
	 * 
	 * The output FluidTanks will contain both the recipe output tank and the biproduct tanks The first tank is ALWAYS the main output tank, and the following tanks will be filled in the order of the fluid biproducts
	 * 
	 * 
	 * 
	 * 
	 * Also, no checks outside of the null recipe check will be performed in these methods All validity checks will take place in the recipe validator methods
	 * 
	 */

	public <T extends Item2ItemRecipe> void processItem2ItemRecipe(ComponentProcessor pr) {
		if (getRecipe() != null) {
			ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
			T locRecipe = (T) getRecipe();
			List<Integer> slotOrientation = locRecipe.getItemArrangment(pr.getProcessorNumber());

			if (locRecipe.hasItemBiproducts()) {
				ProbableItem[] itemBi = locRecipe.getItemBiproducts();
				List<ItemStack> invBi = inv.getItemBiContents();

				for (int i = 0; i < locRecipe.getItemBiproductCount(); i++) {
					if (invBi.get(i).isEmpty()) {
						inv.setItem(inv.getItemBiproductStartIndex() + i, itemBi[i].roll().copy());
					} else {
						inv.getItem(inv.getItemBiproductStartIndex() + i).grow(itemBi[i].roll().getCount());
					}
				}
			}

			if (locRecipe.hasFluidBiproducts()) {
				AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.Inventory);
				ProbableFluid[] fluidBi = locRecipe.getFluidBiproducts();
				FluidTank[] outTanks = handler.getOutputTanks();
				for (int i = 0; i < fluidBi.length; i++) {
					outTanks[i].fill(fluidBi[i].roll(), FluidAction.EXECUTE);
				}
			}
			if (inv.getOutputContents().get(getProcessorNumber()).isEmpty()) {
				inv.setItem(inv.getOutputSlots().get(getProcessorNumber()), locRecipe.getResultItem().copy());
			} else {
				inv.getOutputContents().get(getProcessorNumber()).grow(locRecipe.getResultItem().getCount());
			}
			List<List<ItemStack>> inputs = inv.getInputContents();
			for (int i = 0; i < inputs.get(getProcessorNumber()).size(); i++) {
				inputs.get(getProcessorNumber()).get(slotOrientation.get(i)).shrink(locRecipe.getCountedIngredients().get(i).getStackSize());
			}
		}
	}

	public <T extends FluidItem2FluidRecipe> void processFluidItem2FluidRecipe(ComponentProcessor pr) {
		if (getRecipe() != null) {
			T locRecipe = (T) getRecipe();
			ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
			AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.FluidHandler);
			List<Integer> slotOrientation = locRecipe.getItemArrangment(pr.getProcessorNumber());

			if (locRecipe.hasItemBiproducts()) {
				ProbableItem[] itemBi = locRecipe.getItemBiproducts();
				List<ItemStack> invBi = inv.getItemBiContents();

				for (int i = 0; i < locRecipe.getItemBiproductCount(); i++) {
					if (invBi.get(i).isEmpty()) {
						inv.setItem(inv.getItemBiproductStartIndex() + i, itemBi[i].roll().copy());
					} else {
						inv.getItem(inv.getItemBiproductStartIndex() + i).grow(itemBi[i].roll().getCount());
					}
				}
			}

			if (locRecipe.hasFluidBiproducts()) {
				ProbableFluid[] fluidBi = locRecipe.getFluidBiproducts();
				FluidTank[] outTanks = handler.getOutputTanks();
				for (int i = 0; i < fluidBi.length; i++) {
					outTanks[i + 1].fill(fluidBi[i].roll(), FluidAction.EXECUTE);
				}
			}

			handler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), FluidAction.EXECUTE);

			List<List<ItemStack>> inputs = inv.getInputContents();
			for (int i = 0; i < inputs.get(getProcessorNumber()).size(); i++) {
				inputs.get(getProcessorNumber()).get(slotOrientation.get(i)).shrink(locRecipe.getCountedIngredients().get(i).getStackSize());
			}

			FluidTank[] tanks = handler.getInputTanks();
			List<FluidIngredient> fluidIngs = locRecipe.getFluidIngredients();
			List<Integer> tankOrientation = locRecipe.getFluidArrangement();
			for (int i = 0; i < handler.getInputTankCount(); i++) {
				tanks[tankOrientation.get(i)].drain(fluidIngs.get(i).getFluidStack().getAmount(), FluidAction.EXECUTE);
			}
		}
	}

	public <T extends FluidItem2ItemRecipe> void processFluidItem2ItemRecipe(ComponentProcessor pr) {
		if (getRecipe() != null) {
			T locRecipe = (T) getRecipe();

			ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
			AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.FluidHandler);
			List<Integer> slotOrientation = locRecipe.getItemArrangment(pr.getProcessorNumber());

			if (locRecipe.hasItemBiproducts()) {
				ProbableItem[] itemBi = locRecipe.getItemBiproducts();
				List<ItemStack> invBi = inv.getItemBiContents();

				for (int i = 0; i < locRecipe.getItemBiproductCount(); i++) {
					if (invBi.get(i).isEmpty()) {
						inv.setItem(inv.getItemBiproductStartIndex() + i, itemBi[i].roll().copy());
					} else {
						inv.getItem(inv.getItemBiproductStartIndex() + i).grow(itemBi[i].roll().getCount());
					}
				}
			}

			if (locRecipe.hasFluidBiproducts()) {
				ProbableFluid[] fluidBi = locRecipe.getFluidBiproducts();
				FluidTank[] outTanks = handler.getOutputTanks();
				for (int i = 0; i < fluidBi.length; i++) {
					outTanks[i].fill(fluidBi[i].roll(), FluidAction.EXECUTE);
				}
			}
			if (inv.getOutputContents().get(getProcessorNumber()).isEmpty()) {
				inv.setItem(inv.getOutputSlots().get(getProcessorNumber()), locRecipe.getResultItem().copy());
			} else {
				inv.getOutputContents().get(getProcessorNumber()).grow(locRecipe.getResultItem().getCount());
			}

			List<List<ItemStack>> inputs = inv.getInputContents();
			for (int i = 0; i < inputs.get(getProcessorNumber()).size(); i++) {
				inputs.get(getProcessorNumber()).get(slotOrientation.get(i)).shrink(locRecipe.getCountedIngredients().get(i).getStackSize());
			}

			FluidTank[] tanks = handler.getInputTanks();
			List<FluidIngredient> fluidIngs = locRecipe.getFluidIngredients();
			List<Integer> tankOrientation = locRecipe.getFluidArrangement();
			for (int i = 0; i < handler.getInputTankCount(); i++) {
				tanks[tankOrientation.get(i)].drain(fluidIngs.get(i).getFluidStack().getAmount(), FluidAction.EXECUTE);
			}
		}
	}

	public <T extends Fluid2ItemRecipe> void processFluid2ItemRecipe() {
		if (getRecipe() != null) {
			T locRecipe = (T) getRecipe();

			ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
			AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.FluidHandler);

			if (locRecipe.hasItemBiproducts()) {
				ProbableItem[] itemBi = locRecipe.getItemBiproducts();
				List<ItemStack> invBi = inv.getItemBiContents();

				for (int i = 0; i < locRecipe.getItemBiproductCount(); i++) {
					if (invBi.get(i).isEmpty()) {
						inv.setItem(inv.getItemBiproductStartIndex() + i, itemBi[i].roll().copy());
					} else {
						inv.getItem(inv.getItemBiproductStartIndex() + i).grow(itemBi[i].roll().getCount());
					}
				}
			}

			if (locRecipe.hasFluidBiproducts()) {
				ProbableFluid[] fluidBi = locRecipe.getFluidBiproducts();
				FluidTank[] outTanks = handler.getOutputTanks();
				for (int i = 0; i < fluidBi.length; i++) {
					outTanks[i].fill(fluidBi[i].roll(), FluidAction.EXECUTE);
				}
			}
			if (inv.getOutputContents().get(getProcessorNumber()).isEmpty()) {
				inv.setItem(inv.getOutputSlots().get(getProcessorNumber()), locRecipe.getResultItem().copy());
			} else {
				inv.getOutputContents().get(getProcessorNumber()).grow(locRecipe.getResultItem().getCount());
			}

			FluidTank[] tanks = handler.getInputTanks();
			List<FluidIngredient> fluidIngs = locRecipe.getFluidIngredients();
			List<Integer> tankOrientation = locRecipe.getFluidArrangement();
			for (int i = 0; i < handler.getInputTankCount(); i++) {
				tanks[tankOrientation.get(i)].drain(fluidIngs.get(i).getFluidStack().getAmount(), FluidAction.EXECUTE);
			}
		}
	}

	public <T extends Fluid2FluidRecipe> void processFluid2FluidRecipe(ComponentProcessor pr) {
		if (getRecipe() != null) {
			T locRecipe = (T) getRecipe();
			ComponentInventory inv = holder.getComponent(ComponentType.Inventory);
			AbstractFluidHandler<?> handler = holder.getComponent(ComponentType.FluidHandler);

			if (locRecipe.hasItemBiproducts()) {
				ProbableItem[] itemBi = locRecipe.getItemBiproducts();
				List<ItemStack> invBi = inv.getItemBiContents();

				for (int i = 0; i < locRecipe.getItemBiproductCount(); i++) {
					if (invBi.get(i).isEmpty()) {
						inv.setItem(inv.getItemBiproductStartIndex() + i, itemBi[i].roll().copy());
					} else {
						inv.getItem(inv.getItemBiproductStartIndex() + i).grow(itemBi[i].roll().getCount());
					}
				}
			}

			if (locRecipe.hasFluidBiproducts()) {
				ProbableFluid[] fluidBi = locRecipe.getFluidBiproducts();
				FluidTank[] outTanks = handler.getOutputTanks();
				for (int i = 0; i < fluidBi.length; i++) {
					outTanks[i + 1].fill(fluidBi[i].roll(), FluidAction.EXECUTE);
				}
			}

			handler.getOutputTanks()[0].fill(locRecipe.getFluidRecipeOutput(), FluidAction.EXECUTE);

			FluidTank[] tanks = handler.getInputTanks();
			List<FluidIngredient> fluidIngs = locRecipe.getFluidIngredients();
			List<Integer> tankOrientation = locRecipe.getFluidArrangement();
			for (int i = 0; i < handler.getInputTankCount(); i++) {
				tanks[tankOrientation.get(i)].drain(fluidIngs.get(i).getFluidStack().getAmount(), FluidAction.EXECUTE);
			}
		}
	}

	private static boolean roomInItemBiSlots(List<ItemStack> slots, ItemStack[] biproducts) {
		for (int i = 0; i < slots.size(); i++) {
			ItemStack slotStack = slots.get(i);
			ItemStack biStack = biproducts[i];
			if (slotStack.isEmpty() || ItemUtils.testItems(slotStack.getItem(), biStack.getItem())) {
				if (slotStack.getCount() + biStack.getCount() > slotStack.getMaxStackSize()) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	private static boolean roomInBiproductTanks(FluidTank[] tanks, FluidStack[] stacks) {
		for (int i = 1; i < tanks.length; i++) {
			FluidTank tank = tanks[i];
			FluidStack stack = stacks[i - 1];
			int amtTaken = tank.fill(stack, FluidAction.SIMULATE);
			if (amtTaken < stack.getAmount()) {
				return false;
			}
		}
		return true;
	}

	private boolean checkExistingRecipe(ComponentProcessor pr) {
		if (recipe != null) {
			return recipe.matchesRecipe(pr);
		}
		return false;
	}

}
