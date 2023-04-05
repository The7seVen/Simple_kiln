package dev.the_seven_.simplekiln;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class KilnBlockEntity extends AbstractFurnaceBlockEntity {
    public KilnBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(SimpleKiln.KILN_ENTITY_TYPE.get(), pWorldPosition, pBlockState, SimpleKiln.FIRING_RECIPE.get());
    }

    protected Component getDefaultName() {
        return Component.translatable("container.simplekiln.kiln");
    }

    protected int getBurnDuration(ItemStack pFuel) {
        return super.getBurnDuration(pFuel) / 2;
    }

    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return new KilnContainer(pId, pPlayer, this, this.dataAccess);
    }
}
