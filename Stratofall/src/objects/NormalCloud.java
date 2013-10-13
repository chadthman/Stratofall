package objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.me.stratofall.Stratofall;

public class NormalCloud extends Cloud
{
	public NormalCloud()
	{
		
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
	}
}
