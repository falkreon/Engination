package blue.endless.engination.mixin;

import org.spongepowered.asm.mixin.Mixin;

import blue.endless.engination.BehaviorLockable;
import blue.endless.engination.client.PlayerLockedBehavior;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class PlayerBehaviorLockableMixin implements BehaviorLockable {
	
	private PlayerLockedBehavior engination_lockedBehavior = null;
	
	@Override
	public PlayerLockedBehavior engination_getLockedBehavior() {
		return engination_lockedBehavior;
	}

	@Override
	public void engination_setLockedBehavior(PlayerLockedBehavior behavior) {
		engination_lockedBehavior = behavior;
	}
	
}
