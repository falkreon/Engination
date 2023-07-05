package blue.endless.engination.block;

import blue.endless.engination.VoxelHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class WarpPipeEntrance extends Block implements BlockEntityProvider {
	
	public static final EnumProperty<PipeColor> COLOR = EnumProperty.of("color", PipeColor.class);
	public static final DirectionProperty FACING = Properties.FACING;
	
	private VoxelShape SHAPE_UP = VoxelShapes.union(
			VoxelShapes.cuboid(0, 0, 0, 1, 5/16f, 1),
			VoxelShapes.cuboid(-2/16f, 5/16f, -2/16f, 18/16f, 15/16f, 18/16f)
			);
	
	private VoxelShape SHAPE_DOWN  = VoxelHelper.rotate(SHAPE_UP, 180,   0, 0);
	private VoxelShape SHAPE_NORTH = VoxelHelper.rotate(SHAPE_UP,  90, 180, 0);
	private VoxelShape SHAPE_EAST  = VoxelHelper.rotate(SHAPE_UP,  90, 270, 0);
	private VoxelShape SHAPE_SOUTH = VoxelHelper.rotate(SHAPE_UP,  90,   0, 0);
	private VoxelShape SHAPE_WEST  = VoxelHelper.rotate(SHAPE_UP,  90,  90, 0);
	
	private VoxelShape COLLISION_UP    = VoxelShapes.cuboid(0, 0, 0, 1, 15/16f, 1);
	private VoxelShape COLLISION_DOWN  = VoxelHelper.rotate(COLLISION_UP, 180,   0, 0);
	private VoxelShape COLLISION_NORTH = VoxelHelper.rotate(COLLISION_UP,  90, 180, 0);
	private VoxelShape COLLISION_EAST  = VoxelHelper.rotate(COLLISION_UP,  90, 270, 0);
	private VoxelShape COLLISION_SOUTH = VoxelHelper.rotate(COLLISION_UP,  90,   0, 0);
	private VoxelShape COLLISION_WEST  = VoxelHelper.rotate(COLLISION_UP,  90,  90, 0);
	
	public WarpPipeEntrance() {
		super(Block.Settings.copy(Blocks.IRON_BLOCK)
				.sounds(BlockSoundGroup.COPPER)
				.mapColor(DyeColor.YELLOW)
				.nonOpaque()
				);
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FACING, COLOR);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState base = this.getDefaultState().with(FACING, ctx.getSide());
		
		return getUpdatedState(ctx.getWorld(), ctx.getBlockPos(), base);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return getUpdatedState(world, pos, state);
	}
	
	public BlockState getUpdatedState(WorldAccess world, BlockPos pos, BlockState oldState) {
		//Are we already pointed away from a valid pipe?
		BlockPos presumptiveNeighbor = pos.offset(oldState.get(FACING).getOpposite());
		if (world.getBlockState(presumptiveNeighbor).getBlock() instanceof WarpPipe pipe) {
			//Fix up the color if needed, and bail
			
			return oldState.with(COLOR, pipe.getPipeColor());
		}
		
		//Find a pipe and orient against it
		
		for(Direction d : Direction.values()) {
			BlockPos neighborPos = pos.offset(d);
			BlockState neighbor = world.getBlockState(neighborPos);
			if (neighbor.getBlock() instanceof WarpPipe pipe) {
				//Orient against this neighbor and take on its color
				return oldState.with(FACING, d.getOpposite()).with(COLOR, pipe.getPipeColor());
			}
		}
		
		return oldState;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (!state.isOf(this)) return VoxelShapes.fullCube();
		
		return switch(state.get(FACING)) {
			case UP    -> SHAPE_UP;
			case DOWN  -> SHAPE_DOWN;
			case NORTH -> SHAPE_NORTH;
			case EAST  -> SHAPE_EAST;
			case SOUTH -> SHAPE_SOUTH;
			case WEST  -> SHAPE_WEST;
			default    -> VoxelShapes.fullCube();
		};
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch(state.get(FACING)) {
			case UP    -> COLLISION_UP;
			case DOWN  -> COLLISION_DOWN;
			case NORTH -> COLLISION_NORTH;
			case SOUTH -> COLLISION_SOUTH;
			case EAST  -> COLLISION_EAST;
			case WEST  -> COLLISION_WEST;
			
			default -> VoxelShapes.fullCube();
		};
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		//Don't ever trigger for a player just standing on top
		if (state.get(FACING) == Direction.UP && entity instanceof LivingEntity && !entity.isSneaking()) return;
		
		trigger(world, pos, state, entity);
	}
	
	public void trigger(World world, BlockPos pos, BlockState state, Entity entity) {
		if (world.isClient && entity instanceof PlayerEntity player) {
			System.out.println("Triggered for player!");
		} else if (!world.isClient && !(entity instanceof PlayerEntity)) {
			System.out.println("Triggered for nonplayer entity!");
		}
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		
		return null;
	}
}
