package com.me.stratofall.objects.balloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public class RedBalloon extends Balloon
{
	/**
	 * 
	 * @param p reference to the player
	 * @param x location
	 * @param y location
	 * @param ran Random starting velocity?
	 */
	public RedBalloon(Player p, TextureRegion texture, float x, float y, boolean ran, boolean allowReset)
	{
		this.player = p;
		setName("red");
		
		balloonImage = texture;
		setSize(balloonImage.getRegionWidth(), balloonImage.getRegionHeight());
		//reset times
		max_reset_time = 8 * 1000000000f; //3 seconds
		min_reset_time = 2f * 1000000000f; //immediately
		setPosition(x, y);
		
		if(ran)
			setVelocity();
		
		loadEffects();
		
		allowReset(allowReset);
	}

	@Override
	void loadEffects()
	{
		effectSound = Stratofall.assets.get("sounds/effect/pop.mp3", Sound.class);
		
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particles/effects/balloon_ring_red.p"), Gdx.files.internal("particles/effects"));
		trail_effect = new ParticleEffect();
		trail_effect.load(Gdx.files.internal("particles/effects/balloon_trail_red.p"), Gdx.files.internal("particles/effects"));
		trail_effect.start();
	}
}
