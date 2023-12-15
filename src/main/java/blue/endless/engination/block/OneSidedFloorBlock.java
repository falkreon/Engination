package blue.endless.engination.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class OneSidedFloorBlock extends Block {
	public static final VoxelShape ABOVE = VoxelShapes.cuboid(0, 9/16f, 0, 1, 1, 1);
	public static final VoxelShape BELOW = VoxelShapes.empty();
	
	
	public OneSidedFloorBlock(Settings settings) {
		super(settings.suffocates((state, view, pos) -> false));
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return context.isAbove(ABOVE, pos, false) ? ABOVE : BELOW;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return ABOVE;
	}
	
	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}
}
