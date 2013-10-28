package com.me.stratofall.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public class BlueBalloon extends Balloon
{

	public BlueBalloon(Player p, float x, float y)
	{
		this.player = p;
		
		value = 5; //coin value
		
		balloonImage = new TextureRegion(atlas.findRegion("balloon_blue"));
		balloon = new Rectangle();
		balloon.width = balloonImage.getRegionWidth();
		balloon.height = balloonImage.getRegionHeight();
		balloon.x = x;
		balloon.y = y;
		
		effectSound = Stratofall.assets.get("sounds/effect/pop.ogg", Sound.class);
		
		
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particles/effects/balloon_ring_blue.p"), Gdx.files.internal("particles/effects"));
		trail_effect = new ParticleEffect();
		trail_effect.load(Gdx.files.internal("particles/effects/balloon_trail_blue.p"), Gdx.files.internal("particles/effects"));
		trail_effect.start();
	}
}
