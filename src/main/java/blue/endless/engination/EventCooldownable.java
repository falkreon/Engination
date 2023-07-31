package blue.endless.engination;

public interface EventCooldownable {
	public long engination_getEventCooldownStart();
	public void engination_setEventCooldownStart(long value);
	public void engination_triggerEventCooldown();
}
