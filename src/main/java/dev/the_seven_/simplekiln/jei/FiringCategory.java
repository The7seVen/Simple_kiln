package dev.the_seven_.simplekiln.jei;

import dev.the_seven_.simplekiln.FiringRecipe;
import dev.the_seven_.simplekiln.SimpleKiln;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;

public class FiringCategory extends AbstractCookingCategory<FiringRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(SimpleKiln.MOD_ID, "firing");

    public FiringCategory(IGuiHelper guiHelper) {
        super(guiHelper, SimpleKiln.KILN.get(), "gui.jei.category.firing", 100);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends FiringRecipe> getRecipeClass() {
        return FiringRecipe.class;
    }
}
