package dev.the_seven_.simplekiln.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.the_seven_.simplekiln.FiringRecipe;
import dev.the_seven_.simplekiln.SimpleKiln;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class FiringCategory implements IRecipeCategory<FiringRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(SimpleKiln.MOD_ID, "firing");

    private final IDrawable background;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    protected final IDrawableStatic staticFlame;
    protected final IDrawableAnimated animatedFlame;

    public FiringCategory(IGuiHelper guiHelper) {
//        super(guiHelper, SimpleKiln.KILN.get(), "gui.jei.category.firing", 100);
        this.background = guiHelper.createDrawable(KilnPlugin.RECIPE_GUI_VANILLA, 0, 114, 82, 54);
        this.regularCookTime = 100;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(SimpleKiln.KILN.get()));
        this.localizedName = new TranslatableComponent("gui.jei.category.firing");
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(KilnPlugin.RECIPE_GUI_VANILLA, 82, 128, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
        staticFlame = guiHelper.createDrawable(KilnPlugin.RECIPE_GUI_VANILLA, 82, 114, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
    }

    protected IDrawableAnimated getArrow(FiringRecipe recipe) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends FiringRecipe> getRecipeClass() {
        return FiringRecipe.class;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FiringRecipe recipe, IFocusGroup focuses) {
        IRecipeCategory.super.setRecipe(builder, recipe, focuses);
        builder.addSlot(INPUT, 1, 1)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(OUTPUT, 61, 19)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(FiringRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
        animatedFlame.draw(stack, 1, 20);

        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(stack, 24, 18);

        drawExperience(recipe, stack, 0);
        drawCookTime(recipe, stack, 45);
    }

    protected void drawExperience(FiringRecipe recipe, PoseStack poseStack, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            TranslatableComponent experienceString = new TranslatableComponent("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            fontRenderer.draw(poseStack, experienceString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    protected void drawCookTime(FiringRecipe recipe, PoseStack poseStack, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            TranslatableComponent timeString = new TranslatableComponent("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(poseStack, timeString, background.getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    @Override
    public List<Component> getTooltipStrings(FiringRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public boolean handleInput(FiringRecipe recipe, double mouseX, double mouseY, InputConstants.Key input) {
        return IRecipeCategory.super.handleInput(recipe, mouseX, mouseY, input);
    }

    @Override
    public boolean isHandled(FiringRecipe recipe) {
//        return IRecipeCategory.super.isHandled(recipe);
        return  !recipe.isSpecial();
    }

    //pre 9.4 compat
    @Override
    public void setIngredients(FiringRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FiringRecipe recipe, List<? extends IFocus<?>> focuses) {
        builder.addSlot(INPUT, 1, 1)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(OUTPUT, 61, 19)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FiringRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(1, false, 60, 18);

        guiItemStacks.set(ingredients);
    }

    @Override
    public void draw(FiringRecipe recipe, PoseStack stack, double mouseX, double mouseY) {
        animatedFlame.draw(stack, 1, 20);

        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(stack, 24, 18);

        drawExperience(recipe, stack, 0);
        drawCookTime(recipe, stack, 45);
    }
}
