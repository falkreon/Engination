package blue.endless.engination.block;

import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import blue.endless.engination.Grouped;
import blue.endless.engination.block.entity.ItemBoxBlockEntity;
import blue.endless.engination.block.entity.SparkBlockEntity;
import blue.endless.engination.item.CosmeticBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EnginationBlocks {
	
	public static final Multimap<String, Block> BY_GROUP = MultimapBuilder.hashKeys().arrayListValues().build();
	
	public static TomatoBlock TOMATO_PLANT = null;
	
	public static Block ITEM_BOX;
	public static Block CREATIVE_ITEM_BOX;
	public static BlockEntityType<ItemBoxBlockEntity> ITEM_BOX_ENTITY;
	public static Block SPARK_BLOCK = new SparkBlock();
	public static BlockEntityType<SparkBlockEntity> SPARK_BLOCK_ENTITY;
	public static Block ELEVATOR;
	public static Block ONE_SIDED;
	
	public static void init() {
		block("conveyor", "conveyor",                new ConveyorBlock(2.0));
		block("conveyor", "fast_conveyor",           new ConveyorBlock(4.0));
		block("conveyor", "ultra_fast_conveyor",     new ConveyorBlock(8.0));
		
		block("launcher", "launcher",                new LauncherBlock(2.0));
		block("launcher", "forceful_launcher",       new LauncherBlock(3.0));
		block("launcher", "ultra_forceful_launcher", new LauncherBlock(5.0));
		
		block("landingpad", "landing_pad",           new LandingPadBlock());
		
		block("disappearing", "disappearing_melee",  new MeleeDisappearingBlock());
		block("disappearing", "disappearing_wooden_sword", new HeldItemDisappearingBlock(new ItemStack(Items.WOODEN_SWORD)));
		block("disappearing", "disappearing_stone_sword", new HeldItemDisappearingBlock(new ItemStack(Items.STONE_SWORD)));
		block("disappearing", "disappearing_iron_sword", new HeldItemDisappearingBlock(new ItemStack(Items.IRON_SWORD)));
		block("disappearing", "disappearing_gold_sword", new HeldItemDisappearingBlock(new ItemStack(Items.GOLDEN_SWORD)));
		block("disappearing", "disappearing_diamond_sword", new HeldItemDisappearingBlock(new ItemStack(Items.DIAMOND_SWORD)));
		
		block("disappearing", "disappearing_sprint_speed", new SprintDisappearingBlock());
		block("disappearing", "disappearing_mount_speed", new MountDisappearingBlock(true));
		block("disappearing", "disappearing_cart_speed", new MountDisappearingBlock(false));
		
		block("disappearing", "fall_through", new FallThroughBlock());
		block("disappearing", "disguised_fall_through", new FallThroughBlock());
		
		ELEVATOR = block("elevator", "elevator", new ElevatorBlock());
		
		ONE_SIDED = block("one_sided", "one_sided", new OneSidedFloorBlock(Block.Settings.copy(ELEVATOR)));
		
		ITEM_BOX =  block("question", "item_box", new InventoryItemBox());
		CREATIVE_ITEM_BOX =  block("question", "creative_item_box", InventoryItemBox.makeCreative());
		((InventoryItemBox) CREATIVE_ITEM_BOX).setReappearDelay(20*5);
		
		block("road", "road", new FastTravelBlock(0.2f));
		block("road", "fast_road", new FastTravelBlock(0.4f));
		block("road", "ultra_fast_road", new FastTravelBlock(0.6f));
		
		Registry.register(Registries.BLOCK, new Identifier("engination", "spark_block"), SPARK_BLOCK); //No corresponding BlockItem
		
		TOMATO_PLANT = new TomatoBlock(Block.Settings.copy(Blocks.WHEAT));
		Registry.register(Registries.BLOCK, new Identifier("engination", "tomato_plant"), TOMATO_PLANT);
		
		
		ITEM_BOX_ENTITY = QuiltBlockEntityTypeBuilder.create(ItemBoxBlockEntity::new, ITEM_BOX, CREATIVE_ITEM_BOX).build();
		Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier("engination", "item_box"), ITEM_BOX_ENTITY);
		
		SPARK_BLOCK_ENTITY = QuiltBlockEntityTypeBuilder.create(SparkBlockEntity::new, SPARK_BLOCK).build();
		Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier("engination", "spark_block"), SPARK_BLOCK_ENTITY);
	}

	public static <T extends Block> T block(String group, String name, T block) {
		BY_GROUP.put(group, block);
		
		Registry.register(Registries.BLOCK, new Identifier("engination", name), block);
		
		Item.Settings itemSettings = new Item.Settings();
		BlockItem item = (block instanceof Grouped) ? new CosmeticBlockItem(block, itemSettings) : new BlockItem(block, itemSettings);
		Registry.register(Registries.ITEM, new Identifier("engination", name), item);
		
		return block;
	}

	@Nullable
	public static ItemStack getNextItem(ItemStack stack, String groupId) {
		if (stack.getItem() instanceof BlockItem) {
			Block curBlock = ((BlockItem)stack.getItem()).getBlock();
			
			boolean found = false;
			for(Block block : BY_GROUP.get(groupId)) {
				if (found) {
					return new ItemStack(block, stack.getCount());
				} else {
					if (block == curBlock) {
						found = true;
					}
				}
			}
		}
		
		return null;
	}
}
