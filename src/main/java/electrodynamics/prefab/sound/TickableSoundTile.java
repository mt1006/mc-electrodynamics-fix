package electrodynamics.prefab.sound;

import electrodynamics.prefab.sound.utils.ITickableSoundTile;
import electrodynamics.prefab.utilities.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class TickableSoundTile extends AbstractTickableSoundInstance {

	// Yes it's weird, but I couldn't think of a better way
	private static final double MAXIMUM_DISTANCE = 10;

	protected final ITickableSoundTile tile;
	private final float initialVolume;

	public TickableSoundTile(SoundEvent event, ITickableSoundTile tile, boolean repeat) {
		this(event, SoundSource.BLOCKS, tile, 0.5F, 1.0F, repeat);
	}

	public TickableSoundTile(SoundEvent event, SoundSource source, ITickableSoundTile tile, float volume, float pitch, boolean repeat) {
		super(event, source, SoundInstance.createUnseededRandom());
		this.tile = tile;
		this.volume = 0.5F;
		this.pitch = 1.0F;
		x = tile.getBlockPos().getX();
		y = tile.getBlockPos().getY();
		z = tile.getBlockPos().getZ();
		looping = repeat;
		initialVolume = volume;
	}

	@Override
	public void tick() {
		if (!tile.shouldPlaySound() || tile.isRemoved()) {
			stop();
		}
		Player player = Minecraft.getInstance().player;
		double distance = WorldUtils.distanceBetweenPositions(player.blockPosition(), tile.getBlockPos());
		if (distance > 0 && distance <= MAXIMUM_DISTANCE) {
			volume = (float) (initialVolume / distance);
		} else if (distance > MAXIMUM_DISTANCE) {
			volume = 0;
		} else {
			volume = initialVolume;
		}
	}

	@Override
	public void stop() {
		super.stop();
		tile.setNotPlaying();
	}

	public void stopAbstract() {
		super.stop();
	}

}
