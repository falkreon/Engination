package blue.endless.engination.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

/**
 * This is a variation of Juuxel's ImplementedInventory from {@linkplain https://gist.github.com/Juuxel/32ea65a4c820b63c20798917f0f73706}
 * - used with permission.
 * 
 * <p>This simplifies the sometimes error-prone practice of implementing inventories by adopting a kind of trait pattern.
 */
@FunctionalInterface
public interface InventoryImpl extends SidedInventory {
	
	/**
	 * Acquires the internal item list for this Inventory. Don't call this! Call the regular Inventory methods instead.
	 */
	DefaultedList<ItemStack> getInventoryList();
	
	public static InventoryImpl of(DefaultedList<ItemStack> list) {
		return () -> list;
	}
	
	public static InventoryImpl of(DefaultedList<ItemStack> list, BlockEntity host) {
		return new InventoryImpl() {
			@Override
			public DefaultedList<ItemStack> getInventoryList() {
				return list;
			}
			
			@Override
			public void markDirty() {
				host.markDirty();
			}
		};
	}
	
	//implements Inventory {
	
	@Override
	default void clear() {
		getInventoryList().clear();
	}

	@Override
	default int size() {
		return getInventoryList().size();
	}

	/**
	 * Returns true if this inventory is empty, otherwise false.
	 * 
	 * <p>Deviates slightly from List contract in that a non-zero-sized list of all-EMPTY's will return true.
	 */
	@Override
	default boolean isEmpty() {
		for(int i=0; i<size(); i++) {
			if (!getStack(i).isEmpty()) return false;
		}
		
		return true;
	}

	@Override
	default ItemStack getStack(int slot) {
		return getInventoryList().get(slot);
	}

	@Override
	default ItemStack removeStack(int slot, int amount) {
		ItemStack result = Inventories.splitStack(getInventoryList(), slot, amount);
		if (!result.isEmpty()) {
			markDirty();
		}

		return result;
	}

	@Override
	default ItemStack removeStack(int slot) {
		return Inventories.removeStack(getInventoryList(), slot);
	}

	@Override
	default void setStack(int slot, ItemStack stack) {
		getInventoryList().set(slot, stack);
		if (stack.getCount() > getMaxCountPerStack()) {
			stack.setCount(getMaxCountPerStack());
		}
	}

	/**
	 * It is strongly recommended that individual implementors override this
	 */
	@Override
	default void markDirty() {
	}

	@Override
	default boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
	
	//}
	
	//implements SidedInventory {
	
	@Override
	default int[] getAvailableSlots(Direction side) {
		int[] result = new int[getInventoryList().size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = i;
		}

		return result;
	}
	
	@Override
	default boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return true;
	}
	
	@Override
	default boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return true;
	}
	
	//}
}
