package com.me.stratofall.objects.clouds;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;
import com.me.stratofall.Stratofall;

/**
 * 
 * @author Shane
 * The basis for each type of cloud. 
 * 
 * Types of clouds
 * ================
 * NormalCloud - Does nothing
 * LightningCloud - Damages Player
 * RainCloud - Chance to act as a lightning cloud or a normal cloud
 * 
 */

public abstract class Cloud extends Actor
{
	public TextureRegion cloudImage;
	public Random random = new Random();
	public Sound effectSound;
	
	public Color tint = Color.WHITE;
	
	public boolean hasCollided = false;
	
	public float max_reset_time; //each cloud should have a different time before its reset and put back on the screen
	public float min_reset_time; //the min time a cloud will wait before resetting
	public float reset_time; //the actual reset time, calulated by randomly by the max/min times
	
	public TextureAtlas atlas;
	
	public float VELOCITY_Y_MAX = 4f; //cloud movement speed, changeable for different types.
	public float VELOCITY_Y_MIN = 2f;
	private float velocity_y;
	
	protected float velocity_multiplier = 1f;
	
	
	private float lastTimeOnScreen = TimeUtils.nanoTime(); //last time the cloud was visible on the screen
	
	private boolean onScreen = true;
	
	public ParticleEffect effect;
	

	public Cloud()
	{
		atlas = new TextureAtlas(Gdx.files.internal("objects/objects.pack"));
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		super.draw(batch, parentAlpha);
		
		if(effect != null)
			effect.draw(batch, Gdx.graphics.getDeltaTime());
		
		batch.draw(cloudImage, getX(), getY());
	}
	public abstract void checkCollisions();
	public abstract Rectangle getCollisionRect();
	
	@Override
	public void act(float delta) 
	{
		super.act(delta);
		
		if(getY() < Stratofall.HEIGHT) //move the clouds up the screen while they are on it
			setY(getY() + velocity_y * velocity_multiplier);
		
		if(onScreen)
			lastTimeOnScreen = TimeUtils.nanoTime(); //get the last time the cloud was on the screen
		
		//reuse the object if it goes off the screen to save memory
		if(getY() >= Stratofall.HEIGHT)
		{
			onScreen = false; //no longer on screen
			
			float elapsedTime = (System.nanoTime()- lastTimeOnScreen);
			
			
			if(elapsedTime > reset_time) //reset the cloud
			{
				reset(); //reset the cloud
			}
		}
		
		if(effect != null)
			effect.setPosition(getX() + getWidth()/2, getY() + getHeight()/2); //emitt from the center of the cloud
		
		checkCollisions();
		
	}
	public void setRandomXPosition()
	{
		//give a random position on the x axis
		setPosition(random.nextInt(Stratofall.WIDTH) - getWidth()/2, -getHeight());
	}
	public void setResetTime() //the clouds next reset time is randomized
	{
		reset_time = MathUtils.random(min_reset_time, max_reset_time);
		
	}
	public void playSoundEffect()
	{
//		if(effectSound != null)
//			effectSound.play();
	}
	public boolean onScreen()
	{
		return onScreen;
	}	
	public void reset() //resets the cloud
	{
		playSoundEffect(); //when a cloud enters the screen. play the sound effect.
		onScreen = true; //the cloud is back on the screen
		hasCollided = false; //you can now collide with this cloud again
		setPosition(random.nextInt(Stratofall.WIDTH) - getWidth()/2,-getHeight());
		
		
		setVelocity();
		setResetTime();
	}
	public void setVelocity()
	{
		velocity_y = MathUtils.random(VELOCITY_Y_MIN, VELOCITY_Y_MAX);
		
	}
}
	
