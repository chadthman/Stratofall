package com.me.stratofall.objects.clouds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public class LightningCloud extends Cloud
{
	private final Player player;
	
	public LightningCloud(Player p)
	{
		player = p;
		cloudImage = new TextureRegion(atlas.findRegion("cloud_lightning"));

		setSize(cloudImage.getRegionWidth(), cloudImage.getRegionHeight());
		
		//load sound effect
		//effectSound = Gdx.audio.newSound(Gdx.files.internal("sounds/effect/sound_lightning.wav")); //this sound is actually for lightning clouds, just temporary
		effectSound = Stratofall.assets.get("sounds/effect/sound_lightning.ogg", Sound.class);
		
		//give a random locaton on the x axis
		setRandomXPosition();
		
		
		//reset times
		max_reset_time = 15 * 1000000000f; //15 seconds
		min_reset_time = 3 * 1000000000f; //3 seconds
		setResetTime();
		
		//get the rain effect
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particles/effects/cloud_lightning.p"), Gdx.files.internal("particles/effects"));
		
		effect.start(); //start the effect
		
		VELOCITY_Y_MAX = 3f; //moves half the speed of a normal cloud
		VELOCITY_Y_MIN = 1.5f;
		setVelocity();
	}
	@Override
	public void act(float delta) 
	{
		velocity_multiplier = player.getVelocity();
		super.act(delta);
	}
	@Override
	public void checkCollisions()
	{
		if(getCollisionRect().overlaps(player.getCollisionLocation()) && !hasCollided) //if collides with player
		{
			hasCollided = true;
			player.setHitFlashRate(.15f); //flashes the player red for .5 seconds
			player.flash(10); //flash 6 times
			player.removeLife();
		}
	}
	
	@Override
	public Rectangle getCollisionRect()
	{
		return new Rectangle(getX() + 22 , getY() + 45, 215, 110); //these values are hardcoded based on the photoshop model of what looked like a good collision rectangle
	}
}

