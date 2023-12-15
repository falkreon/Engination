package blue.endless.engination.client;

import org.joml.Matrix4f;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class RenderHelper {
	public static void cubeAround(MatrixStack matrices, VertexConsumer buf, int light, int overlay, double x, double y, double z, double r, int color) {
		Matrix4f transform = matrices.peek().getPositionMatrix();
		
		//+z face
		
		buf //bottomleft
			.vertex(transform, (float) (x-r), (float) (y-r), (float) (z+r))
			.color(color)
			.texture(0, 1)
			.overlay(overlay)
			.light(light)
			.normal(0, 0, 1)
			.next();
		
		buf //bottomright
			.vertex(transform, (float) (x+r), (float) (y-r), (float) (z+r))
			.color(color)
			.texture(1, 1)
			.overlay(overlay)
			.light(light)
			.normal(0, 0, 1)
			.next();
		
		buf //topright
			.vertex(transform, (float) (x+r), (float) (y+r), (float) (z+r))
			.color(color)
			.texture(1, 0)
			.overlay(overlay)
			.light(light)
			.normal(0, 0, 1)
			.next();
		
		buf //topleft
			.vertex(transform, (float) (x-r), (float) (y+r), (float) (z+r))
			.color(color)
			.texture(0, 0)
			.overlay(overlay)
			.light(light)
			.normal(0, 0, 1)
			.next();
		
		//+x face
		
		buf //bottomleft
			.vertex(transform, (float) (x+r), (float) (y-r), (float) (z+r))
			.color(color)
			.texture(0, 1)
			.overlay(overlay)
			.light(light)
			.normal(1, 0, 0)
			.next();
		
		buf //bottomright
			.vertex(transform, (float) (x+r), (float) (y-r), (float) (z-r))
			.color(color)
			.texture(1, 1)
			.overlay(overlay)
			.light(light)
			.normal(1, 0, 0)
			.next();
		
		buf //topright
			.vertex(transform, (float) (x+r), (float) (y+r), (float) (z-r))
			.color(color)
			.texture(1, 0)
			.overlay(overlay)
			.light(light)
			.normal(1, 0, 0)
			.next();
		
		buf //topleft
			.vertex(transform, (float) (x+r), (float) (y+r), (float) (z+r))
			.color(color)
			.texture(0, 0)
			.overlay(overlay)
			.light(light)
			.normal(1, 0, 0)
			.next();
		
		//-z face
		
		buf //bottomleft
			.vertex(transform, (float) (x+r), (float) (y-r), (float) (z-r))
			.color(color)
			.texture(0, 1)
			.overlay(overlay)
			.light(light)
			.normal(0, 0, -1)
			.next();
		
		buf //bottomright
			.vertex(transform, (float) (x-r), (float) (y-r), (float) (z-r))
			.color(color)
			.texture(1, 1)
			.overlay(overlay)
			.light(light)
			.normal(0, 0, -1)
			.next();
		
		buf //topright
			.vertex(transform, (float) (x-r), (float) (y+r), (float) (z-r))
			.color(color)
			.texture(1, 0)
			.overlay(overlay)
			.light(light)
			.normal(0, 0, -1)
			.next();
		
		buf //topleft
			.vertex(transform, (float) (x+r), (float) (y+r), (float) (z-r))
			.color(color)
			.texture(0, 0)
			.overlay(overlay)
			.light(light)
			.normal(0, 0, -1)
			.next();
		
		//-x face
		
		buf //bottomleft
			.vertex(transform, (float) (x-r), (float) (y-r), (float) (z-r))
			.color(color)
			.texture(0, 1)
			.overlay(overlay)
			.light(light)
			.normal(-1, 0, 0)
			.next();
		
		buf //bottomright
			.vertex(transform, (float) (x-r), (float) (y-r), (float) (z+r))
			.color(color)
			.texture(1, 1)
			.overlay(overlay)
			.light(light)
			.normal(-1, 0, 0)
			.next();
		
		buf //topright
			.vertex(transform, (float) (x-r), (float) (y+r), (float) (z+r))
			.color(color)
			.texture(1, 0)
			.overlay(overlay)
			.light(light)
			.normal(-1, 0, 0)
			.next();
		
		buf //topleft
			.vertex(transform, (float) (x-r), (float) (y+r), (float) (z-r))
			.color(color)
			.texture(0, 0)
			.overlay(overlay)
			.light(light)
			.normal(-1, 0, 0)
			.next();
		
		//+y face
		
		buf //bottomleft
			.vertex(transform, (float) (x-r), (float) (y+r), (float) (z+r))
			.color(color)
			.texture(0, 1)
			.overlay(overlay)
			.light(light)
			.normal(0, 1, 0)
			.next();
		
		buf //bottomright
			.vertex(transform, (float) (x+r), (float) (y+r), (float) (z+r))
			.color(color)
			.texture(1, 1)
			.overlay(overlay)
			.light(light)
			.normal(0, 1, 0)
			.next();
		
		buf //topright
			.vertex(transform, (float) (x+r), (float) (y+r), (float) (z-r))
			.color(color)
			.texture(1, 0)
			.overlay(overlay)
			.light(light)
			.normal(0, 1, 0)
			.next();
		
		buf //topleft
			.vertex(transform, (float) (x-r), (float) (y+r), (float) (z-r))
			.color(color)
			.texture(0, 0)
			.overlay(overlay)
			.light(light)
			.normal(0, 1, 0)
			.next();
		
		//-y face
		
		buf //bottomleft
			.vertex(transform, (float) (x+r), (float) (y-r), (float) (z+r))
			.color(color)
			.texture(0, 1)
			.overlay(overlay)
			.light(light)
			.normal(0, -1, 0)
			.next();
		
		buf //bottomright
			.vertex(transform, (float) (x-r), (float) (y-r), (float) (z+r))
			.color(color)
			.texture(1, 1)
			.overlay(overlay)
			.light(light)
			.normal(0, -1, 0)
			.next();
		
		buf //topright
			.vertex(transform, (float) (x-r), (float) (y-r), (float) (z-r))
			.color(color)
			.texture(1, 0)
			.overlay(overlay)
			.light(light)
			.normal(0, -1, 0)
			.next();
		
		buf //topleft
			.vertex(transform, (float) (x+r), (float) (y-r), (float) (z-r))
			.color(color)
			.texture(0, 0)
			.overlay(overlay)
			.light(light)
			.normal(0, -1, 0)
			.next();
	}
	
	public static void barLine(MatrixStack matrices, VertexConsumer buf, int light, int overlay, Vec3d a, Vec3d b, double thickness, int aColor, int bColor) {
		
		Matrix4f transform = matrices.peek().getPositionMatrix();
		
		double ht = thickness / 2.0;
		
		Vec3d forward = b.subtract(a).normalize();
		Vec3d up = (a.x == b.x && a.z == b.z) ?
				new Vec3d(0, 0, -1) :
				new Vec3d(0, 1, 0);
		
		Vec3d right = forward.crossProduct(up).normalize();
		up = right.crossProduct(forward);
		
		Vec3d halfRight = right.multiply(ht);
		Vec3d halfUp = up.multiply(ht);
		
		//+y face
		
		Vec3d curPoint = a.subtract(halfRight).add(halfUp);
		buf //bottomleft
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(aColor)
			.texture(0, 1)
			.overlay(overlay)
			.light(light)
			.normal((float) up.x, (float) up.y, (float) up.z)
			.next();
		
		curPoint = a.add(halfRight).add(halfUp);
		buf //bottomright
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(aColor)
			.texture(1, 1)
			.overlay(overlay)
			.light(light)
			.normal((float) up.x, (float) up.y, (float) up.z)
			.next();
		
		curPoint = b.add(halfRight).add(halfUp);
		buf //topright
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(bColor)
			.texture(1, 0)
			.overlay(overlay)
			.light(light)
			.normal((float) up.x, (float) up.y, (float) up.z)
			.next();
		
		curPoint = b.subtract(halfRight).add(halfUp);
		buf //topleft
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(bColor)
			.texture(0, 0)
			.overlay(overlay)
			.light(light)
			.normal((float) up.x, (float) up.y, (float) up.z)
			.next();
		
		//-y face
		
		curPoint = a.add(halfRight).subtract(halfUp);
		buf //bottomleft
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(aColor)
			.texture(0, 1)
			.overlay(overlay)
			.light(light)
			.normal((float) -up.x, (float) -up.y, (float) -up.z)
			.next();
		
		curPoint = a.subtract(halfRight).subtract(halfUp);
		buf //bottomright
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(aColor)
			.texture(1, 1)
			.overlay(overlay)
			.light(light)
			.normal((float) -up.x, (float) -up.y, (float) -up.z)
			.next();
		
		curPoint = b.subtract(halfRight).subtract(halfUp);
		buf //topright
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(bColor)
			.texture(1, 0)
			.overlay(overlay)
			.light(light)
			.normal((float) -up.x, (float) -up.y, (float) -up.z)
			.next();
		
		curPoint = b.add(halfRight).subtract(halfUp);
		buf //topleft
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(bColor)
			.texture(0, 0)
			.overlay(overlay)
			.light(light)
			.normal((float) -up.x, (float) -up.y, (float) -up.z)
			.next();
		
		//+right face
		
		curPoint = a.add(halfRight).subtract(halfUp);
		buf //bottomleft
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(aColor)
			.texture(0, 1)
			.overlay(overlay)
			.light(light)
			.normal((float) right.x, (float) right.y, (float) right.z)
			.next();
		
		curPoint = b.add(halfRight).subtract(halfUp);
		buf //bottomright
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(bColor)
			.texture(1, 1)
			.overlay(overlay)
			.light(light)
			.normal((float) right.x, (float) right.y, (float) right.z)
			.next();
		
		curPoint = b.add(halfRight).add(halfUp);
		buf //topright
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(bColor)
			.texture(1, 0)
			.overlay(overlay)
			.light(light)
			.normal((float) right.x, (float) right.y, (float) right.z)
			.next();
		
		curPoint = a.add(halfRight).add(halfUp);
		buf //topleft
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(aColor)
			.texture(0, 0)
			.overlay(overlay)
			.light(light)
			.normal((float) right.x, (float) right.y, (float) right.z)
			.next();
		
		//-right face
		
		curPoint = b.subtract(halfRight).subtract(halfUp);
		buf //bottomleft
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(bColor)
			.texture(0, 1)
			.overlay(overlay)
			.light(light)
			.normal((float) -right.x, (float) -right.y, (float) -right.z)
			.next();
		
		curPoint = a.subtract(halfRight).subtract(halfUp);
		buf //bottomright
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(aColor)
			.texture(1, 1)
			.overlay(overlay)
			.light(light)
			.normal((float) -right.x, (float) -right.y, (float) -right.z)
			.next();
		
		curPoint = a.subtract(halfRight).add(halfUp);
		buf //topright
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(aColor)
			.texture(1, 0)
			.overlay(overlay)
			.light(light)
			.normal((float) -right.x, (float) -right.y, (float) -right.z)
			.next();
		
		curPoint = b.subtract(halfRight).add(halfUp);
		buf //topleft
			.vertex(transform, (float) curPoint.x, (float) curPoint.y, (float) curPoint.z)
			.color(bColor)
			.texture(0, 0)
			.overlay(overlay)
			.light(light)
			.normal((float) -right.x, (float) -right.y, (float) -right.z)
			.next();
	}
}
