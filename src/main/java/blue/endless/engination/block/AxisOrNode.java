package blue.endless.engination.block;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

public enum AxisOrNode implements StringIdentifiable {
	NODE("node", Direction.Axis.Y),
	X("x", Direction.Axis.X),
	Y("y", Direction.Axis.Y),
	Z("z", Direction.Axis.Z),
	;
	
	private final Direction.Axis axis;
	private final String name;
	
	AxisOrNode(String name, Direction.Axis axis) {
		this.axis = axis;
		this.name = name;
	}
	
	public Direction.Axis getAxis() {
		return axis;
	}
	
	public boolean isAxis() {
		return this != NODE;
	}
	
	public boolean isNode() {
		return this == NODE;
	}

	@Override
	public String asString() {
		return this.name;
	}
	
	public static AxisOrNode of(Direction.Axis axis) {
		return switch(axis) {
		case X -> X;
		case Y -> Y;
		case Z -> Z;
		};
	}
}
