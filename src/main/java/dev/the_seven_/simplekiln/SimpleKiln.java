package dev.the_seven_.simplekiln;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

@Mod(SimpleKiln.MOD_ID)
public class SimpleKiln
{
    public static final String MOD_ID = "simplekiln";

//    public static final ResourceLocation INTERACT_WITH_KILN = makeCustomStat("interact_with_kiln", StatFormatter.DEFAULT);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleKiln.MOD_ID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimpleKiln.MOD_ID);

    public static final RegistryObject<Block> KILN = registerBlock("kiln",
            () -> new KilnBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(litBlockEmission(13))));

    private static <T extends Block> RegistryObject<T> registerBlock(String pName, Supplier<T> pBlock) {
        RegistryObject<T> block = BLOCKS.register(pName, pBlock);
        SimpleKiln.ITEMS.register(pName, () -> new BlockItem(block.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        return block;
    }

    public static final DeferredRegister<BlockEntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SimpleKiln.MOD_ID);

    public static final RegistryObject<BlockEntityType<KilnBlockEntity>> KILN_ENTITY_TYPE = ENTITY_TYPES.register("kiln",
            () -> BlockEntityType.Builder.of(KilnBlockEntity::new, SimpleKiln.KILN.get()).build(null));

    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SimpleKiln.MOD_ID);

    public static final RegistryObject<MenuType<KilnContainer>> KILN_CONTAINER = MENU_TYPES.register("kiln",
            () -> new MenuType<>(KilnContainer::new));

    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimpleKiln.MOD_ID);

    public static final RegistryObject<RecipeSerializer<FiringRecipe>> FIRING_SERIALIZER = RECIPE_SERIALIZERS.register("firing", () -> new SimpleCookingSerializer<>(FiringRecipe::new, 100));

    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, SimpleKiln.MOD_ID);

    public static RegistryObject<RecipeType<FiringRecipe>> FIRING_RECIPE = RECIPE_TYPES.register("firing", FiringRecipe.FiringRecipeType::new);

    public static final mezz.jei.api.recipe.RecipeType<FiringRecipe> FIRING = mezz.jei.api.recipe.RecipeType.create(MOD_ID, "firing", FiringRecipe.class);


    public SimpleKiln() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        ENTITY_TYPES.register(eventBus);
        MENU_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);

        eventBus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MenuScreens.register(SimpleKiln.KILN_CONTAINER.get(), KilnScreen::new);
    }

    private static ToIntFunction<BlockState> litBlockEmission(int pLightValue) {
        return (p_50763_) -> p_50763_.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
    }

    private static ResourceLocation makeCustomStat(String pKey, StatFormatter pFormatter) {
        ResourceLocation resourcelocation = new ResourceLocation(pKey);
        Registry.register(Registry.CUSTOM_STAT, pKey, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, pFormatter);
        return resourcelocation;
    }
}
