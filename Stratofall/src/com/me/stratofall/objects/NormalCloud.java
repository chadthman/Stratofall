package com.me.stratofall.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

/**
 * 
 * @author Shane
 * The normal cloud class is just an image, has no effects. The player is able
 * to pass though without consequince or gain.
 */
public class NormalCloud extends Cloud
{
	public NormalCloud(Player p)
	{
		player = p;
		
		if(MathUtils.random(1) == 0)
			cloudImage = new TextureRegion(atlas.findRegion("cloud"));
		else
			cloudImage = new TextureRegion(atlas.findRegion("cloud2"));
		
		cloud = new Rectangle();
		cloud.width = cloudImage.getRegionWidth();
		cloud.height = cloudImage.getRegionHeight();
			
		//give a random locaton on the x axis
		cloud.x = random.nextInt(Stratofall.WIDTH) - (cloud.width)/2;
		cloud.y = -cloud.height;
		
		//reset times
		max_reset_time = 3 * 1000000000f; //3 seconds
		min_reset_time = 0f; //immediately
		setResetTime();
		setVelocity();
	}

	@Override
	public void checkCollisions()
	{
		if(getCollisionRect().overlaps(player.getCollisionLocation()) )//&& !hasCollided)
		{
			hasCollided = true;
			player.setFallRate(.25f, .0095f); //slows the player down to a speed of .25f at a rate of .025f per update.
		}
	}
	private Rectangle getCollisionRect()
	{
		return new Rectangle(cloud.x + 22 , cloud.y + 28, 215, 110); //these values are hardcoded based on the photoshop model of what looked like a good collision rectangle
	}
}
