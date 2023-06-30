package blue.endless.engination.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import blue.endless.engination.block.HeldItemDisappearingBlock;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class RestrictedLeftClickMixin {
	/*
	@Shadow @Final
	protected ServerPlayerEntity player;
	@Shadow
	protected ServerWorld world;
	@Shadow
	private GameMode gameMode;

	@Inject(at = { @At("HEAD") }, method="processBlockBreakingAction")
	public void processBlockBreakingAction(BlockPos pos, Action action, Direction direction, int worldHeight, int i, CallbackInfo callbackInfo) {
		System.out.println("INJECT FIRED");
		if (world.getBlockState(pos).getBlock() instanceof HeldItemDisappearingBlock disappearingBlock) {
			//Check to see if we're in a mode that restricts world editing
			if (!player.isBlockBreakingRestricted(this.world, pos, this.gameMode)) return; //Leftclick action will fire normally, so skip the mixin
			
			//We're before the distance checks, so let's make sure the block is in range
			if (this.player.getEyePos().squaredDistanceTo(Vec3d.ofCenter(pos)) > ServerPlayNetworkHandler.MAX_INTERACTION_DISTANCE) return;
			
			System.out.println("Full inject activation");
			disappearingBlock.onBlockBreakStart(world.getBlockState(pos), world, pos, player);
		}
	}*/
}
