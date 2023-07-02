package blue.endless.engination.block.entity;

import blue.endless.engination.block.EnginationBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class ItemBoxBlockEntity extends BlockEntity implements InventoryProvider, NamedScreenHandlerFactory {
	private DefaultedList<ItemStack> inventoryList = DefaultedList.ofSize(9, ItemStack.EMPTY);
	private SidedInventory inventory = InventoryImpl.of(inventoryList, this);
	
	public ItemBoxBlockEntity(BlockPos pos, BlockState state) {
		super(EnginationBlocks.ITEM_BOX_ENTITY, pos, state);
		
		//TODO: This is for testing. Remove later
		inventoryList.set(3, new ItemStack(Items.EMERALD, 3));
		inventoryList.set(8, new ItemStack(Items.GOLD_NUGGET, 8));
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
	}

	@Override
	public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		return inventory;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, inventoryList);
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, inventoryList);
	}
	
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return new ItemBoxGuiDescription(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
	}

	@Override
	public Text getDisplayName() {
		return Text.translatable(getCachedState().getBlock().getTranslationKey());
	}
}
