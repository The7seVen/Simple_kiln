package dev.the_seven_.simplekiln;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class KilnBlockEntity extends AbstractFurnaceTileEntity {
    public KilnBlockEntity() {
        super(SimpleKiln.KILN_ENTITY_TYPE.get(), SimpleKiln.FIRING_RECIPE);
    }

    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.simplekiln.kiln");
    }

    protected int getBurnDuration(ItemStack pFuel) {
        return super.getBurnDuration(pFuel) / 2;
    }

    protected Container createMenu(int pId, PlayerInventory pPlayer) {
        return new KilnContainer(pId, pPlayer, this, this.dataAccess);
    }
}
