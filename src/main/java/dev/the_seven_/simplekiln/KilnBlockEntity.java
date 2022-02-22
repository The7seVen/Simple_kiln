package dev.the_seven_.simplekiln;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class KilnBlockEntity extends AbstractFurnaceBlockEntity {
    public KilnBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(SimpleKiln.KILN_ENTITY_TYPE.get(), pWorldPosition, pBlockState, SimpleKiln.FIRING_RECIPE);
    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container.simplekiln.kiln");
    }

    protected int getBurnDuration(ItemStack pFuel) {
        return super.getBurnDuration(pFuel) / 2;
    }

    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return new KilnContainer(pId, pPlayer, this, this.dataAccess);
    }
}
