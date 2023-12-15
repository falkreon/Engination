package blue.endless.engination.block;

import blue.endless.engination.Engination;
import blue.endless.engination.SoundEventDebouncer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.tick.TickPriority;

public class ItemBox extends VentralPressureTriggeredBlock {
	
	public static final double KICK_DEFAULT = 0.2;
	public static final double KICK_DOUBLE = 0.4;
	
	public static final BooleanProperty DEPLETED = BooleanProperty.of("depleted");
	
	private int reappearDelay = 20 * 5;
	
	public ItemBox() {
		super(Block.Settings.create()
				.sounds(BlockSoundGroup.DEEPSLATE)
				.mapColor(DyeColor.ORANGE)
				.hardness(1)
				.resistance(15)
				);
		
		this.setDefaultState(this.stateManager.getDefaultState().with(DEPLETED, false));
	}
	
	public ItemBox(Block.Settings settings) {
		super(settings);
		
		this.setDefaultState(this.stateManager.getDefaultState().with(DEPLETED, false));
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(DEPLETED);
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.setBlockState(pos, state.with(DEPLETED, false), Block.NOTIFY_ALL);
	}
	
	@Override
	public void onPressureActivate(Entity entity, BlockState state, World world, BlockPos pos) {
		if (!state.get(DEPLETED)) {
			world.setBlockState(pos, state.with(DEPLETED, true), Block.NOTIFY_ALL);
			world.syncWorldEvent(null, WorldEvents.BLOCK_BROKEN, pos, getRawIdFromState(state));
			world.scheduleBlockTick(pos, this, reappearDelay, TickPriority.LOW);
			dispense(state, world, pos);
		}
		SoundEventDebouncer.play(entity, world, pos, Engination.SOUND_ITEM_BOX);
	}
	
	public void setReappearDelay(int delay) {
		this.reappearDelay = delay;
	}
	
	/**
	 * Dispenses whatever is in the question block
	 */
	public void dispense(BlockState state, World world, BlockPos pos) {
	}
	
	/**
	 * Helper method to dispense an item from the block with abnormal speed, but respecting vanilla constraints.
	 * @param world The world the block is in.
	 * @param pos   The position of the block.
	 * @param stack The ItemStack to dispense.
	 * @param kickStrength How violently to dispense the item. 0.2 is default.
	 */
	public void dispenseItem(World world, BlockPos pos, ItemStack stack, double kickStrength) {
		if (world.isClient || stack.isEmpty()) return;
		
		double halfHeight = (double)EntityType.ITEM.getHeight() / 2.0;
		double x = (double)pos.getX() + 0.5 + MathHelper.nextDouble(world.random, -0.25, 0.25);
		double y = (double)pos.getY() + 0.5 + MathHelper.nextDouble(world.random, -0.25, 0.25) - halfHeight;
		double z = (double)pos.getZ() + 0.5 + MathHelper.nextDouble(world.random, -0.25, 0.25);
		double doubleKickStrength = kickStrength * 2;
		double vx = world.random.nextDouble() * doubleKickStrength - kickStrength;
		double vy = doubleKickStrength;
		double vz = world.random.nextDouble() * doubleKickStrength - kickStrength;
		
		ItemEntity itemEntity = new ItemEntity(world, x, y, z, stack);
		
		itemEntity.setToDefaultPickupDelay();
		itemEntity.setVelocity(vx, vy, vz);
		
		world.spawnEntity(itemEntity);
	}
}
