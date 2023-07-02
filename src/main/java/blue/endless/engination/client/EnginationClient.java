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

public class EnginationClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient(ModContainer mod) {
		for(Block block : EnginationBlocks.BLOCK_GROUPS.get("disappearing")) {
			BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), block);
		}
		BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), EnginationBlocks.TOMATO_PLANT);
		
		HandledScreens.<ItemBoxGuiDescription, ItemBoxScreen>register(Engination.ITEM_BOX_SCREEN_HANDLER, (gui, inventory, title) -> new ItemBoxScreen(gui, inventory, title));
	}
}
