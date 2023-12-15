package blue.endless.engination.client;

import org.joml.Vector3d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Lerp {
	public static double of(double a, double b, double t) {
		if (t < 0) t = 0;
		if (t > 1) t = 1;
		return (1-t) * a + t * b;
	}
	
	public static Vector3d of(BlockPos a, BlockPos b, double t) {
		final double x = of(a.getX() + 0.5, b.getX() + 0.5, t);
		final double y = of(a.getY() + 0.5, b.getY() + 0.5, t);
		final double z = of(a.getZ() + 0.5, b.getZ() + 0.5, t);
		
		return new Vector3d(x,y,z);
	}
	
	@Environment(EnvType.CLIENT)
	public static Vector3d of(Vector3d a, Vector3d b, double t) {
		return new Vector3d(
				of(a.x, b.x, t),
				of(a.y, b.y, t),
				of(a.z, b.z, t)
				);
	}
	
	@Environment(EnvType.CLIENT)
	public static Vec3d of(Vec3d a, Vec3d b, double t) {
		return new Vec3d(
				of(a.x, b.x, t),
				of(a.y, b.y, t),
				of(a.z, b.z, t)
				);
	}
}
