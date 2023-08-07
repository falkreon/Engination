package blue.endless.engination.block.entity;

import org.quiltmc.loader.api.minecraft.ClientOnly;

import blue.endless.engination.Engination;
import blue.endless.engination.block.InventoryItemBox;
import blue.endless.engination.client.screen.ImageBackgroundPainter;
import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WPlayerInvPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class ItemBoxGuiDescription extends SyncedGuiDescription {
	private static final int INVENTORY_SIZE = 9;
	
	private WItemSlot grid;
	private WPlainPanel playerInventoryContainer;
	private WPlayerInvPanel playerInventoryPanel;
	private boolean creative;
	
	public ItemBoxGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(Engination.ITEM_BOX_SCREEN_HANDLER, syncId, playerInventory, getBlockInventory(context, INVENTORY_SIZE), getBlockPropertyDelegate(context));
		
		creative = context.get((world, pos) -> {
			BlockState state = world.getBlockState(pos);
			if (state.getBlock() instanceof InventoryItemBox box) {
				return box.isCreative();
			} else {
				return false;
			}
		}).orElse(false);
		
		this.setTitleColor(0xFF_FFFFFF);
		this.setTitleAlignment(HorizontalAlignment.CENTER);
		
		WPlainPanel root = new WPlainPanel();
		setRootPanel(root);
		root.setInsets(Insets.ROOT_PANEL);
		
		
		grid = WItemSlot.of(blockInventory, 0, 3, 3);
		root.add(grid, 60, 18);
		
		playerInventoryContainer = new WPlainPanel();
		playerInventoryContainer.setInsets(Insets.ROOT_PANEL);
		
		playerInventoryPanel = createPlayerInventoryPanel(false);
		
		playerInventoryContainer.add(playerInventoryPanel, 0, 0, 162, 87);
		root.add(playerInventoryContainer, 0, 84, 176, 90);

		root.validate(this);
	}
	
	@ClientOnly
	@Override
	public void addPainters() {
		//Do not call super! We do not want the typical root painter!
		grid.setBackgroundPainter(new ImageBackgroundPainter(new Identifier("engination","textures/gui/item_box.png"), 5, 5, 54, 54));
		playerInventoryContainer.setBackgroundPainter(BackgroundPainter.VANILLA);
	}
	
	@Override
	public boolean canUse(PlayerEntity entity) {
		if (this.creative && !entity.isCreative()) return false;
		
		return super.canUse(entity);
	}
}
