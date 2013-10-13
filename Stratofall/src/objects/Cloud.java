package objects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.me.stratofall.Stratofall;

public abstract class Cloud
{
	public TextureRegion cloudImage;
	public Rectangle cloud;
	public Random random = new Random();
	
	public float max_reset_time; //each cloud should have a different time before its reset and put back on the screen
	public float min_reset_time;
	public float reset_time;
	
	public TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("objects/objects.pack"));;
	
	private final float VELOCITY_Y = 5f;
	
	private Timer timer = new Timer();
	
	private float deltaTime;
	private float lastTimeOnScreen;
	
	private boolean onScreen = true;
	
	
	public void draw(float delta, Stratofall gam)
	{
		deltaTime = delta;
		
		gam.batch.begin();
		gam.batch.draw(cloudImage, cloud.x, cloud.y);
		gam.batch.end();
		
		update(); //update the cloud
	}
	public void setLocation(Vector2 v)
	{
		cloud.x = v.x;
		cloud.y = v.y;
	}
	public void reset() //resets the cloud
	{
		cloud.x = random.nextInt(Stratofall.WIDTH) - (cloud.width)/2;
		cloud.y = -cloud.height;
		setResetTime();
	}
	public Rectangle getLocation()
	{
		return cloud;
	}
	public void update()
	{
		if(cloud.y < Stratofall.HEIGHT)
			cloud.y += VELOCITY_Y;
		
		if(onScreen)
			lastTimeOnScreen = TimeUtils.nanoTime(); //get the last time the cloud was on the screen
		
		//reuse the object if it goes off the screen to save memory
		if(cloud.y > Stratofall.HEIGHT)
		{
			onScreen = false; //no longer on screen
			float elapsedTime = (System.nanoTime()- lastTimeOnScreen);
			
			
			if(elapsedTime > reset_time) //reset the cloud
			{
				reset(); //reset the cloud
			}
		}
	}
	public void setResetTime() //the clouds next reset time is randomized
	{
		reset_time = MathUtils.random(max_reset_time - min_reset_time);
	}
}
	
