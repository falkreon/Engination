package blue.endless.engination;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;

public class MoreNbt {
	
	public static OptionalNbtCompound wrap(NbtCompound compound) {
		return new OptionalNbtCompound(compound);
	}
	
	
	public static interface OptionalNbt<T> {
		public boolean isPresent();
		public boolean isEmpty();
		@Nullable
		public T get();
	}
	
	/**
	 * CompoundAccess is kind of like an {@code Optional<NbtCompound>}
	 * 
	 * <p>It's used for easily diving into a complex NBT structure and getting the tag you need.
	 */
	public static class OptionalNbtCompound implements OptionalNbt<NbtCompound> {
		@Nullable
		private final NbtCompound value;
		
		public OptionalNbtCompound(NbtCompound value) {
			this.value = value;
		}
		
		@Override
		public boolean isPresent() {
			return value != null;
		}
		
		@Override
		public boolean isEmpty() {
			return value == null;
		}
		
		@Override
		@Nullable
		public NbtCompound get() {
			return value;
		}
		
		public void ifPresent(Consumer<NbtCompound> consumer) {
			if (value != null) consumer.accept(value);
		}
		
		public boolean containsKey(String key) {
			if (value == null) return false;
			return value.contains(key);
		}
		
		public OptionalNbtCompound getCompound(String key) {
			if (value == null) return this;
			if (value.contains(key, NbtElement.COMPOUND_TYPE)) {
				return new OptionalNbtCompound(value.getCompound(key));
			} else {
				return new OptionalNbtCompound(null);
			}
		}
		
		public OptionalInt getInt(String key) {
			if (value == null) return OptionalInt.empty();
			if (!value.contains(key, NbtElement.INT_TYPE)) return OptionalInt.empty();
			return OptionalInt.of(value.getInt(key));
		}
		
		public OptionalDouble getDouble(String key) {
			if (value == null) return OptionalDouble.empty();
			if (!value.contains(key, NbtElement.DOUBLE_TYPE)) return OptionalDouble.empty();
			return OptionalDouble.of(value.getDouble(key));
		}
		
		public Optional<Float> getFloat(String key) {
			if (value == null) return Optional.empty();
			if (!value.contains(key, NbtElement.FLOAT_TYPE)) return Optional.empty();
			return Optional.of(value.getFloat(key));
		}
		
		public OptionalLong getLong(String key) {
			if (value == null) return OptionalLong.empty();
			if (!value.contains(key, NbtElement.LONG_TYPE)) return OptionalLong.empty();
			return OptionalLong.of(value.getLong(key));
		}
		
		public Optional<String> getString(String key) {
			if (value == null) return Optional.empty();
			if (!value.contains(key, NbtElement.STRING_TYPE)) return Optional.empty();
			return Optional.of(value.getString(key));
		}
		
		public Optional<BlockPos> asBlockPos() {
			OptionalInt x = getInt("X");
			OptionalInt y = getInt("Y");
			OptionalInt z = getInt("Z");
			
			if (x.isPresent() && y.isPresent() && z.isPresent()) {
				return Optional.of(new BlockPos(x.getAsInt(), y.getAsInt(), z.getAsInt()));
			} else {
				return Optional.empty();
			}
		}
	}
	
	public static class OptionalNbtList implements OptionalNbt<NbtList> {
		private final NbtList value;
		
		public OptionalNbtList(NbtList value) {
			this.value = value;
		}
		
		@Override
		public boolean isPresent() {
			return value != null;
		}
		
		@Override
		public boolean isEmpty() {
			return value == null;
		}
		
		@Override
		@Nullable
		public NbtList get() {
			return value;
		}
		
		
	}
}
