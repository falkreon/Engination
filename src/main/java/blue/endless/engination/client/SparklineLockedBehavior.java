package blue.endless.engination.client;

import java.util.Optional;

import blue.endless.engination.block.EnginationBlocks;
import blue.endless.engination.block.entity.SparkBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SparklineLockedBehavior implements PlayerLockedBehavior {
	public static final double METERS_PER_SECOND = 40.0;
	public static final double METERS_PER_TICK = METERS_PER_SECOND / 20.0;
	public static final double TICKS_PER_METER = 1.0 / METERS_PER_TICK;
	
	BlockPos target;
	Vec3d prev;
	Vec3d next;
	int totalTicks;
	int ticksSoFar;
	
	@Override
	public void activate(Entity entity, BlockPos pos) {
		World world = entity.getWorld();
		BlockState state = world.getBlockState(pos);
		if (state.isOf(EnginationBlocks.SPARK_BLOCK)) {
			Optional<SparkBlockEntity> maybeBE = world.getBlockEntity(pos, EnginationBlocks.SPARK_BLOCK_ENTITY);
			if (maybeBE.isEmpty()) {
				stop();
				return;
			}
			
			prev = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
			var nextPos = maybeBE.get().next;
			if (nextPos == null) {
				stop();
				return;
			}
			target = nextPos;
			next = new Vec3d(nextPos.getX() + 0.5, nextPos.getY(), nextPos.getZ() + 0.5);
			
			totalTicks = (int) Math.ceil(prev.distanceTo(next) * TICKS_PER_METER) + 1;
			if (totalTicks <= 0) totalTicks = 1;
			ticksSoFar = 0;
		}
	}

	@Override
	public void tick(Entity entity) {
		if (prev == null) return;
		if (entity.isSneaking()) {
			//System.out.println("Entity is sneaking; Yeeting!");
			stop();
			return;
		}
		
		ticksSoFar++;
		if (ticksSoFar >= totalTicks) {
			//Look for connecting sparks!
			boolean chain = entity.getWorld().getBlockEntity(target, EnginationBlocks.SPARK_BLOCK_ENTITY).map(it -> it.next != null).orElse(false);
			if (chain) {
				activate(entity, target);
			} else {
				entity.setPosition(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
				stop();
				return;
			}
		} else {
			double t = ticksSoFar / (double) totalTicks;
			if (t < 0) t = 0;
			if (t > 1) t = 1;
			Vec3d cur = Lerp.of(prev, next, t);
			entity.setPosition(cur.x, cur.y, cur.z);
		}
	}
	
	public void stop() {
		prev = null;
	}

	@Override
	public boolean shouldContinue() {
		return prev != null && totalTicks > 0 && ticksSoFar < totalTicks;
	}

}
