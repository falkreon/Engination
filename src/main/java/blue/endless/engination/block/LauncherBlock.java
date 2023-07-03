package blue.endless.engination.block;

import blue.endless.engination.Engination;
import blue.endless.engination.SoundEventDebouncer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LauncherBlock extends PressureTriggeredBlock {
	protected double force = 2.0;
	
	public LauncherBlock(double force) {
		super(Settings.create().sounds(BlockSoundGroup.METAL).mapColor(DyeColor.WHITE).strength(1, 15));
		this.force = force;
	}
	
	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		entity.fallDistance = 0; //both sides, all entities
		super.onLandedUpon(world, state, pos, entity, 0);
	}
	
	@Override
	public void trigger(World world, BlockPos pos, LivingEntity entity) {
		double newY = adjustScalar(entity.getVelocity().y, force*0.6);
		if (newY!=entity.getVelocity().y) {
			entity.setVelocity(entity.getVelocity().x, newY, entity.getVelocity().z);
			
			//world.playSound(entity.x, entity.y, entity.z, Engination.SOUND_LAUNCH, net.minecraft.sound.SoundCategory.PLAYER, 0.5F, world.getRandom().nextFloat() * 0.4F + 1.0F);
			Vec3d vec = entity.getPos();
			
			SoundEventDebouncer.play(entity, world, pos, Engination.SOUND_JUMP);
			//world.playSound(vec.x, vec.y, vec.z, Engination.SOUND_JUMP, SoundCategory.PLAYERS, 0.5f, world.getRandom().nextFloat() * 0.4F + 1.0F, true);
		}
	}
	
	private double adjustScalar(double in, double floor) {
		if (floor<0) {
			return (floor < in) ? floor : in;
		} else if (floor>0) {
			return (floor > in) ? floor : in;
		}
		
		return in;
	}
}
