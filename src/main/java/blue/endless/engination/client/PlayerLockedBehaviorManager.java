package blue.endless.engination.client;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.entity.event.api.client.ClientEntityTickCallback;

import blue.endless.engination.BehaviorLockable;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class PlayerLockedBehaviorManager {
	@ClientOnly
	public static void init() {
		ClientEntityTickCallback.EVENT.register(PlayerLockedBehaviorManager::tick);
	}
	
	@ClientOnly
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
