package blue.endless.engination.block.entity;

import java.util.Locale;

import blue.endless.engination.MoreNbt;
import blue.endless.engination.block.EnginationBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtInt;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

public class SparkBlockEntity extends BlockEntity {
	public static final String PREVIOUS_KEY = "Previous";
	public static final String NEXT_KEY = "Next";
	public static final String COLOR_KEY = "Color";
	public static final int DEFAULT_COLOR = 0xFF_2ab8fa;
	
	private static final String HEX_DIGITS = "0123456789abcdef";
	
	public BlockPos previous;
	public BlockPos next;
	public int color = DEFAULT_COLOR;
	
	public SparkBlockEntity(BlockPos pos, BlockState state) {
		super(EnginationBlocks.SPARK_BLOCK_ENTITY, pos, state);
	}
	
	/* NBT schema
	 * 
	 * {
	 *   // IdentifyingData
	 *   "id": "engination:spark_block",
	 *   "x": 0,
	 *   "y": 0,
	 *   "z": 0
	 * 
	 *    Engination Data
	 *   "Previous": { "X": 0, "Y": 0, "Z": 0 },
	 *   "Next":     { "X": 0, "Y": 0, "Z": 0 },
	 *   "Color":    0 | "3fa" | "#3fa" | "c04dfe" | "#c0fdfe"
	 * }
	 */
	
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		var opt = MoreNbt.wrap(nbt);
		
		previous = opt.getCompound(PREVIOUS_KEY).asBlockPos().orElse(null);
		next = opt.getCompound(NEXT_KEY).asBlockPos().orElse(null);
		color = opt.getInt(COLOR_KEY).orElseGet(() -> {
			String code = opt.getString(COLOR_KEY).orElse("2ab8fa").toLowerCase(Locale.ROOT);
			if (code.startsWith("#")) code = code.substring(1);
			if (code.length() == 6) {
				try {
					int i = Integer.parseInt(code, 16);
					
					return i | 0xFF_000000;
				} catch (Throwable t) {
					return DEFAULT_COLOR;
				}
			} else if (code.length() == 3) {
				int r = HEX_DIGITS.indexOf(code.charAt(0));
				int g = HEX_DIGITS.indexOf(code.charAt(1));
				int b = HEX_DIGITS.indexOf(code.charAt(2));
				if (r==-1 || g==-1 || b==-1) return DEFAULT_COLOR;
				
				r = expandHex(r);
				g = expandHex(g);
				b = expandHex(b);
				
				return 0xFF_000000 |
						(r << 16) |
						(g << 8) |
						(b);
			} else {
				return DEFAULT_COLOR;
			}
		});
	}
	
	private int expandHex(int i) {
		if (i == -1) return -1;
		
		i = (i & 0xF);
		return i | (i << 4);
	}
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		if (previous != null) nbt.put(PREVIOUS_KEY, NbtHelper.fromBlockPos(previous));
		if (next != null) nbt.put(NEXT_KEY, NbtHelper.fromBlockPos(next));
		nbt.put(COLOR_KEY, NbtInt.of(color));
	}
	
	@Override
	public NbtCompound toSyncedNbt() {
		NbtCompound compound = new NbtCompound();
		//if (previous != null) compound.put(PREVIOUS_KEY, NbtHelper.fromBlockPos(previous));
		if (next != null) compound.put(NEXT_KEY, NbtHelper.fromBlockPos(next));
		compound.put(COLOR_KEY, NbtInt.of(color));
		
		return compound;
	}
	
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}
	
	
	
}
