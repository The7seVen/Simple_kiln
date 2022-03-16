package dev.the_seven_.simplekiln;

import mezz.jei.api.constants.RecipeTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import org.apache.logging.log4j.LogManager;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.logging.Logger;

@Mod(SimpleKiln.MOD_ID)
public class SimpleKiln
{
    public static final String MOD_ID = "simplekiln";

    public static ResourceLocation INTERACT_WITH_KILN;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleKiln.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimpleKiln.MOD_ID);
    public static final RegistryObject<Block> KILN = registerBlock("kiln",
            () -> new KilnBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(litBlockEmission(13))));
    private static <T extends Block> RegistryObject<T> registerBlock(String pName, Supplier<T> pBlock) {
        RegistryObject<T> block = BLOCKS.register(pName, pBlock);
        SimpleKiln.ITEMS.register(pName, () -> new BlockItem(block.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        return block;
    }

    public static final DeferredRegister<BlockEntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SimpleKiln.MOD_ID);
    public static final RegistryObject<BlockEntityType<KilnBlockEntity>> KILN_ENTITY_TYPE = ENTITY_TYPES.register("kiln",
            () -> BlockEntityType.Builder.of(KilnBlockEntity::new, SimpleKiln.KILN.get()).build(null));

    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, SimpleKiln.MOD_ID);
    public static final RegistryObject<MenuType<KilnContainer>> KILN_CONTAINER = CONTAINERS.register("kiln",
            () -> new MenuType<>(KilnContainer::new));

    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimpleKiln.MOD_ID);
    public static final RegistryObject<RecipeSerializer<FiringRecipe>> FIRING_SERIALIZER = RECIPE_SERIALIZERS.register("firing",
            FiringRecipe.Serializer::new);
    public static RecipeType<FiringRecipe> FIRING_RECIPE = new FiringRecipe.FiringRecipeType();

    public SimpleKiln() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();


        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        ENTITY_TYPES.register(eventBus);
        CONTAINERS.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);

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

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerRecipe(final RegistryEvent.Register<RecipeSerializer<?>> registryEvent) {
            Registry.register(Registry.RECIPE_TYPE, new ResourceLocation("simplekiln:firing"), FIRING_RECIPE);
        }
        @SubscribeEvent
        public static void registerStat(final RegistryEvent.Register<StatType<?>> registryEvent) {
            INTERACT_WITH_KILN = makeCustomStat("simplekiln:interact_with_kiln", StatFormatter.DEFAULT);
        }
    }
}
