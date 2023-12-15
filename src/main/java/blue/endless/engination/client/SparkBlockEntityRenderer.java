package blue.endless.engination.client;

import blue.endless.engination.block.entity.SparkBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

public class SparkBlockEntityRenderer implements BlockEntityRenderer<SparkBlockEntity> {
	private static final Identifier BEAM_TEXTURE = new Identifier("engination", "textures/effect/beam.png");
	private static final double SPARK_TRAVEL_TIME = 100.0;

	public SparkBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
	}
	
	@Override
	public boolean rendersOutsideBoundingBox(SparkBlockEntity blockEntity) {
		return blockEntity.next != null;
	}
	
	@Override
	public void render(SparkBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		
		double laserThrob =  Math.sin((entity.getWorld().getTime() + tickDelta) / 16.0) * 0.03;
		double endThrob = -0.03;
		
		double prog =( (entity.getWorld().getTime() + tickDelta) % SPARK_TRAVEL_TIME) / SPARK_TRAVEL_TIME;
		
		BlockPos pos = entity.getPos();
		int x1 = pos.getX();
		int y1 = pos.getY();
		int z1 = pos.getZ();
		
		matrices.push();
		matrices.translate(0.5, 0.5, 0.5);
		
		VertexConsumer buf = vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE, true));
		VertexConsumer solidIsh = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(BEAM_TEXTURE));
		
		BlockPos pos2 = entity.next;
		if (pos2 != null) {
			endThrob = Math.cos((entity.getWorld().getTime() + tickDelta) / 10) * 0.03;
			
			Vector3d p1 = new Vector3d(x1 + 0.5, y1 + 0.5, z1 + 0.5);
			Vector3d p2 = new Vector3d(pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5);
			Vector3d delta = p2.sub(p1, new Vector3d());
			
			Vector3d lerp = Lerp.of(new Vector3d(), delta, prog);
			RenderHelper.cubeAround(matrices, buf, light, overlay, lerp.x, lerp.y, lerp.z, 1/12f, entity.color & 0xFFFFFF | 0x55_000000);
			
			//Draw a beam from pos1 to pos2. Requires two draws on different layers to get the white-hot core that is more opaque and the colored translucent exterior
			RenderHelper.barLine(matrices, solidIsh, light, overlay, Vec3d.ZERO, new Vec3d(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z), 0.062, 0xFF_FFFFFF, 0x77_FFFFFF);
			RenderHelper.barLine(matrices, buf, light, overlay, Vec3d.ZERO, new Vec3d(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z), 0.125 + laserThrob, entity.color & 0x00_FFFFFF | 0x66_000000, entity.color & 0x00_FFFFFF | 0x33_000000);
		}
		
		RenderHelper.cubeAround(matrices, buf, light, overlay, 0, 0, 0, 0.12 + endThrob, entity.color & 0xFFFFFF | 0x55_000000);
		
		matrices.pop();
	}
	
}
