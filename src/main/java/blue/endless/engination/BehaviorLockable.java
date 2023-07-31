package blue.endless.engination;

import blue.endless.engination.client.PlayerLockedBehavior;

public interface BehaviorLockable {
	public PlayerLockedBehavior engination_getLockedBehavior();
	public void engination_setLockedBehavior(PlayerLockedBehavior behavior);
	
	default void engination_clearLockedBehavior() {
		engination_setLockedBehavior(null);
	}
}
