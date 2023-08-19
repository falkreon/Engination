package blue.endless.engination.item;

import blue.endless.engination.block.EnginationBlocks;
import blue.endless.engination.block.SparkBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EnginationItems {
	
	public static final TagKey<Item> SPARKLINE_ACTIVATORS = TagKey.of(RegistryKeys.ITEM, SparkBlock.ACTIVATION_ID);
	
	public static TomatoItem TOMATO = new TomatoItem(false);
	public static TomatoItem CREATIVE_TOMATO = new TomatoItem(true);
	public static AliasedBlockItem TOMATO_SEEDS = new AliasedBlockItem(EnginationBlocks.TOMATO_PLANT, new Item.Settings());
	
	public static final FoodComponent USELESS_FOOD = new FoodComponent.Builder()
			.alwaysEdible()
			.hunger(-1)
			.build();
	public static Item CELERY = new Item(new Item.Settings().food(USELESS_FOOD));
	
	public static Item SPARKLINE_TOOL = new SparklineTool();
	
	public static void init() {
		Registry.register(Registries.ITEM, new Identifier("engination", "tomato"), TOMATO);
		Registry.register(Registries.ITEM, new Identifier("engination", "creative_tomato"), CREATIVE_TOMATO);
		Registry.register(Registries.ITEM, new Identifier("engination", "tomato_seeds"), TOMATO_SEEDS);
		
		Registry.register(Registries.ITEM, new Identifier("engination", "celery"), CELERY);
		
		Registry.register(Registries.ITEM, new Identifier("engination", "sparkline_tool"), SPARKLINE_TOOL);
		
		DispenserBlock.registerBehavior(TOMATO, new TomatoItem.DispenserBehavior(TOMATO));
		DispenserBlock.registerBehavior(CREATIVE_TOMATO, new TomatoItem.CreativeDispenserBehavior(CREATIVE_TOMATO));
		
		
		//TODO: Get help on the FabricLootSupplierBuilder front, I'm either doing something wrong here or the API doesn't work.
		
		//LootTableLoadingCallback.EVENT.register(EnginationItems::onLootTableLoad);
	}
	
	/*
	public static void onLootTableLoad(ResourceManager resourceManager, LootManager manager, Identifier id, FabricLootSupplierBuilder supplier, LootTableSetter setter) {
		if (id.equals(new Identifier("minecraft", "blocks/grass"))) {
			System.out.println("Injecting tomato seeds");
			
			LootPool.Builder tomatoSeedsPool = new LootPool.Builder()
					.withEntry( ItemEntry.builder(TOMATO_SEEDS) )
					.withRolls(UniformLootTableRange.between(1, 1))
					.method_356(RandomChanceLootCondition.builder(0.07f))
			;
					//.build();
			
			supplier.withPool(tomatoSeedsPool);
			
			//setter.set(supplier.create());
		} else {
			if (id.getPath().contains("grass")) System.out.println(id);
			
		}
	}*/
}