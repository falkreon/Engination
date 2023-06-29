package blue.endless.engination.client;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

import blue.endless.engination.block.EnginationBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

public class EnginationClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient(ModContainer mod) {
		for(Block block : EnginationBlocks.BLOCK_GROUPS.get("disappearing")) {
			BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), block);
		}
		BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), EnginationBlocks.TOMATO_PLANT);
	}
}
