package blue.endless.engination.client.screen;

import blue.endless.engination.block.entity.ItemBoxGuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ItemBoxScreen extends CottonInventoryScreen<ItemBoxGuiDescription> {

	public ItemBoxScreen(ItemBoxGuiDescription description, PlayerInventory inventory, Text title) {
		super(description, inventory, title);
	}

}
