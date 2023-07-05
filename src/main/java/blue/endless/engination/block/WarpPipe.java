package blue.endless.engination.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

/**
 * This block represents connective tissue to make extremely-local pipe connections easier
 */
public class WarpPipe extends Block {
	
	public static final EnumProperty<AxisOrNode> AXIS_OR_NODE = EnumProperty.of("axis_or_node", AxisOrNode.class);

	private final PipeColor pipeColor;
	private boolean isClear = false;
	
	public WarpPipe(PipeColor color, BlockSoundGroup sounds) {
		super(Block.Settings.copy(Blocks.IRON_BLOCK)
				.mapColor(color.getDyeColor())
				.sounds(sounds)
				.nonOpaque()
				.suffocates((state, view, pos) -> false)
				);
		
		this.pipeColor = color;
		this.setDefaultState(this.stateManager.getDefaultState().with(AXIS_OR_NODE, AxisOrNode.NODE));
	}
	
	public WarpPipe(Block.Settings settings, PipeColor color) {
		super(settings);
		
		this.pipeColor = color;
		this.setDefaultState(this.stateManager.getDefaultState().with(AXIS_OR_NODE, AxisOrNode.NODE));
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(AXIS_OR_NODE);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState base =  this.getDefaultState().with(AXIS_OR_NODE, AxisOrNode.of(ctx.getSide().getAxis()));
		return getConnectedState(ctx.getWorld(), ctx.getBlockPos(), base);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return getConnectedState(world, pos, state);
	}
	
	public BlockState getConnectedState(WorldAccess world, BlockPos pos, BlockState initial) {
		Block ub = world.getBlockState(pos.up()).getBlock();
		Block db = world.getBlockState(pos.down()).getBlock();
		Block nb = world.getBlockState(pos.north()).getBlock();
		Block sb = world.getBlockState(pos.south()).getBlock();
		Block eb = world.getBlockState(pos.east()).getBlock();
		Block wb = world.getBlockState(pos.west()).getBlock();
		
		
		boolean u = ub instanceof WarpPipe || ub instanceof WarpPipeEntrance;
		boolean d = db instanceof WarpPipe || db instanceof WarpPipeEntrance;
		boolean n = nb instanceof WarpPipe || nb instanceof WarpPipeEntrance;
		boolean s = sb instanceof WarpPipe || sb instanceof WarpPipeEntrance;
		boolean e = eb instanceof WarpPipe || eb instanceof WarpPipeEntrance;
		boolean w = wb instanceof WarpPipe || wb instanceof WarpPipeEntrance;
		
		boolean x = e | w;
		boolean y = u | d;
		boolean z = n | s;
		
		if ((x && y) || (x && z) || (y && z)) return initial.with(AXIS_OR_NODE, AxisOrNode.NODE);
		
		if (x) return initial.with(AXIS_OR_NODE, AxisOrNode.X);
		if (y) return initial.with(AXIS_OR_NODE, AxisOrNode.Y);
		if (z) return initial.with(AXIS_OR_NODE, AxisOrNode.Z);
		
		return initial;
	}
	
	@Override
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return (isClear) ? VoxelShapes.empty() : VoxelShapes.fullCube();
	}
	
	public PipeColor getPipeColor() {
		return pipeColor;
	}
	
	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}
	
	@Override
	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		return 0;
	}

	public void setClear() {
		isClear = true;
	}
}
