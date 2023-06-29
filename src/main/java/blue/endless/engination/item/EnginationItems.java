package blue.endless.engination.item;

import blue.endless.engination.block.EnginationBlocks;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EnginationItems {
	public static TomatoItem       TOMATO;
	public static TomatoItem       CREATIVE_TOMATO;
	public static Item             CELERY;
	public static AliasedBlockItem TOMATO_SEEDS;
	
	public static final FoodComponent USELESS_FOOD = new FoodComponent.Builder()
			.alwaysEdible()
			.hunger(-1)
			.build();
	
	public static void init() {
		TOMATO = new TomatoItem(false);
		CREATIVE_TOMATO = new TomatoItem(true);
		CELERY = new Item(new Item.Settings().food(USELESS_FOOD));
		TOMATO_SEEDS = new AliasedBlockItem(EnginationBlocks.TOMATO_PLANT, new Item.Settings());
		
		Registry.register(Registries.ITEM, new Identifier("engination", "tomato"), TOMATO);
		Registry.register(Registries.ITEM, new Identifier("engination", "creative_tomato"), CREATIVE_TOMATO);
		
		Registry.register(Registries.ITEM, new Identifier("engination", "celery"), CELERY);
		Registry.register(Registries.ITEM, new Identifier("engination", "tomato_seeds"), TOMATO_SEEDS);
		
		
		
		
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