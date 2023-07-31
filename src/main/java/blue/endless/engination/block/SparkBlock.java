package blue.endless.engination.block;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import org.jetbrains.annotations.Nullable;

import blue.endless.engination.BehaviorLockable;
import blue.endless.engination.VoxelHelper;
import blue.endless.engination.block.entity.SparkBlockEntity;
import blue.endless.engination.client.PlayerLockedBehaviorManager;
import blue.endless.engination.client.SparklineLockedBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SparkBlock extends Block implements BlockEntityProvider {
	//6px x 6px centered cube
	public static final VoxelShape HITBOX = VoxelHelper.centeredCube(3);
	public static final int MAX_CHAINED_BEAMS = 40;
	
	public SparkBlock() {
		super(Block.Settings.create()
				.emissiveLighting((state, world, pos) -> true)
				.luminance((state) -> 15)
				);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		
		//Don't trigger for an entity that is already in a locked state
		if (entity instanceof BehaviorLockable lockable) {
			if (lockable.engination_getLockedBehavior() != null) return;
		}
		
		trigger(world, pos, state, entity);
	}
	
	public void trigger(World world, BlockPos pos, BlockState state, Entity entity) {
		if (world.isClient && entity instanceof PlayerEntity player) {
			Optional<BlockPos> next = world.getBlockEntity(pos, EnginationBlocks.SPARK_BLOCK_ENTITY).map(it -> it.next);
			if (next.isEmpty()) return;
			PlayerLockedBehaviorManager.lockPlayer(player, new SparklineLockedBehavior(), pos);
			
		} else if (!world.isClient && !(entity instanceof PlayerEntity)) {
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) return ActionResult.PASS;
		
		if (player.isCreative()) {
			ItemStack stack = player.getStackInHand(hand);
			if (stack.isEmpty()) return ActionResult.PASS;
			
			if (stack.getItem() instanceof DyeItem dye) {
				
				final int newColor = dye.getColor().getSignColor();
				setColorAndChain(world, pos, newColor);
				
				return ActionResult.SUCCESS;
			}
		}
		
		return ActionResult.PASS;
	}
	
	@Nullable
	private BlockPos setColorAndGetNext(World world, BlockPos pos, int color) {
		Optional<SparkBlockEntity> opt = world.getBlockEntity(pos, EnginationBlocks.SPARK_BLOCK_ENTITY);
		if (opt.isEmpty()) return null;
		
		SparkBlockEntity entity = opt.get();
		entity.color = color;
		entity.markDirty();
		BlockState state = world.getBlockState(pos);
		world.updateListeners(pos, state, state, NOTIFY_LISTENERS);
		return entity.next;
	}
	
	private void setColorAndChain(World world, BlockPos pos, int color) {
		Set<BlockPos> history = new HashSet<>();
		BlockPos cur = pos;
		
		while(cur != null && !history.contains(cur) && history.size() < MAX_CHAINED_BEAMS) {
			history.add(cur);
			cur = setColorAndGetNext(world, cur, color);
		}
	}
	
	@Override
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return HITBOX;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SparkBlockEntity(pos, state);
	}
}
