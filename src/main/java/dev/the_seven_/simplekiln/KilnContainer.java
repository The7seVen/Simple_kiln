package dev.the_seven_.simplekiln;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.AbstractFurnaceContainer;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.util.IIntArray;

public class KilnContainer extends AbstractFurnaceContainer {
    public KilnContainer(int pContainerId, PlayerInventory pInventory) {
        super(SimpleKiln.KILN_CONTAINER.get(), SimpleKiln.FIRING_RECIPE, RecipeBookCategory.FURNACE, pContainerId, pInventory);
    }

    public KilnContainer(int pContainerId, PlayerInventory pInventory, IInventory pContainer, IIntArray PData) {
        super(SimpleKiln.KILN_CONTAINER.get(), SimpleKiln.FIRING_RECIPE, RecipeBookCategory.FURNACE, pContainerId, pInventory, pContainer, PData);
    }
}
