package electrodynamics.common.recipe.recipeutils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;

import electrodynamics.Electrodynamics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Extension of Ingredient that adds Fluid compatibility
 * 
 * @author skip999
 *
 */
public class FluidIngredient extends Ingredient {

	@Nonnull
	private List<FluidStack> fluidStacks;

	@Nullable
	public TagKey<Fluid> tag;
	private int amount;

	public FluidIngredient(FluidStack fluidStack) {
		super(Stream.empty());
		fluidStacks = new ArrayList<>();
		fluidStacks.add(fluidStack);
	}

	public FluidIngredient(List<FluidStack> fluidStack) {
		super(Stream.empty());
		fluidStacks = fluidStack;
	}

	/**
	 * Call this one if you're trying to get a Tag once loaded on the server or are trying to get a specific fluid
	 * 
	 * DO NOT call this one if trying to load a fluid from a JSON file!
	 * 
	 * @param resourceLocation
	 * @param amount
	 * @param isTag
	 */
	public FluidIngredient(ResourceLocation resourceLocation, int amount, boolean isTag) {
		super(Stream.empty());
		if (isTag) {
			// Don't know ifi can use FluidTags.create(resourceLocation) all the time but we shall see.
			List<Fluid> fluids = ForgeRegistries.FLUIDS.tags().getTag(FluidTags.create(resourceLocation)).stream().toList();
			fluidStacks = new ArrayList<>();
			for (Fluid fluid : fluids) {
				fluidStacks.add(new FluidStack(fluid, amount));
			}
		} else {
			List<FluidStack> fluids = new ArrayList<>();
			fluids.add(new FluidStack(ForgeRegistries.FLUIDS.getValue(resourceLocation), amount));
			fluidStacks = fluids;
		}
		if (fluidStacks.isEmpty()) {
			throw new UnsupportedOperationException("No fluids returned from tag " + resourceLocation);
		}
	}

	/**
	 * Constructor is designed to defer loading tag value until ingredient is referenced
	 * 
	 * @param resource
	 * @param amount
	 */
	public FluidIngredient(ResourceLocation resource, int amount) {
		this(FluidTags.create(resource), amount);
	}

	public FluidIngredient(TagKey<Fluid> tag, int amount) {
		super(Stream.empty());
		this.tag = tag;
		this.amount = amount;
		fluidStacks = new ArrayList<>();

	}

	public static FluidIngredient deserialize(JsonObject jsonObject) {
		Preconditions.checkArgument(jsonObject != null, "FluidStack can only be deserialized from a JsonObject");
		try {
			if (GsonHelper.isValidNode(jsonObject, "fluid")) {
				ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(jsonObject, "fluid"));
				int amount = GsonHelper.getAsInt(jsonObject, "amount");
				return new FluidIngredient(resourceLocation, amount, false);
			} else if (GsonHelper.isValidNode(jsonObject, "tag")) {
				ResourceLocation resourceLocation = new ResourceLocation(GsonHelper.getAsString(jsonObject, "tag"));
				int amount = GsonHelper.getAsInt(jsonObject, "amount");
				// special constructor call for JSONs
				return new FluidIngredient(resourceLocation, amount);
			}
		} catch (Exception e) {
			Electrodynamics.LOGGER.info("Invalid Fluid Type or Fluid amount entered in JSON file");
		}

		return null;
	}

	public boolean testFluid(@Nullable FluidStack t) {
		if (t != null) {
			for (FluidStack stack : getMatchingFluids()) {
				if (t.getAmount() >= stack.getAmount()) {
					if (t.getFluid().isSame(stack.getFluid())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static FluidIngredient read(FriendlyByteBuf input) {
		List<FluidStack> stacks = new ArrayList<>();
		int count = input.readInt();
		for (int i = 0; i < count; i++) {
			stacks.add(input.readFluidStack());
		}
		return new FluidIngredient(stacks);
	}

	public static FluidIngredient[] readList(FriendlyByteBuf buffer) {
		int length = buffer.readInt();
		FluidIngredient[] ings = new FluidIngredient[length];
		for (int i = 0; i < length; i++) {
			ings[i] = read(buffer);
		}
		return ings;
	}

	public void write(FriendlyByteBuf output) {
		fluidStacks = getMatchingFluids();
		output.writeInt(fluidStacks.size());
		for (FluidStack stack : fluidStacks) {
			output.writeFluidStack(stack);
		}
	}

	public static void writeList(FriendlyByteBuf buffer, List<FluidIngredient> list) {
		buffer.writeInt(list.size());
		for (FluidIngredient ing : list) {
			ing.write(buffer);
		}
	}

	public List<FluidStack> getMatchingFluids() {
		if (fluidStacks.isEmpty() && tag != null) {
			ForgeRegistries.FLUIDS.tags().getTag(tag).forEach(h -> {
				fluidStacks.add(new FluidStack(h, amount));
			});
		}
		return fluidStacks;
	}

	public FluidStack getFluidStack() {
		return getMatchingFluids().get(0);
	}

}
