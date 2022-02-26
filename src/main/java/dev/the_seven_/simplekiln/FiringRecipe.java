package dev.the_seven_.simplekiln;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class FiringRecipe extends AbstractCookingRecipe {
    public FiringRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        super(SimpleKiln.FIRING_RECIPE, pId, pGroup, pIngredient, pResult, pExperience, pCookingTime);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(SimpleKiln.KILN.get());
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SimpleKiln.FIRING_SERIALIZER.get();
    }

    public static class FiringRecipeType implements IRecipeType<FiringRecipe> {
        @Override
        public String toString() {
            return "simplekiln:firing";
        }
    }
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<FiringRecipe> {

        @Override
        public FiringRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            String s = JSONUtils.getAsString(pJson, "group", "");
            JsonElement jsonelement = (JsonElement)(JSONUtils.isArrayNode(pJson, "ingredient") ? JSONUtils.getAsJsonArray(pJson, "ingredient") : JSONUtils.getAsJsonObject(pJson, "ingredient"));
            Ingredient ingredient = Ingredient.fromJson(jsonelement);
            //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
            if (!pJson.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            ItemStack itemstack;
            if (pJson.get("result").isJsonObject()) itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(pJson, "result"));
            else {
                String s1 = JSONUtils.getAsString(pJson, "result");
                ResourceLocation resourcelocation = new ResourceLocation(s1);
                itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
                    return new IllegalStateException("Item: " + s1 + " does not exist");
                }));
            }
            float f = JSONUtils.getAsFloat(pJson, "experience", 0.0F);
            int i = JSONUtils.getAsInt(pJson, "cookingtime", 100);
            return new FiringRecipe(pRecipeId, s, ingredient, itemstack, f, i);
        }

        @Nullable
        @Override
        public FiringRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
            String s = pBuffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack itemstack = pBuffer.readItem();
            float f = pBuffer.readFloat();
            int i = pBuffer.readVarInt();
            return new FiringRecipe(pRecipeId, s, ingredient, itemstack, f, i);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, FiringRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.group);
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeFloat(pRecipe.experience);
            pBuffer.writeVarInt(pRecipe.cookingTime);
        }
    }
}
