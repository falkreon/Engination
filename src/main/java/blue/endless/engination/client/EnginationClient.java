package blue.endless.engination.client;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

import blue.endless.engination.Engination;
import blue.endless.engination.block.EnginationBlocks;
import blue.endless.engination.block.entity.ItemBoxGuiDescription;
import blue.endless.engination.client.screen.ItemBoxScreen;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class EnginationClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient(ModContainer mod) {
		for(Block block : EnginationBlocks.BY_GROUP.get("disappearing")) {
			BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), block);
		}
		for(Block block : EnginationBlocks.BY_GROUP.get("warpzone")) {
			//find the clear warp pipe and cut it out
			Identifier id = Registries.BLOCK.getId(block);
			if (id.getPath().equals("clear_warp_pipe") || id.getPath().equals("warp_pipe_entrance")) {
				BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), block);
			}
		}
			
		BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), EnginationBlocks.TOMATO_PLANT);
		
		HandledScreens.<ItemBoxGuiDescription, ItemBoxScreen>register(Engination.ITEM_BOX_SCREEN_HANDLER, (gui, inventory, title) -> new ItemBoxScreen(gui, inventory, title));
	}
}
