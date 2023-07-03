package blue.endless.engination;

import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoundEventDebouncer {
	public static final long DEBOUNCE_TIME = 3L;
	
	public static void play(Entity triggerer, World world, BlockPos pos, SoundEvent event) {
		play(triggerer, world, pos, event, 0.3f, (float) world.getRandom().nextTriangular(1.0f, 0.15f));
	}
	
	public static void play(Entity triggerer, World world, BlockPos pos, SoundEvent event, float volume, float pitch) {
		if (world.isClient()) return; //We only do serverside events
		if (triggerer instanceof SoundEventDebounceable debounceable) {
			BlockPos lastPos = debounceable.engination_getLastTriggerLocation();
			long lastTick = debounceable.engination_getLastTriggerTick();
			long elapsed = world.getTime() - lastTick;
			
			if (lastPos!=null && lastPos.equals(pos) && elapsed < DEBOUNCE_TIME) {
				//This is what debounce is for!
			} else {
				world.playSound(null, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, event, SoundCategory.BLOCKS, volume, pitch);
				debounceable.engination_setLastTrigger(pos, world.getTime());
			}
		} else {
			world.playSound(null, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, event, SoundCategory.BLOCKS, volume, pitch);
		}
	}
}
