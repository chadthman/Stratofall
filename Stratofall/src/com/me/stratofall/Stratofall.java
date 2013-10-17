package com.me.stratofall;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.stratofall.screens.GameScreen;
import com.me.stratofall.screens.MainMenuScreen;

public class Stratofall extends Game
{
	public SpriteBatch batch;
	public BitmapFont font;
	
	public static final int WIDTH = 768;
	public static final int HEIGHT = 1280;
	
	public static AssetManager assets = new AssetManager();
	
	
	public void create() 
	{
		loadAssets();
		
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		this.setScreen(new MainMenuScreen(this));
		
	}
	private void loadAssets()
	{
		assets.load("sounds/background/background_wind.wav", Music.class);
		assets.load("sounds/effect/sound_lightning.wav", Sound.class);
		
		assets.load("backgrounds/background_fog.png", Texture.class);
		assets.load("backgrounds/background.jpg", Texture.class);
		assets.load("hud/life.png", Texture.class);
		//assets.load("objects/objects.png", TextureRegion.class); dont load texture regions for atlas. 
		assets.load("player.png", Texture.class);
		assets.load("playerSpriteSheet.png", Texture.class);
		
		assets.finishLoading(); //dont do anything until all assets are loaded
		
	}
	public void render()
	{
		super.render();
		
//		if(assets.update()) //if all assets loaded, for synchronized loading. 
//		{
//			this.setScreen(new GameScreen(this));
//		}
//		else
//		{
//			//display loading info
//			float progress = assets.getProgress();
//			System.out.println("Loading... "+progress*100+"%");
//		}
	}
	public void dispose()
	{
		batch.dispose();
		font.dispose();
		assets.dispose();
	}
}
