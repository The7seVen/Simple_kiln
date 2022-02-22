package dev.the_seven_.simplekiln;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class FiringRecipe extends AbstractCookingRecipe {
    public FiringRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        super(SimpleKiln.FIRING_RECIPE, pId, pGroup, pIngredient, pResult, pExperience, pCookingTime);
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
