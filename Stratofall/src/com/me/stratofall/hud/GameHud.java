package com.me.stratofall.hud;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;


public class GameHud 
{
	private Player player;
	
	private Texture lifeImage;
	
	private float lives_loc_x; //x location on the screen
	private float lives_loc_y; //y location on the screen
	
	private float margin_top = 24f;
	
	private int meters_fallen;
	private float meters_loc_x = 24f;
	private float meters_loc_y = Stratofall.HEIGHT - margin_top;
	private float coins_loc_x = Stratofall.WIDTH/4;
	private float coins_loc_y = Stratofall.HEIGHT - margin_top;
	
	private BitmapFont white;
	
	private ArrayList<Texture> lives;
	private ParticleEffect life_effect;
	private Sound lifeLostSoundEffect;
	
	
	
	public GameHud(Player p)
	{
		player = p;
		
		lives = new ArrayList<Texture>();
		
		//create font: we really need a new font
		white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);
		
		//load images
		lifeImage = Stratofall.assets.get("hud/heart.png", Texture.class);
		updateLifeDisplay();
		
		//set lives location on screen
		lives_loc_y = Stratofall.HEIGHT - lifeImage.getHeight() - 16;
		lives_loc_x = Stratofall.WIDTH - (lifeImage.getWidth() * player.getLives()); //adjust the lives display accordingly
		
		//get the heart effect
		life_effect = new ParticleEffect();
		life_effect.load(Gdx.files.internal("particles/effects/heart_explosion.p"), Gdx.files.internal("particles/effects"));
		
		lifeLostSoundEffect = Stratofall.assets.get("sounds/effect/heart_loss.ogg", Sound.class);
		
		
	}
	public void draw(SpriteBatch batch, float delta)
	{
		update();
		
		batch.begin();
		drawLifeDisplay(batch, delta);
		drawMetersDisplay(batch);
		drawCoinsDisplay(batch);
		batch.end();
		
	}
	private void drawCoinsDisplay(SpriteBatch batch)
	{
		// TODO Auto-generated method stub
		white.setColor(1f,.96f,0f,1f); //yellow color
		white.draw(batch, "Coins:", coins_loc_x, coins_loc_y);
		white.setColor(1f, 1f, 1f, 1f);
		white.draw(batch, ""+player.getCoins(), coins_loc_x + 135, coins_loc_y);
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
	private void drawLifeDisplay(SpriteBatch batch, float delta)
	{
		int currentLife = 0;
		for (Texture life : lives)
		{
			batch.draw(life, lives_loc_x + (currentLife * life.getWidth()), lives_loc_y);
			currentLife++;
		}
		life_effect.draw(batch, delta);
		currentLife = 0;
	}
	
	private void updateLifeDisplay()
	{
		int l = lives.size();
		int pl = player.getLives();
		
		if(pl >= 0) //while the player has lives left
		{
			if(l < pl) //if the lives displayed is less than the amount of lives the player actually has, update.
			{
				for(int i = 0; i < (pl-l); i++)
					lives.add(lifeImage); //add the correct amount of lives to display
			}
			else if(pl < l) //if the player has lost lives, remove them.
			{
				for(int i = 0; i < (l - pl); i++)
				{
					lives.remove(lives.size()-1);
				}
				doLifeEffect();
			}
		}
		
		//adjust lives display x value
		lives_loc_x = Stratofall.WIDTH - (lifeImage.getWidth() * lives.size());
		
	}
	private void doLifeEffect()
	{
		life_effect.setPosition(lives_loc_x + (lifeImage.getWidth()/2), lives_loc_y + (lifeImage.getHeight()/2));
		life_effect.reset();
		lifeLostSoundEffect.play();
	}
}
