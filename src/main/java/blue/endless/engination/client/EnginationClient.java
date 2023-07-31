package blue.endless.engination.client;

import java.util.Optional;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

import blue.endless.engination.Engination;
import blue.endless.engination.block.ElevatorBlock;
import blue.endless.engination.block.EnginationBlocks;
import blue.endless.engination.block.entity.ItemBoxGuiDescription;
import blue.endless.engination.block.entity.SparkBlockEntity;
import blue.endless.engination.client.screen.ItemBoxScreen;
import blue.endless.engination.item.EnginationItems;
import blue.endless.engination.item.SparklineTool;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.nbt.NbtCompound;
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
		BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), EnginationBlocks.SPARK_BLOCK);
		ColorProviderRegistry.BLOCK.register((state, view, pos, i) -> {
			return view.getBlockEntity(pos, EnginationBlocks.SPARK_BLOCK_ENTITY)
					.map(it -> it.color)
					.orElse(SparkBlockEntity.DEFAULT_COLOR);
		}, EnginationBlocks.SPARK_BLOCK);
		
		BlockRenderLayerMap.put(RenderLayer.getCutoutMipped(), EnginationBlocks.ELEVATOR);
		ColorProviderRegistry.BLOCK.register((state, view, pos, i) -> {
			return state.get(ElevatorBlock.COLOR).getSignColor();
		}, EnginationBlocks.ELEVATOR);
		
		ModelPredicateProviderRegistry.register(EnginationItems.SPARKLINE_TOOL, new Identifier("engination:sparkling"), (itemStack, clientWorld, livingEntity, i) -> {
			NbtCompound tag = itemStack.getNbt();
			if (tag == null) return 0.0f;
			return (tag.getBoolean(SparklineTool.SPARKLING_KEY)) ? 1.0f : 0.0f;
		});
		
		BlockEntityRendererFactories.register(EnginationBlocks.SPARK_BLOCK_ENTITY, SparkBlockEntityRenderer::new);
		
		
		HandledScreens.<ItemBoxGuiDescription, ItemBoxScreen>register(Engination.ITEM_BOX_SCREEN_HANDLER, (gui, inventory, title) -> new ItemBoxScreen(gui, inventory, title));
		
		PlayerLockedBehaviorManager.init();
	}
}
