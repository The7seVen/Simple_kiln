package dev.the_seven_.simplekiln;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class FiringRecipe extends AbstractCookingRecipe {
    public FiringRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        super(SimpleKiln.FIRING_RECIPE.get(), pId, pGroup, pIngredient, pResult, pExperience, pCookingTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(SimpleKiln.KILN.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SimpleKiln.FIRING_SERIALIZER.get();
    }

    public static class FiringRecipeType implements RecipeType<FiringRecipe> {
        @Override
        public String toString() {
            return "simplekiln:firing";
        }
    }
}
