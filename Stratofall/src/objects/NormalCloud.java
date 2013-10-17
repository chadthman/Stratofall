package objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
		
		cloudImage = new TextureRegion(atlas.findRegion("cloud"));
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
	}

	@Override
	public void checkCollisions()
	{
		if(getLocation().overlaps(player.getCollisionLocation()) && !hasCollided)
		{
			hasCollided = true;
			player.setFallRate(.25f, .025f); //slows the player down to a speed of .25f at a rate of .025f per update.
		}
	}
}
