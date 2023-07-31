package blue.endless.engination.item;

import java.util.Optional;

import blue.endless.engination.block.EnginationBlocks;
import blue.endless.engination.block.entity.SparkBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class SparklineTool extends Item {
	public static final String SPARKLING_KEY = "Sparkling";
	public static final String PREVIOUS_KEY = SparkBlockEntity.PREVIOUS_KEY;
	public static final String NEXT_KEY = SparkBlockEntity.NEXT_KEY;
	
	public SparklineTool() {
		super(new Item.Settings());
	}
	
	@SuppressWarnings("resource")
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getWorld().isClient) {
			return ActionResult.SUCCESS;
		}
		
		if (context.getPlayer().isSneaking()) {
			NbtCompound tag = context.getPlayer().getStackInHand(context.getHand()).getOrCreateNbt();
			tag.putBoolean(SPARKLING_KEY, false);
			tag.remove(PREVIOUS_KEY);
			
			return ActionResult.SUCCESS;
		}
		
		ServerWorld world = (ServerWorld) context.getWorld();
		
		BlockState blockStateClicked = world.getBlockState(context.getBlockPos());
		if (blockStateClicked.isOf(EnginationBlocks.SPARK_BLOCK)) {
			ItemStack tool = context.getStack().copy();
			NbtCompound tag = tool.getOrCreateNbt();
			if (tag.contains(PREVIOUS_KEY, NbtElement.COMPOUND_TYPE)) {
				// We're in sparkling mode, and trying to patch this beam into an existing line
				
				BlockPos previousPos = NbtHelper.toBlockPos(tag.getCompound(PREVIOUS_KEY));
				Optional<SparkBlockEntity> be = world.getBlockEntity(previousPos, EnginationBlocks.SPARK_BLOCK_ENTITY);
				if (be.isPresent()) {
					be.get().next = context.getBlockPos();
					be.get().markDirty();
					world.updateListeners(previousPos, EnginationBlocks.SPARK_BLOCK.getDefaultState(), EnginationBlocks.SPARK_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
					
					tag.putBoolean(SPARKLING_KEY, false);
					tag.remove(PREVIOUS_KEY);
					tool.setNbt(tag);
					context.getPlayer().setStackInHand(context.getHand(), tool);
					return ActionResult.SUCCESS;
					
				} else {
					Vec3d hit = context.getHitPos();
					world.playSound(null, new BlockPos((int) hit.x, (int) hit.y, (int) hit.z), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS);
					return ActionResult.FAIL;
				}
			} else {
				//We're not in sparkling mode, and trying to pick up the end of an existing line to route it further
				
				Optional<SparkBlockEntity> be = world.getBlockEntity(context.getBlockPos(), EnginationBlocks.SPARK_BLOCK_ENTITY);
				if (be.isPresent()) {
					BlockPos next = be.get().next;
					if (next != null) {
						BlockState nextState = world.getBlockState(next);
						if (nextState.isOf(EnginationBlocks.SPARK_BLOCK)) {
						
							//Fizzle because we can't branch out from the middle of a beam
							Vec3d hit = context.getHitPos();
							world.playSound(null, new BlockPos((int) hit.x, (int) hit.y, (int) hit.z), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS);
							return ActionResult.FAIL;
						} else {
							//We encountered a broken link! Clear the broken data and continue
							be.get().next = null;
							be.get().markDirty();
							world.updateListeners(context.getBlockPos(), EnginationBlocks.SPARK_BLOCK.getDefaultState(), EnginationBlocks.SPARK_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
						}
					}
					
					tag.putBoolean(SPARKLING_KEY, true);
					tag.put(PREVIOUS_KEY, NbtHelper.fromBlockPos(context.getBlockPos()));
					tool.setNbt(tag);
					context.getPlayer().setStackInHand(context.getHand(), tool);
					return ActionResult.SUCCESS;
				} else {
					Vec3d hit = context.getHitPos();
					world.playSound(null, new BlockPos((int) hit.x, (int) hit.y, (int) hit.z), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS);
					return ActionResult.FAIL;
				}
			}
		}
		
		Vec3d hit = context.getHitPos();
		BlockPos proposedLocation = context.getBlockPos().offset(context.getSide());
		
		BlockState existing = world.getBlockState(proposedLocation);
		
		if (existing.isOf(EnginationBlocks.SPARK_BLOCK)) {
			//If we rightclicked a spark block, or if the location we indicated is already a spark block, fail.
			world.playSound(null, new BlockPos((int) hit.x, (int) hit.y, (int) hit.z), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS);
			return ActionResult.FAIL;
		}
		
		if (!existing.canReplace(new ItemPlacementContext(context))) {
			world.playSound(null, new BlockPos((int) hit.x, (int) hit.y, (int) hit.z), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS);
			return ActionResult.FAIL;
		}
		
		ItemStack tool = context.getStack().copy();
		NbtCompound tag = tool.getOrCreateNbt();
		
		// We're all set to replace the block in the proposed location. Do so.
		world.setBlockState(proposedLocation, EnginationBlocks.SPARK_BLOCK.getDefaultState());
		
		Optional<SparkBlockEntity> be = world.getBlockEntity(proposedLocation, EnginationBlocks.SPARK_BLOCK_ENTITY);
		
		if (be.isPresent() && tag.contains(PREVIOUS_KEY, NbtElement.COMPOUND_TYPE)) {
			
			BlockPos previousPos = NbtHelper.toBlockPos(tag.getCompound(PREVIOUS_KEY));
			Optional<SparkBlockEntity> prevBe = world.getBlockEntity(previousPos, EnginationBlocks.SPARK_BLOCK_ENTITY);
			if (prevBe.isPresent()) {
				
				prevBe.get().next = proposedLocation;
				prevBe.get().markDirty();
				world.updateListeners(proposedLocation, existing, EnginationBlocks.SPARK_BLOCK.getDefaultState(), Block.NOTIFY_LISTENERS);
			}
			
			be.get().previous = previousPos;
			be.get().markDirty();
			
			BlockState prevPosState = world.getBlockState(previousPos);
			world.updateListeners(previousPos, prevPosState, prevPosState, Block.NOTIFY_LISTENERS);
			
			tag.putBoolean(SPARKLING_KEY, true);
			tag.put(PREVIOUS_KEY, NbtHelper.fromBlockPos(proposedLocation));
			tool.setNbt(tag);
			context.getPlayer().setStackInHand(context.getHand(), tool);
		} else {
			
			tag.putBoolean(SPARKLING_KEY, true);
			tag.put(PREVIOUS_KEY, NbtHelper.fromBlockPos(proposedLocation));
			tool.setNbt(tag);
			context.getPlayer().setStackInHand(context.getHand(), tool);
		}
		
		world.spawnParticles(
				ParticleTypes.ENCHANT, 
				proposedLocation.getX() + 0.5, proposedLocation.getY() + 0.5, proposedLocation.getZ() + 0.5,
				36,
				0.25, 0.25, 0.25,
				1.0
			);
		world.spawnParticles(
				ParticleTypes.FIREWORK,
				proposedLocation.getX() + 0.5, proposedLocation.getY() + 0.5, proposedLocation.getZ() + 0.5,
				36,
				0.25, 0.25, 0.25,
				1.5
			);
		world.playSound(null, new BlockPos((int) hit.x, (int) hit.y, (int) hit.z), SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS);
		return ActionResult.SUCCESS;
	}
	
	
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.NONE;
	}
}
