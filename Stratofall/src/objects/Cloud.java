package objects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.me.stratofall.Player;
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

public abstract class Cloud
{
	public Player player;
	public TextureRegion cloudImage;
	public Rectangle cloud;
	public Random random = new Random();
	public Sound effectSound;
	
	public float max_reset_time; //each cloud should have a different time before its reset and put back on the screen
	public float min_reset_time; //the min time a cloud will wait before resetting
	public float reset_time; //the actual reset time, calulated by randomly by the max/min times
	
	public TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("objects/objects.pack"));;
	
	public float VELOCITY_Y = 4f; //cloud movement speed, changeable for different types.
	
	private float lastTimeOnScreen = TimeUtils.nanoTime(); //last time the cloud was visible on the screen
	
	private boolean onScreen = true;
	
	public ParticleEffect effect;
	
	public void draw(Stratofall gam,float delta)
	{	
		gam.batch.begin();
		if(effect != null)
			effect.draw(gam.batch, delta);
		
		gam.batch.draw(cloudImage, cloud.x, cloud.y);
		gam.batch.end();
		
		update(); //update the cloud
		checkCollisions(); //check for collisions and do stuff
	}
	public abstract void checkCollisions();
	public void update()
	{	
		
		if(cloud.y < Stratofall.HEIGHT) //move the clouds up the screen while they are on it
			cloud.y += VELOCITY_Y;
		
		if(onScreen)
			lastTimeOnScreen = TimeUtils.nanoTime(); //get the last time the cloud was on the screen
		
		//reuse the object if it goes off the screen to save memory
		if(cloud.y >= Stratofall.HEIGHT)
		{
			onScreen = false; //no longer on screen
			effect.allowCompletion(); //stop the effect
			
			float elapsedTime = (System.nanoTime()- lastTimeOnScreen);
			
			
			if(elapsedTime > reset_time) //reset the cloud
			{
				reset(); //reset the cloud
			}
		}
		
		if(effect != null)
			effect.setPosition(cloud.x + cloud.width/2, cloud.y + cloud.height/2); //emitt from the center of the cloud
	}
	public void setResetTime() //the clouds next reset time is randomized
	{
		reset_time = MathUtils.random(max_reset_time - min_reset_time);
		
	}
	public void playSoundEffect()
	{
		if(effectSound != null)
			effectSound.play();
	}
	public Boolean onScreen()
	{
		return onScreen;
	}	
	//custom set a clouds location
	public void setLocation(Vector2 v)
	{
		cloud.x = v.x;
		cloud.y = v.y;
	}
	public void reset() //resets the cloud
	{
		playSoundEffect(); //when a cloud enters the screen. play the sound effect.
		onScreen = true; //the cloud is back on the screen
		cloud.x = random.nextInt(Stratofall.WIDTH) - (cloud.width)/2;
		cloud.y = -cloud.height;
		effect.reset(); //start the effect again
		setResetTime();
	}
	//returns the clouds rectangle location
	public Rectangle getLocation()
	{
		return cloud;
	}
}
	
