package blue.endless.engination.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class VentralPressureTriggeredBlock extends Block {
	
	//This is the opposite of path or soulsand; you can enter the block space 1 pixel from below
	private static VoxelShape ALMOST_FULL_CUBE = VoxelShapes.cuboid(0, 1/16.0, 0, 1, 1, 1);
	
	public VentralPressureTriggeredBlock(Block.Settings settings) {
		super(settings);
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState var1, LootContextParameterSet.Builder var2) {
		@SuppressWarnings("deprecation")
		List<ItemStack> superStacks = super.getDroppedStacks(var1, var2);
		if (!superStacks.isEmpty()) return superStacks;
		
		List<ItemStack> result = new ArrayList<>();
		result.add(new ItemStack(this));
		return result;
	}
	
	public void onPressureActivate(Entity entity, BlockState state, World world, BlockPos pos) {
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient()) onPressureActivate(entity, state, world, pos);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return ALMOST_FULL_CUBE;
	}
}
