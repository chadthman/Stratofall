package com.me.stratofall.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public abstract class Balloon extends Actor implements Poolable
{
	public Player player;
	public TextureRegion balloonImage;
	public TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("objects/objects.pack"));
	
	public int value = 1;
	public String TYPE;
	
	public Sound effectSound;
	public ParticleEffect effect;
	public ParticleEffect trail_effect;
	
	private float VELOCITY_MIN = 2f;
	private float VELOCITY_MAX = 5f;
	private float velocity = 3f;
	
	private boolean isAlive = true;
	
	public static float WIDTH = 64;
	public static float HEIGHT = 4;
	
	//TODO add in random respawn times so that it varies
	
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		
		if(isAlive)
		{
			//checkCollisions();
			setY(getY() + velocity * player.getVelocity()/2);
			trail_effect.setPosition(getX() + (getWidth()/2), getY()); //center of coin
		}
		if(getY() > Stratofall.HEIGHT)
		{
			isAlive = false;
			reset();
		}
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		if(isAlive)
		{
			//we draw the coin if its alive
			trail_effect.draw(batch, Gdx.graphics.getDeltaTime());
			batch.draw(balloonImage, getX(), getY());
		}
		
		effect.draw(batch, Gdx.graphics.getDeltaTime()); //the effect should still be drawn even if the coin is gone until it is complete
	}
	public void playEffect()
	{
		effect.setPosition(getX() + (getWidth()/2), getY() + (getHeight()/2)); //center of coin
		effectSound.play();
		effect.reset();
	}
	public void checkCollisions()
	{
		if(player.getCollisionLocation().overlaps(getCollisionRectangle()) && isAlive)
		{
			if(isAlive)
			{
				player.addBalloons(value); //increase coin count
				playEffect();
			}
			
			isAlive = false;
			reset();
		}
	}
	public Rectangle getCollisionRectangle()
	{
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
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
	public String getType()
	{
		// TODO Auto-generated method stub
		return TYPE;
	}
	
	public void setLocation(float x, float y)
	{
		setPosition(x, y);
	}
	
	@Override
	public void reset()
	{
		isAlive = true;
		setPosition(MathUtils.random(Stratofall.WIDTH) - getWidth()/2,-getHeight() * 2);
		setVelocity();
		setRespawnTimes();
	}
	private void setRespawnTimes()
	{
		//random respawn times.
		
	}
	protected void setVelocity()
	{
		velocity = MathUtils.random(VELOCITY_MIN, VELOCITY_MAX);
		
	}
	abstract void loadEffects();
}
