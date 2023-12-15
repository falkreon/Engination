package blue.endless.engination.client;

import blue.endless.engination.BehaviorLockable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class PlayerLockedBehaviorManager {
	@Environment(EnvType.CLIENT)
	public static void init() {
		//TODO: This will need to be replaced with a mixin
		//ClientEntityTickCallback.EVENT.register(PlayerLockedBehaviorManager::tick);
		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			if (
					client.player == null ||
					client.player.clientWorld == null ||
					!client.player.isAlive()) return; // Only tick if player is present, alive, and in a world.
			
			tick(client.player, client.player.hasVehicle());
		});
		/*
		ClientTickEvents.END_WORLD_TICK.register((world) -> {
			
			for (AbstractClientPlayerEntity entity : world.getPlayers()) {
				tick(entity, entity.hasVehicle());
			}
		});*/
	}
	
	@Environment(EnvType.CLIENT)
	private static void tick(Entity entity, boolean isPassenger) {
		if (entity instanceof BehaviorLockable lockable) {

			PlayerLockedBehavior behavior = lockable.engination_getLockedBehavior();
			if (behavior != null) {
				if (isPassenger) {
					entity.stopRiding();
				}
				
				behavior.tick(entity);
				
				if (!behavior.shouldContinue()) {
					behavior.deactivate(entity);
					lockable.engination_clearLockedBehavior();
				}
			}
		}
	}
	
	public static void lockPlayer(Entity player, PlayerLockedBehavior behavior, BlockPos startLocation) {
		if (player instanceof BehaviorLockable lockable) {
			behavior.activate(player, startLocation);
			lockable.engination_setLockedBehavior(behavior);
		}
	}
}
