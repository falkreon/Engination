package blue.endless.engination.client;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public interface PlayerLockedBehavior {
	public void activate(Entity entity, BlockPos pos);
	public default void deactivate(Entity entity) {}
	public void tick(Entity entity);
	public boolean shouldContinue();
}
