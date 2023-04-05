package dev.the_seven_.simplekiln;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;

public class KilnContainer extends AbstractFurnaceMenu {
    public KilnContainer(int pContainerId, Inventory pInventory) {
        super(SimpleKiln.KILN_CONTAINER.get(), SimpleKiln.FIRING_RECIPE.get(), RecipeBookType.FURNACE, pContainerId, pInventory);
    }

    public KilnContainer(int pContainerId, Inventory pInventory, Container pContainer, ContainerData PData) {
        super(SimpleKiln.KILN_CONTAINER.get(), SimpleKiln.FIRING_RECIPE.get(), RecipeBookType.FURNACE, pContainerId, pInventory, pContainer, PData);
    }
}
