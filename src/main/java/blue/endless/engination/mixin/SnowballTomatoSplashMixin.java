package blue.endless.engination.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import blue.endless.engination.Engination;
import blue.endless.engination.item.TomatoItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

@Mixin(SnowballEntity.class)
public abstract class SnowballTomatoSplashMixin extends ThrownItemEntity {
	
	public SnowballTomatoSplashMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Shadow
	private ParticleEffect getParticleParameters() {
		return null;
	}
	
	@Inject(at = { @At("HEAD") }, method="handleStatus(B)V", cancellable = true)
	public void handleStatus(byte status, CallbackInfo ci) {
		if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
			if (getItem().getItem() instanceof TomatoItem) {
				ParticleEffect particleEffect = this.getParticleParameters();
				Random rnd = getWorld().getRandom();
				int particles = 64;
				
				//This is client-only
				@SuppressWarnings("resource")
				ParticlesMode particlesMode = MinecraftClient.getInstance().options.getParticles().getValue();
				if (particlesMode == ParticlesMode.DECREASED) {
					particles = 16;
				} else if (particlesMode == ParticlesMode.MINIMAL) {
					return;
				}
				
				Vec3d pos = this.getPos();
				this.getWorld().playSound(pos.x, pos.y, pos.z, Engination.SOUND_SQUISH, getSoundCategory(), 0.7f, 1.0f + (float) rnd.nextTriangular(0, 0.2), false);
				
				for(int i = 0; i < particles; ++i) {
					this.getWorld().addParticle(
							particleEffect,
							pos.x + rnd.nextTriangular(0, 0.5),
							pos.y + rnd.nextTriangular(0, 0.5),
							pos.z + rnd.nextTriangular(0, 0.5),
							rnd.nextGaussian() * 0.8, rnd.nextGaussian() * 0.8, rnd.nextGaussian() * 0.8);
				}
				
				ci.cancel();
			}
		}
	}
}
