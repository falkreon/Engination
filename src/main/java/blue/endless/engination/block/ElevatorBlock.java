package blue.endless.engination.block;

import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import blue.endless.engination.Engination;
import blue.endless.engination.EventCooldownable;

import java.util.Set;

import org.quiltmc.qsl.networking.api.PacketByteBufs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ElevatorBlock extends Block {
	public static final int MAX_DISTANCE = 32;
	public static final int TELEPORT_COOLDOWN_TICKS = 10;
	
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);
	public static final VoxelShape SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, 3/16f, 1);
	
	public ElevatorBlock() {
		super(Block.Settings.create().sounds(BlockSoundGroup.METAL).mapColor(DyeColor.WHITE).strength(1, 15).luminance((it)->8));
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(FACING, COLOR);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (player.isCreative() && stack.getItem() instanceof DyeItem dye) {
			DyeColor color = dye.getColor();
			world.setBlockState(pos, state.with(COLOR, color), Block.NOTIFY_ALL);
			world.addBlockBreakParticles(pos, state);
			return ActionResult.SUCCESS;
		}
		
		return ActionResult.PASS;
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) { //TODO Mixin a cooldown for players!
		if (world.isClient && entity instanceof ClientPlayerEntity player) {
			if (entity instanceof EventCooldownable eventCooldown) {
				long elapsed = world.getTime() - eventCooldown.engination_getEventCooldownStart();
				if (elapsed < TELEPORT_COOLDOWN_TICKS) {
					return;
				}
			}
			
			
			if (entity.getVelocity().y > 0) {
				//Activate UP!
				BlockPos cur = pos.up();
				BlockState curState = world.getBlockState(cur);
				int distance = 1;
				while(distance < MAX_DISTANCE && !curState.isOf(EnginationBlocks.ELEVATOR)) {
					cur = cur.up();
					curState = world.getBlockState(cur);
					distance++;
				}
				
				if (curState.isOf(EnginationBlocks.ELEVATOR)) {
					requestTeleport(world, cur, entity);
				}
			} else if (entity.isSneaking()) {
				//Activate DOWN!
				BlockPos cur = pos.down();
				BlockState curState = world.getBlockState(cur);
				int distance = 1;
				while(distance < MAX_DISTANCE && !curState.isOf(EnginationBlocks.ELEVATOR)) {
					cur = cur.down();
					curState = world.getBlockState(cur);
					distance++;
				}
				
				if (curState.isOf(EnginationBlocks.ELEVATOR)) {
					requestTeleport(world, cur, entity);
				}
			}
		}
	}
	
	public static void requestTeleport(World world, BlockPos pos, Entity entity) {
		if (entity instanceof EventCooldownable eventCooldown) {
			eventCooldown.engination_triggerEventCooldown();
		}
		
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBlockPos(pos);
		ClientPlayNetworking.send(Engination.TELEPORT_REQUEST_CHANNEL, buf);
		
		entity.setVelocity(0, 0, 0);
		entity.setSneaking(false);
	}
	
	public static void teleportEntity(World world, BlockPos pos, BlockState state, Entity entity) {
		double x = pos.getX() + 0.5;
		double y = pos.getY() + (3/16d);
		double z = pos.getZ() + 0.5;
		float yaw = 0;
		
		if (state.isOf(EnginationBlocks.ELEVATOR)) {
			yaw = state.get(FACING).asRotation();
		}
		
		if (entity instanceof ServerPlayerEntity player) {
			player.networkHandler.requestTeleport(x, y, z, yaw, 0, Set.of(MovementFlag.X, MovementFlag.Y, MovementFlag.Z, MovementFlag.Y_ROT));
		}
		
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.6f, 1.0f);
		world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.6f, 1.0f);
			
		if (world instanceof ServerWorld serverWorld) {
			serverWorld.spawnParticles(
					ParticleTypes.PORTAL, 
					x,
					y,
					z,
					32,
					0.25, 2, 0.25,
					1.0
				);
			
			serverWorld.spawnParticles(
					ParticleTypes.PORTAL, 
					entity.getX(),
					entity.getY(),
					entity.getZ(),
					32,
					0.25, 2, 0.25,
					1.0
				);
		}
	}
}
