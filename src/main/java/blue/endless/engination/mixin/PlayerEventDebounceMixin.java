package blue.endless.engination.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import blue.endless.engination.SoundEventDebounceable;

@Mixin(ServerPlayerEntity.class)
public class PlayerEventDebounceMixin implements SoundEventDebounceable {
	protected BlockPos engination_lastTriggerLocation = null;
	protected long engination_lastTriggerTick = 0L;
	
	@Override
	public @Nullable BlockPos engination_getLastTriggerLocation() {
		return engination_lastTriggerLocation;
	}

	@Override
	public long engination_getLastTriggerTick() {
		return engination_lastTriggerTick;
	}

	@Override
	public void engination_setLastTrigger(BlockPos pos, long tick) {
		this.engination_lastTriggerLocation = pos;
		this.engination_lastTriggerTick = tick;
	}
}
