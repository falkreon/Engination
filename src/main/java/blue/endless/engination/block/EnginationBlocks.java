package blue.endless.engination.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import blue.endless.engination.Engination;

import blue.endless.engination.Grouped;
import blue.endless.engination.block.entity.ItemBoxBlockEntity;
import blue.endless.engination.item.CosmeticBlockItem;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EnginationBlocks {
	
	public static Map<String, List<Block>> BLOCK_GROUPS = new HashMap<>();
	
	public static TomatoBlock TOMATO_PLANT = null;
	
	public static Block ITEM_BOX_BLOCK;
	public static BlockEntityType<ItemBoxBlockEntity> ITEM_BOX_ENTITY;
	
	public static void init() {
		block("conveyor", "conveyor",                new ConveyorBlock(2.0), Engination.ENGINATION_GADGETS);
		block("conveyor", "fast_conveyor",           new ConveyorBlock(4.0), Engination.ENGINATION_GADGETS);
		block("conveyor", "ultra_fast_conveyor",     new ConveyorBlock(8.0), Engination.ENGINATION_GADGETS);
		
		block("launcher", "launcher",                new LauncherBlock(2.0), Engination.ENGINATION_GADGETS);
		block("launcher", "forceful_launcher",       new LauncherBlock(3.0), Engination.ENGINATION_GADGETS);
		block("launcher", "ultra_forceful_launcher", new LauncherBlock(5.0), Engination.ENGINATION_GADGETS);
		
		block("landingpad", "landing_pad",           new LandingPadBlock(),  Engination.ENGINATION_GADGETS);
		
		block("disappearing", "disappearing_melee",  new MeleeDisappearingBlock(), Engination.ENGINATION_GADGETS);
		block("disappearing", "disappearing_wooden_sword", new HeldItemDisappearingBlock(new ItemStack(Items.WOODEN_SWORD)), Engination.ENGINATION_GADGETS);
		block("disappearing", "disappearing_stone_sword", new HeldItemDisappearingBlock(new ItemStack(Items.STONE_SWORD)), Engination.ENGINATION_GADGETS);
		block("disappearing", "disappearing_iron_sword", new HeldItemDisappearingBlock(new ItemStack(Items.IRON_SWORD)), Engination.ENGINATION_GADGETS);
		block("disappearing", "disappearing_gold_sword", new HeldItemDisappearingBlock(new ItemStack(Items.GOLDEN_SWORD)), Engination.ENGINATION_GADGETS);
		block("disappearing", "disappearing_diamond_sword", new HeldItemDisappearingBlock(new ItemStack(Items.DIAMOND_SWORD)), Engination.ENGINATION_GADGETS);
		
		block("disappearing", "disappearing_sprint_speed", new SprintDisappearingBlock(), Engination.ENGINATION_GADGETS);
		block("disappearing", "disappearing_mount_speed", new MountDisappearingBlock(true), Engination.ENGINATION_GADGETS);
		block("disappearing", "disappearing_cart_speed", new MountDisappearingBlock(false), Engination.ENGINATION_GADGETS);
		
		block("disappearing", "fall_through", new FallThroughBlock(), Engination.ENGINATION_GADGETS);
		block("disappearing", "disguised_fall_through", new FallThroughBlock(), Engination.ENGINATION_GADGETS);
		
		ITEM_BOX_BLOCK =  block("question", "item_box", new InventoryItemBox(), Engination.ENGINATION_GADGETS);
		
		block("road", "road", new FastTravelBlock(0.2f), Engination.ENGINATION_GADGETS);
		block("road", "fast_road", new FastTravelBlock(0.4f), Engination.ENGINATION_GADGETS);
		block("road", "ultra_fast_road", new FastTravelBlock(0.6f), Engination.ENGINATION_GADGETS);
		
		TOMATO_PLANT = new TomatoBlock(Block.Settings.copy(Blocks.WHEAT));
		Registry.register(Registries.BLOCK, new Identifier("engination", "tomato_plant"), TOMATO_PLANT);
		
		
		ITEM_BOX_ENTITY = QuiltBlockEntityTypeBuilder.create(ItemBoxBlockEntity::new, ITEM_BOX_BLOCK).build();
		Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier("engination", "item_box"), ITEM_BOX_ENTITY);
	}

	public static Block block(String group, String name, Block block, ItemGroup itemGroup) {
		List<Block> blockGroup = BLOCK_GROUPS.get(group);
		if (blockGroup==null) {
			blockGroup = new ArrayList<>();
			BLOCK_GROUPS.put(group, blockGroup);
		}
		blockGroup.add(block);
		
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
			
			List<Block> blockGroup = BLOCK_GROUPS.get(groupId);
			if (blockGroup==null) return null;
			for(int i=0; i<blockGroup.size(); i++) {
				Block b = blockGroup.get(i);
				if (b==curBlock) {
					int nextBlock = (i + 1) % blockGroup.size();
					ItemStack result = new ItemStack(blockGroup.get(nextBlock), stack.getCount());
					return result;
				}
			}
		}
		
		return null;
	}
}
