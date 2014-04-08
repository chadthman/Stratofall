package com.me.stratofall.objects.balloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.TouchableAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public abstract class Balloon extends Actor
{
	public Player player;
	public TextureRegion balloonImage;
	
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
	public static float HEIGHT = 64;
	
	public float reset_time;
	public float max_reset_time;
	public float min_reset_time;
	public float lastTimeOnScreen;
	
	private boolean allowReset = true;
	
	
	public Balloon()
	{
		this.addListener(new InputListener()
		{
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button)
			{
				if(isAlive)
				{
					if(player != null)
						player.addBalloons(value); //increase coin count
					playEffect();
					isAlive = false;
				}
				
				return super.touchDown(event, x, y, pointer, button);
			}
		});
	}
	
	@Override
	public void act(float delta)
	{
		
		
		if(isAlive)
		{
			lastTimeOnScreen = System.nanoTime();
			checkCollisions();
			
			if(player != null) //if the player exists. i.e not in a menu
				setY(getY() + velocity * player.getVelocity());
			else
				setY(getY() + velocity);
			
			trail_effect.setPosition(getX() + (getWidth()/2), getY()); //center of coin
			
			if(getY() > Stratofall.HEIGHT)
			{
				isAlive = false;
			}
		}
		else
		{
			float elapsedTime = System.nanoTime() - lastTimeOnScreen;
			if(elapsedTime > reset_time)
			{
				reset();
			}
		}
		
		super.act(delta);
	}
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		if(isAlive)
		{
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
		if(player != null)
			if(player.getCollisionLocation().overlaps(getCollisionRectangle()) && isAlive)
			{
				if(isAlive)
				{
					player.addBalloons(value); //increase coin count
					playEffect();
				}
				
				isAlive = false;
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
	
	public void reset()
	{
		if(allowReset)
		{
			setRespawnTimes();
			isAlive = true;
			setPosition(MathUtils.random(Stratofall.WIDTH) - getWidth()/2,-getHeight() * 2);
			setVelocity();
		}
		
	}
	public void allowReset(boolean allow)
	{
		allowReset = allow;
	}
	public void setRespawnTimes()
	{
		reset_time = MathUtils.random(min_reset_time, max_reset_time);
	}
	protected void setVelocity()
	{
		velocity = MathUtils.random(VELOCITY_MIN, VELOCITY_MAX);
		
	}
	abstract void loadEffects();
}
