package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public class LightningCloud extends Cloud
{
	public LightningCloud(Player p)
	{
		player = p;
		cloudImage = new TextureRegion(atlas.findRegion("cloud_lightning"));
		cloud = new Rectangle();
		cloud.width = cloudImage.getRegionWidth();
		cloud.height = cloudImage.getRegionHeight();
		
		//load sound effect
		effectSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effect/sound_lightning.wav")); //this sound is actually for lightning clouds, just temporary
		
		//give a random locaton on the x axis
		cloud.x = random.nextInt(Stratofall.WIDTH) - (cloud.width)/2;
		cloud.y = -cloud.height;
		
		//reset times
		max_reset_time = 15 * 1000000000f; //6 seconds
		min_reset_time = 3 * 1000000000f; //3 seconds
		setResetTime();
		
		VELOCITY_Y = 2.5f; //moves half the speed of a normal cloud
	}

	@Override
	public void checkCollisions()
	{
		
	}
}
