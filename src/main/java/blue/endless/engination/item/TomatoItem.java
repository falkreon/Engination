package blue.endless.engination.item;

import blue.endless.engination.Engination;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TomatoItem extends Item {
	private final boolean creative;
	
	public TomatoItem(boolean creative) {
		super(new Item.Settings().maxCount(16));
		this.creative = creative;
	}
	
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (!player.getAbilities().creativeMode && !creative) {
			itemStack.decrement(1);
		}
		
		Vec3d vec = player.getPos();
		world.playSound((PlayerEntity)null, vec.x, vec.y, vec.z, Engination.SOUND_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!world.isClient) {
			SnowballEntity entity = new SnowballEntity(world, player);
			entity.setItem(new ItemStack(EnginationItems.TOMATO));
			
			entity.setProperties(player, player.getPitch(), player.getYaw(), 0.0f, 1.5f, 1.0f);
			world.spawnEntity(entity);
		}
		
		player.incrementStat(Stats.USED.getOrCreateStat(this));
		return new TypedActionResult<>(ActionResult.SUCCESS, itemStack);
	}
	
	public static class DispenserBehavior extends ProjectileDispenserBehavior {
		private ItemConvertible item;
		
		public DispenserBehavior(ItemConvertible item) {
			this.item = item;
		}
		
		@Override
		protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
			SnowballEntity entity = new SnowballEntity(world, position.getX(), position.getY(), position.getZ());
			entity.setItem(new ItemStack(item));
			return entity;
		}
	}
	
	public static class CreativeDispenserBehavior extends DispenserBehavior {

		public CreativeDispenserBehavior(ItemConvertible item) {
			super(item);
		}
		
		@Override
		public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
			World world = pointer.getWorld();
			Position position = DispenserBlock.getOutputLocation(pointer);
			Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
			ProjectileEntity projectileEntity = this.createProjectile(world, position, stack);
			projectileEntity.setVelocity(
				direction.getOffsetX(), direction.getOffsetY() + 0.1, direction.getOffsetZ(), this.getForce(), this.getVariation()
			);
			world.spawnEntity(projectileEntity);
			return stack;
		}
	}
}