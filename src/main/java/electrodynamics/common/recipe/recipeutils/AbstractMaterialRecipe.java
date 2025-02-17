package electrodynamics.common.recipe.recipeutils;

import java.util.Collections;
import java.util.List;

import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.ElectrodynamicsRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public abstract class AbstractMaterialRecipe extends ElectrodynamicsRecipe {

	public AbstractMaterialRecipe(ResourceLocation recipeID, double experience, int ticks, double usagePerTick, ProbableItem[] itemBiproducts, ProbableFluid[] fluidBiproducts, ProbableGas[] gasBiproducts) {
		super(recipeID, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
	}

	@Override
	public ItemStack assemble(RecipeWrapper inv) {
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	public FluidStack getFluidRecipeOutput() {
		return FluidStack.EMPTY;
	}
	
	public GasStack getGasRecipeOutput() {
		return GasStack.EMPTY;
	}

	public List<FluidIngredient> getFluidIngredients() {
		return Collections.emptyList();
	}
	
	public List<GasIngredient> getGasIngredients() {
		return Collections.emptyList();
	}

}
