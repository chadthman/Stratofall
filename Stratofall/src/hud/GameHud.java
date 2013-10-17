package hud;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public class GameHud 
{
	private Player player;
	
	private Texture lifeImage;
	
	private float lives_loc_x; //x location on the screen
	private float lives_loc_y; //y location on the screen
	
	private float lives_margin; //the space between each life image
	
	private float meters_fallen;
	private float meters_loc_x = 24f;
	private float meters_loc_y = Stratofall.HEIGHT - 24f;
	
	private BitmapFont white;
	
	private ArrayList<Texture> lives;
	
	public GameHud(Player p)
	{
		player = p;
		
		lives = new ArrayList<Texture>();
		
		//create font: we really need a new font
		white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);
		
		//load images
		lifeImage = Stratofall.assets.get("hud/life.png", Texture.class);
		updateLifeDisplay();
		
		//set lives location on screen
		lives_loc_y = Stratofall.HEIGHT - lifeImage.getHeight();
		lives_loc_x = Stratofall.WIDTH - (lifeImage.getWidth() * player.getLives()); //adjust the lives display accordingly
	}
	public void draw(SpriteBatch batch, float delta)
	{
		update();
		
		batch.begin();
		drawLifeDisplay(batch);
		drawMetersDisplay(batch);
		batch.end();
	}
	private void drawMetersDisplay(SpriteBatch batch)
	{
		//white.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		white.draw(batch,""+meters_fallen, meters_loc_x, meters_loc_y);
		
	}
	private void update()
	{
		updateLifeDisplay();
		updateMetersDisplay();
	}
	private void updateMetersDisplay()
	{
		meters_fallen = player.getMetersFallen();
		
	}
	private void drawLifeDisplay(SpriteBatch batch)
	{
		int currentLife = 0;
		for (Texture life : lives)
		{
			batch.draw(life, lives_loc_x + (currentLife * life.getWidth()), lives_loc_y);
			currentLife++;
		}
		currentLife = 0;
	}
	
	private void updateLifeDisplay()
	{
		int l = lives.size();
		int pl = player.getLives();
		
		if(pl > 0) //while the player has lives left
		{
			if(l < pl) //if the lives displayed is less than the amount of lives the player actually has, update.
			{
				for(int i = 0; i < (pl-l); i++)
					lives.add(lifeImage); //add the correct amount of lives to display
			}
			else if(pl < l) //if the player has lost lives, remove them.
			{
				for(int i = 0; i < (l - pl); i++)
					lives.remove(lives.size()-1);
			}
		}
		
		//adjust lives display x value
		lives_loc_x = Stratofall.WIDTH - (lifeImage.getWidth() * lives.size());
	}
}
