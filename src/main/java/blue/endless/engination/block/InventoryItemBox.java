package blue.endless.engination.block;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ArrayList;

import blue.endless.engination.block.entity.ItemBoxBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class InventoryItemBox extends ItemBox implements BlockEntityProvider {
	private boolean isCreative = false;
	
	public InventoryItemBox() {
		this.setReappearDelay(10);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ItemBoxBlockEntity(pos, state);
	}
	
	@Override
	public void dispense(BlockState state, World world, BlockPos pos) {
		SidedInventory inv = getInventory(state, world, pos);
		
		List<Integer> sourceSlots = new ArrayList<>();
		for(int i=0; i<inv.size(); i++) {
			if (!inv.getStack(i).isEmpty()) sourceSlots.add(i);
		}
		if (sourceSlots.size() == 0) {
			return;
		}
		int i = world.getRandom().nextInt(sourceSlots.size());
		if (i>=sourceSlots.size()) i=0; //At the risk of making slot zero more likely, this will squash any off-by-1 bugs
		int slotToTake = sourceSlots.get(i);
		ItemStack stack = ItemStack.EMPTY;
		if (isCreative) {
			stack = inv.getStack(slotToTake).withCount(1); // implicit copy
		} else {
			stack = inv.removeStack(slotToTake, 1);
		}
		
		this.dispenseItem(world, pos, stack, KICK_DOUBLE);
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		SidedInventory inv = getInventory(state, world, pos);
		if (inv.isEmpty()) return;
		
		super.scheduledTick(state, world, pos, random);
	}

	@Nullable
	private SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		return world
				.getBlockEntity(pos, EnginationBlocks.ITEM_BOX_ENTITY)
				.map(it->it.getInventory(state, world, pos))
				.orElse(null);
	}
	
	@Nullable
	@Override
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory)blockEntity : null;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

		if (!player.canModifyBlocks()) return ActionResult.PASS;
		if (isCreative && !player.getAbilities().creativeMode) return ActionResult.PASS;
		
		player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
		return ActionResult.SUCCESS;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock()) && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
			world.getBlockEntity(pos, EnginationBlocks.ITEM_BOX_ENTITY).ifPresent(be -> {
				ItemScatterer.spawn(world, pos, be.getInventory(state, world, pos));
			});
			
			world.updateComparators(pos, this);
		}
		
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	public boolean isCreative() {
		return isCreative;
	}
	
	public static InventoryItemBox makeCreative() {
		InventoryItemBox result = new InventoryItemBox();
		result.isCreative = true;
		return result;
	}
}
