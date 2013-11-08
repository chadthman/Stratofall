package com.me.stratofall.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.me.stratofall.Player;
import com.me.stratofall.Player.PlayerState;

/**
 * 
 * @author Shane
 * The normal cloud class is just an image, has no effects. The player is able
 * to pass though without consequence or gain.
 */
public class NormalCloud extends Cloud
{
	private Player player;
	public NormalCloud(Player p)
	{
		super();
		
		player = p;
		
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
	public void act(float delta) 
	{
		velocity_multiplier = player.getVelocity();
		super.act(delta);
	}
	@Override
	public void checkCollisions()
	{
		if(getCollisionRect().overlaps(player.getCollisionLocation()) )//&& !hasCollided)
		{
			hasCollided = true;
			player.setState(PlayerState.FLOATING);
		}
		else
		{
			if(hasCollided) //previously collided with cloud, but is no longer colliding
			{
				hasCollided = false;
				player.setState(PlayerState.FALLING);
			}
		}
	}
	@Override
	public Rectangle getCollisionRect()
	{
		return new Rectangle(getX() + 22 , getY() + 28, 215, 110); //these values are hardcoded based on the photoshop model of what looked like a good collision rectangle
	}
}
