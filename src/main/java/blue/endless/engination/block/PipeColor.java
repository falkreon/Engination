package blue.endless.engination.block;

import java.util.Locale;

import net.minecraft.util.DyeColor;
import net.minecraft.util.StringIdentifiable;

public enum PipeColor implements StringIdentifiable {
	GREEN(DyeColor.GREEN),
	ORANGE(DyeColor.ORANGE),
	BLUE(DyeColor.BLUE),
	GRAY(DyeColor.GRAY),
	CLEAR(DyeColor.WHITE);
	
	private final DyeColor color;
	
	PipeColor(DyeColor color) {
		this.color = color;
	}
	
	public DyeColor getDyeColor() {
		return color;
	}
	
	@Override
	public String asString() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}