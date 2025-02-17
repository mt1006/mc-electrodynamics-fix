package electrodynamics.compatibility.jei.recipecategories.fluid2item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import electrodynamics.common.recipe.ElectrodynamicsRecipe;
import electrodynamics.common.recipe.categories.fluid2item.Fluid2ItemRecipe;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.compatibility.jei.recipecategories.ElectrodynamicsRecipeCategory;
import electrodynamics.compatibility.jei.utils.gui.backgroud.BackgroundWrapper;
import electrodynamics.prefab.utilities.CapabilityUtils;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class Fluid2ItemRecipeCategory<T extends Fluid2ItemRecipe> extends ElectrodynamicsRecipeCategory<T> {

	/*
	 * DOCUMENTATION NOTES:
	 * 
	 * > Items supercede bucket slots in position > All biproducts will be included with the outputSlots field > All fluid bucket output slots will be incled with the outputSlots field
	 */

	protected Fluid2ItemRecipeCategory(IGuiHelper guiHelper, String modID, String recipeGroup, ItemStack inputMachine, BackgroundWrapper bWrap, Class<T> recipeClass, int animTime) {

		super(guiHelper, modID, recipeGroup, inputMachine, bWrap, recipeClass, animTime);
	}

	@Override
	public List<List<FluidStack>> getFluidInputs(ElectrodynamicsRecipe electro) {
		Fluid2ItemRecipe recipe = (Fluid2ItemRecipe) electro;
		List<List<FluidStack>> ingredients = new ArrayList<>();
		for (FluidIngredient ing : recipe.getFluidIngredients()) {
			List<FluidStack> fluids = new ArrayList<>();
			for (FluidStack stack : ing.getMatchingFluids()) {
				if (!ForgeRegistries.FLUIDS.getKey(stack.getFluid()).toString().toLowerCase().contains("flow")) {
					fluids.add(stack);
				}
			}
			ingredients.add(fluids);
		}
		return ingredients;
	}

	@Override
	public List<List<ItemStack>> getItemInputs(ElectrodynamicsRecipe electro) {
		Fluid2ItemRecipe recipe = (Fluid2ItemRecipe) electro;
		List<FluidIngredient> ings = recipe.getFluidIngredients();
		List<List<ItemStack>> totalBuckets = new ArrayList<>();
		for (FluidIngredient ing : ings) {
			List<ItemStack> buckets = new ArrayList<>();
			for (FluidStack stack : ing.getMatchingFluids()) {
				ItemStack bucket = new ItemStack(stack.getFluid().getBucket(), 1);
				CapabilityUtils.fill(bucket, stack);
				buckets.add(bucket);
			}
			totalBuckets.add(buckets);
		}
		return totalBuckets;
	}

	@Override
	public List<ItemStack> getItemOutputs(ElectrodynamicsRecipe electro) {
		Fluid2ItemRecipe recipe = (Fluid2ItemRecipe) electro;
		List<ItemStack> outputItems = new ArrayList<>();

		outputItems.add(recipe.getResultItem());

		if (recipe.hasItemBiproducts()) {
			outputItems.addAll(Arrays.asList(recipe.getFullItemBiStacks()));
		}

		if (recipe.hasFluidBiproducts()) {
			for (ProbableFluid stack : recipe.getFluidBiproducts()) {
				ItemStack temp = new ItemStack(stack.getFullStack().getFluid().getBucket(), 1);
				CapabilityUtils.fill(temp, stack.getFullStack());
				outputItems.add(temp);
			}
		}
		return outputItems;
	}

}
