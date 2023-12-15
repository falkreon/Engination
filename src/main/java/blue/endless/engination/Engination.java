package blue.endless.engination;

import blue.endless.engination.block.ElevatorBlock;
import blue.endless.engination.block.EnginationBlocks;
import blue.endless.engination.block.entity.ItemBoxGuiDescription;
import blue.endless.engination.item.EnginationItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class Engination implements ModInitializer {
	public static SoundEvent SOUND_JUMP;
	public static SoundEvent SOUND_THROW;
	public static SoundEvent SOUND_SQUISH;
	public static SoundEvent SOUND_ITEM_BOX;
	
	public static final Identifier TELEPORT_REQUEST_CHANNEL = new Identifier("engination", "teleport_request");
	
	public static ItemGroup ENGINATION_GADGETS = 
			FabricItemGroup.builder()
			.displayName(Text.literal("Engination")) //Literal is okay here because it's a proper noun
			.entries((params, collector) -> {
				for(Block block : EnginationBlocks.BY_GROUP.values()) {
					collector.add(block);
				}
				
				collector.add(EnginationItems.TOMATO_SEEDS);
				collector.add(EnginationItems.TOMATO);
				collector.add(EnginationItems.CREATIVE_TOMATO);
				collector.add(EnginationItems.CELERY);
				collector.add(EnginationItems.SPARKLINE_TOOL);
			})
			.icon(()->new ItemStack(EnginationBlocks.BY_GROUP.get("launcher").iterator().next()))
			.build();
	
	public static ScreenHandlerType<ItemBoxGuiDescription> ITEM_BOX_SCREEN_HANDLER;
	
	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM_GROUP, new Identifier("engination", "gadgets"), ENGINATION_GADGETS);
		
		SOUND_JUMP =   SoundEvent.of(new Identifier("engination", "launcher_activate"));
		SOUND_THROW =  SoundEvent.of(new Identifier("engination", "projectile_throw"));
		SOUND_SQUISH = SoundEvent.of(new Identifier("engination", "squish"));
		SOUND_ITEM_BOX=SoundEvent.of(new Identifier("engination", "item_box"));
		
		Registry.register(Registries.SOUND_EVENT, "engination:launcher_activate", SOUND_JUMP);
		Registry.register(Registries.SOUND_EVENT, "engination:projectile_throw",  SOUND_THROW);
		Registry.register(Registries.SOUND_EVENT, "engination:squish",            SOUND_SQUISH);
		Registry.register(Registries.SOUND_EVENT, new Identifier("engination", "item_box"), SOUND_ITEM_BOX);
		
		EnginationBlocks.init();
		EnginationItems.init();
		
		ServerPlayNetworking.registerGlobalReceiver(TELEPORT_REQUEST_CHANNEL, (server, player, handler, buf, sender) -> {
			final BlockPos to = buf.readBlockPos();
			
			server.execute(() -> {
				BlockState destState = player.getWorld().getBlockState(to);
				ElevatorBlock.teleportEntity(player.getWorld(), to, destState, player);
			});
			
		});
		
		ITEM_BOX_SCREEN_HANDLER = new ScreenHandlerType<ItemBoxGuiDescription>(
				(syncId, inventory) -> new ItemBoxGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY),
				FeatureFlags.VANILLA_FEATURES
			);
		
		Registry.register(Registries.SCREEN_HANDLER, new Identifier("engination", "item_box"), ITEM_BOX_SCREEN_HANDLER);
	}
}
