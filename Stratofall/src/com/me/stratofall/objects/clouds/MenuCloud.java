package com.me.stratofall.objects.clouds;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * 
 * @author Shane
 * The normal cloud class is just an image, has no effects. The player is able
 * to pass though without consequence or gain.
 */
public class MenuCloud extends Cloud
{
	public MenuCloud()
	{
		super();
		if(MathUtils.random(1) == 0)
			cloudImage = new TextureRegion(atlas.findRegion("cloud"));
		else
			cloudImage = new TextureRegion(atlas.findRegion("cloud2"));

		setSize(cloudImage.getRegionWidth(), cloudImage.getRegionHeight());
		
		setRandomXPosition();
		
		//reset times
		max_reset_time = 3 * 1000000000f; //3 seconds
		min_reset_time = 0f; //immediately
		
		setResetTime();
		setVelocity();
	}

	@Override
	public void checkCollisions() {
	}

	@Override
	public Rectangle getCollisionRect() {
		return null;
	}
}
