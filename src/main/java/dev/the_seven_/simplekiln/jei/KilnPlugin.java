package dev.the_seven_.simplekiln.jei;

import dev.the_seven_.simplekiln.KilnContainer;
import dev.the_seven_.simplekiln.KilnScreen;
import dev.the_seven_.simplekiln.SimpleKiln;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class KilnPlugin implements IModPlugin {

    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(ModIds.JEI_ID, "textures/gui/gui_vanilla.png");

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SimpleKiln.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FiringCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(SimpleKiln.KILN.get()), SimpleKiln.FIRING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(KilnScreen.class, 78, 32, 28, 23, SimpleKiln.FIRING, RecipeTypes.FUELING);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        registration.addRecipes(SimpleKiln.FIRING, rm.getAllRecipesFor(SimpleKiln.FIRING_RECIPE.get()).stream()
                        .filter(Objects::nonNull).collect(Collectors.toList()));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(KilnContainer.class, SimpleKiln.KILN_CONTAINER.get(), SimpleKiln.FIRING, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(KilnContainer.class, SimpleKiln.KILN_CONTAINER.get(), RecipeTypes.FUELING, 1, 1, 3, 36);
    }
}
