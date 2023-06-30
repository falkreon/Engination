package blue.endless.engination.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MeleeDisappearingBlock extends DisappearingBlock {
	public static final ChainReactionType CHAINTYPE_PUNCH = new ChainReactionType();
	
	@Override
	public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (world.isClient()) return; 
		if (player.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
			this.disappearChainReaction(world, pos);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient()) return ActionResult.SUCCESS;
		
		if (player.isCreative() || !player.getAbilities().allowModifyWorld) {
			this.disappearChainReaction(world, pos);
		}
		
		return ActionResult.PASS;
	}
	
	//@Override
	public ChainReactionType getChainReactionType() {
		return CHAINTYPE_PUNCH;
	}
}
