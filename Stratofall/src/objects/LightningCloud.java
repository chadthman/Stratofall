package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
		//effectSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effect/sound_lightning.wav")); //this sound is actually for lightning clouds, just temporary
		effectSound = Stratofall.assets.get("sounds/effect/sound_lightning.wav", Sound.class);
		
		//give a random locaton on the x axis
		cloud.x = random.nextInt(Stratofall.WIDTH) - (cloud.width)/2;
		cloud.y = -cloud.height;
		
		//reset times
		max_reset_time = 15 * 1000000000f; //15 seconds
		min_reset_time = 3 * 1000000000f; //3 seconds
		setResetTime();
		
		//get the rain effect
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particles/effects/cloud_rain.p"), Gdx.files.internal("particles/effects"));
		effect.start(); //start the effect
		
		VELOCITY_Y_MAX = 3f; //moves half the speed of a normal cloud
		VELOCITY_Y_MIN = 1.5f;
		setVelocity();
	}

	@Override
	public void checkCollisions()
	{
		if(getLocation().overlaps(player.getCollisionLocation()) && !hasCollided) //if collides with player
		{
			hasCollided = true;
			player.removeLife();
		}
	}
}

