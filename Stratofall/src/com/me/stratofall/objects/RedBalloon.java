package com.me.stratofall.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public class RedBalloon extends Balloon
{
	public RedBalloon(Player p, float x, float y)
	{
		this.player = p;
		TYPE = "red";
		
		balloonImage = new TextureRegion(atlas.findRegion("balloon_red"));
		setSize(balloonImage.getRegionWidth(), balloonImage.getRegionHeight());
		setPosition(x, y);
		setVelocity();
		loadEffects();
	}

	@Override
	void loadEffects()
	{
		effectSound = Stratofall.assets.get("sounds/effect/pop.ogg", Sound.class);
		
		//get the rain effect
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particles/effects/balloon_ring_red.p"), Gdx.files.internal("particles/effects"));
		trail_effect = new ParticleEffect();
		trail_effect.load(Gdx.files.internal("particles/effects/balloon_trail_red.p"), Gdx.files.internal("particles/effects"));
		trail_effect.start();
	}
}
