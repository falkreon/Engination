package blue.endless.engination;

import net.minecraft.util.math.BlockPos;

public interface SoundEventDebounceable {
	public BlockPos engination_getLastTriggerLocation();
	public long engination_getLastTriggerTick();
	public void engination_setLastTrigger(BlockPos pos, long tick);
}
