package blue.endless.engination.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmeraldQuestionBlock extends ItemBox {

	public EmeraldQuestionBlock() {
		super(Block.Settings.create()
				.sounds(BlockSoundGroup.DEEPSLATE)
				.mapColor(DyeColor.GREEN)
				.hardness(1)
				.resistance(15)
				);
	}

	@Override
	public void dispense(BlockState state, World world, BlockPos pos) {
		dispenseItem(world, pos, new ItemStack(Items.EMERALD), 0.4);
	}
}
