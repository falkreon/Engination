package com.elytradev.engination;

import com.elytradev.engination.block.EnginationBlocks;

import net.fabricmc.api.ModInitializer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Engination implements ModInitializer {
	public static SoundEvent SOUND_JUMP = new SoundEvent(new Identifier("engination", "launcher_activate"));
	public static SoundEvent SOUND_THROW = new SoundEvent(new Identifier("engination", "projectile_throw"));
	public static SoundEvent SOUND_SQUISH = new SoundEvent(new Identifier("engination", "squish"));
	
	@Override
	public void onInitialize() {
		Registry.register(Registry.SOUND_EVENT, "engination:launcher_activate", SOUND_JUMP);
		Registry.register(Registry.SOUND_EVENT, "engination:projectile_throw",  SOUND_THROW);
		Registry.register(Registry.SOUND_EVENT, "engination:squish",            SOUND_SQUISH);
		
		EnginationBlocks.init();
		
	}
}
