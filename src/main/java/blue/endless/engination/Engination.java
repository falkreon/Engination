package blue.endless.engination;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import blue.endless.engination.block.EnginationBlocks;
import blue.endless.engination.block.entity.ItemBoxGuiDescription;
import blue.endless.engination.item.EnginationItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.feature_flags.FeatureFlags;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Engination implements ModInitializer {
	public static SoundEvent SOUND_JUMP;
	public static SoundEvent SOUND_THROW;
	public static SoundEvent SOUND_SQUISH;
	public static SoundEvent SOUND_ITEM_BOX;
	
	public static ItemGroup ENGINATION_GADGETS = 
			FabricItemGroup.builder()
			.name(Text.literal("Engination")) //Literal is okay here because it's a proper noun
			.entries((params, collector) -> {
				for(Block block : EnginationBlocks.BY_GROUP.values()) {
					collector.addItem(block);
				}
			})
			.icon(()->new ItemStack(EnginationBlocks.BY_GROUP.get("launcher").iterator().next()))
			.build();
	
	public static ScreenHandlerType<ItemBoxGuiDescription> ITEM_BOX_SCREEN_HANDLER;
	
	@Override
	public void onInitialize(ModContainer mod) {
		Registry.register(Registries.ITEM_GROUP, new Identifier("engination", "gadgets"), ENGINATION_GADGETS);
		
		SOUND_JUMP =   SoundEvent.createVariableRangeEvent(new Identifier("engination", "launcher_activate"));
		SOUND_THROW =  SoundEvent.createVariableRangeEvent(new Identifier("engination", "projectile_throw"));
		SOUND_SQUISH = SoundEvent.createVariableRangeEvent(new Identifier("engination", "squish"));
		SOUND_ITEM_BOX=SoundEvent.createVariableRangeEvent(new Identifier("engination", "item_box"));
		
		Registry.register(Registries.SOUND_EVENT, "engination:launcher_activate", SOUND_JUMP);
		Registry.register(Registries.SOUND_EVENT, "engination:projectile_throw",  SOUND_THROW);
		Registry.register(Registries.SOUND_EVENT, "engination:squish",            SOUND_SQUISH);
		Registry.register(Registries.SOUND_EVENT, new Identifier("engination", "item_box"), SOUND_ITEM_BOX);
		
		EnginationBlocks.init();
		EnginationItems.init();
		
		ITEM_BOX_SCREEN_HANDLER = new ScreenHandlerType<ItemBoxGuiDescription>(
				(syncId, inventory) -> new ItemBoxGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY),
				FeatureFlags.VANILLA_SET
			);
		
		Registry.register(Registries.SCREEN_HANDLER_TYPE, new Identifier("engination", "item_box"), ITEM_BOX_SCREEN_HANDLER);
	}
}
