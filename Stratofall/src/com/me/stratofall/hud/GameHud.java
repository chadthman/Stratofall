package com.me.stratofall.hud;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;


public class GameHud extends Actor
{
	private Player player;
	
	
	private float lives_loc_x; //x location on the screen
	private float lives_loc_y; //y location on the screen
	
	private float margin_top = 24f;
	
	private int meters_fallen;
	private float meters_loc_x = 24f;
	private float meters_loc_y = Stratofall.HEIGHT - margin_top;
	private float balloons_loc_x = Stratofall.WIDTH / 4;
	private float balloons_loc_y = Stratofall.HEIGHT - margin_top;
	
	private BitmapFont whiteFont;
	
	private Texture lifeTexture;
	private ArrayList<Texture> lives;
	
	private ParticleEffect life_effect;
	private Sound lifeLostSoundEffect;
	
	
	public GameHud(Player p)
	{
		player = p;
		
		lives = new ArrayList<Texture>();
		
		//create font: we really need a new font
		whiteFont = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);
		
		//load images
		lifeTexture = Stratofall.assets.get("hud/heart.png", Texture.class);
		updateLifeDisplay();
		
		//set lives location on screen
		lives_loc_y = Stratofall.HEIGHT - lifeTexture.getHeight() - 16;
		lives_loc_x = Stratofall.WIDTH - (lifeTexture.getWidth() * player.getLives()); //adjust the lives display accordingly
		
		//get the heart effect
		life_effect = new ParticleEffect();
		life_effect.load(Gdx.files.internal("particles/effects/heart_explosion.p"), Gdx.files.internal("particles/effects"));
		
		lifeLostSoundEffect = Stratofall.assets.get("sounds/effect/heart_loss.ogg", Sound.class);
	}
	
	@Override
	public void act(float delta) 
	{
		super.act(delta);
		updateLifeDisplay();
		updateMetersDisplay();
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);
		drawLifeDisplay(batch, Gdx.graphics.getDeltaTime());
		drawMetersDisplay(batch);
		drawBalloonsDisplay(batch);
	}
	
	private void drawBalloonsDisplay(SpriteBatch batch)
	{
		whiteFont.setColor(1f,.96f,0f,1f); //yellow color
		whiteFont.draw(batch, "Multiplier:", balloons_loc_x, balloons_loc_y);
		whiteFont.setColor(1f, 1f, 1f, 1f);
		whiteFont.draw(batch, Integer.toString(player.getBalloons())+"x", balloons_loc_x + 240, balloons_loc_y);
	}
	
	private void drawMetersDisplay(SpriteBatch batch)
	{
		//white.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		whiteFont.setColor(1, 1, 1, 1);
		whiteFont.draw(batch,meters_fallen+"M", meters_loc_x, meters_loc_y);
		
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
					lives.add(lifeTexture); //add the correct amount of lives to display
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
		lives_loc_x = Stratofall.WIDTH - (lifeTexture.getWidth() * lives.size());
		
	}
	
	private void doLifeEffect()
	{
		life_effect.setPosition(lives_loc_x + (lifeTexture.getWidth()/2), lives_loc_y + (lifeTexture.getHeight()/2));
		life_effect.reset();
		lifeLostSoundEffect.play();
	}
}
