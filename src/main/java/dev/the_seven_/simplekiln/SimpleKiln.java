package dev.the_seven_.simplekiln;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

@Mod(SimpleKiln.MOD_ID)
public class SimpleKiln
{
    public static final String MOD_ID = "simplekiln";

    public static final ResourceLocation INTERACT_WITH_KILN = makeCustomStat("interact_with_kiln", IStatFormatter.DEFAULT);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleKiln.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimpleKiln.MOD_ID);
    public static final RegistryObject<Block> KILN = registerBlock("kiln",
            () -> new KilnBlock(AbstractBlock.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F).lightLevel(litBlockEmission(13))));
    private static <T extends Block> RegistryObject<T> registerBlock(String pName, Supplier<T> pBlock) {
        RegistryObject<T> block = BLOCKS.register(pName, pBlock);
        SimpleKiln.ITEMS.register(pName, () -> new BlockItem(block.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
        return block;
    }

    public static final DeferredRegister<TileEntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SimpleKiln.MOD_ID);
    public static final RegistryObject<TileEntityType<KilnBlockEntity>> KILN_ENTITY_TYPE = ENTITY_TYPES.register("kiln",
            () -> TileEntityType.Builder.of(KilnBlockEntity::new, SimpleKiln.KILN.get()).build(null));

    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, SimpleKiln.MOD_ID);
    public static final RegistryObject<ContainerType<KilnContainer>> KILN_CONTAINER = CONTAINERS.register("kiln",
            () -> new ContainerType<>(KilnContainer::new));

    private static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimpleKiln.MOD_ID);
    public static final RegistryObject<IRecipeSerializer<FiringRecipe>> FIRING_SERIALIZER = RECIPE_SERIALIZERS.register("firing",
            FiringRecipe.Serializer::new);
    public static IRecipeType<FiringRecipe> FIRING_RECIPE = new FiringRecipe.FiringRecipeType();

    public SimpleKiln() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        ENTITY_TYPES.register(eventBus);
        CONTAINERS.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation("simplekiln:firing"), FIRING_RECIPE);

        eventBus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenManager.register(SimpleKiln.KILN_CONTAINER.get(), KilnScreen::new);
    }

    private static ToIntFunction<BlockState> litBlockEmission(int pLightValue) {
        return (p_50763_) -> p_50763_.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
    }

    private static ResourceLocation makeCustomStat(String pKey, IStatFormatter pFormatter) {
        ResourceLocation resourcelocation = new ResourceLocation(pKey);
        Registry.register(Registry.CUSTOM_STAT, pKey, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, pFormatter);
        return resourcelocation;
    }
}
