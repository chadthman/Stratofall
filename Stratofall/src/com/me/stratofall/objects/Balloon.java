package com.me.stratofall.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public abstract class Balloon 
{
	public Player player;
	public TextureRegion balloonImage;
	public TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("objects/objects.pack"));
	public Rectangle balloon;
	
	public int value = 1;
	
	public Sound effectSound;
	public ParticleEffect effect;
	public ParticleEffect trail_effect;
	
	private float velocity = 3f;
	
	private boolean isAlive = true;
	
	
	public void draw(SpriteBatch batch, float delta)
	{		
		if(isAlive)
		{
			//we draw the coin if its alive
			trail_effect.draw(batch, delta);
			batch.draw(balloonImage, balloon.x, balloon.y);
		}
		
		effect.draw(batch, delta); //the effect should still be drawn even if the coin is gone until it is complete
	}
	public void update()
	{
		if(balloon.y < Stratofall.HEIGHT)
			balloon.y += velocity * player.getFallRate();
		
		trail_effect.setPosition(balloon.x + (balloon.width/2), balloon.y); //center of coin
	}
	public void playEffect()
	{
		//play sound effect
		//play particle effect
		effect.setPosition(balloon.x + (balloon.width/2), balloon.y + (balloon.height/2)); //center of coin
		effectSound.play();
		effect.reset();
	}
	public void checkCollisions()
	{
		if(player.getCollisionLocation().overlaps(balloon) && isAlive)
		{
			if(isAlive)
			{
				player.addCoins(value); //increase coin count
				playEffect();
			}
			
			isAlive = false;
		}
	}
	public Rectangle getLocation()
	{
		return balloon;
	}
	public void dispose()
	{
		effect.dispose();
		trail_effect.dispose();
	}
	public boolean isAlive()
	{
		// TODO Auto-generated method stub
		return isAlive;
	}
	public boolean effectCompleted()
	{
		// TODO Auto-generated method stub
		return effect.isComplete();
	}

}
