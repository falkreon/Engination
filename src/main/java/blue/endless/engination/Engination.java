package blue.endless.engination;

import java.util.List;
import java.util.Map;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import blue.endless.engination.block.EnginationBlocks;
import blue.endless.engination.item.EnginationItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Engination implements ModInitializer {
	public static SoundEvent SOUND_JUMP;
	public static SoundEvent SOUND_THROW;
	public static SoundEvent SOUND_SQUISH;
	
	public static ItemGroup ENGINATION_GADGETS = 
			FabricItemGroup.builder()
			.name(Text.literal("Engination")) //Literal is okay here because it's a proper noun
			.entries((params, collector) -> {
				for(Map.Entry<String, List<Block>> entry : EnginationBlocks.BLOCK_GROUPS.entrySet()) {
					for(Block b : entry.getValue()) {
						collector.addItem(b);
					}
				}
				//TODO: Add gadgets
			})
			.icon(()->new ItemStack(EnginationBlocks.BLOCK_GROUPS.get("launcher").get(0)))
			.build();
	
	@Override
	public void onInitialize(ModContainer mod) {
		Registry.register(Registries.ITEM_GROUP, new Identifier("engination", "gadgets"), ENGINATION_GADGETS);
		
		SOUND_JUMP =   SoundEvent.createVariableRangeEvent(new Identifier("engination", "launcher_activate"));
		SOUND_THROW =  SoundEvent.createVariableRangeEvent(new Identifier("engination", "projectile_throw"));
		SOUND_SQUISH = SoundEvent.createVariableRangeEvent(new Identifier("engination", "squish"));
		
		Registry.register(Registries.SOUND_EVENT, "engination:launcher_activate", SOUND_JUMP);
		Registry.register(Registries.SOUND_EVENT, "engination:projectile_throw",  SOUND_THROW);
		Registry.register(Registries.SOUND_EVENT, "engination:squish",            SOUND_SQUISH);
		
		EnginationBlocks.init();
		EnginationItems.init();
		
		
	}
}
