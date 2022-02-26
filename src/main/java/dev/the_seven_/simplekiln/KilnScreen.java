package dev.the_seven_.simplekiln;

import net.minecraft.client.gui.screen.inventory.AbstractFurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KilnScreen extends AbstractFurnaceScreen<KilnContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SimpleKiln.MOD_ID, "textures/gui/container/kiln.png");

    public KilnScreen(KilnContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
        super(pMenu, new FiringRecipeBookComponent(), pPlayerInventory, pTitle, TEXTURE);
    }
}
