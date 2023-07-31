package blue.endless.engination.mixin;

import org.spongepowered.asm.mixin.Mixin;

import blue.endless.engination.EventCooldownable;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEventCooldownMixin extends Entity implements EventCooldownable {
	public ClientPlayerEventCooldownMixin(EntityType<?> variant, World world) {
		super(variant, world);
	}

	public long engination_eventCooldownStart = -1L;
	
	@Override
	public long engination_getEventCooldownStart() {
		return engination_eventCooldownStart;
	}

	@Override
	public void engination_setEventCooldownStart(long value) {
		engination_eventCooldownStart = value;
	}

	@Override
	public void engination_triggerEventCooldown() {
		if (getWorld() != null) {
			engination_eventCooldownStart = getWorld().getTime();
		} else {
			engination_eventCooldownStart = -1L;
		}
	}
}
