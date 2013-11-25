package com.me.stratofall;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.me.stratofall.screens.MainMenuScreen;

public class Stratofall extends Game
{
	public SpriteBatch batch;
	public BitmapFont font;
	
	public static final int WIDTH = 768;
	public static final int HEIGHT = 1280;
	
	public static AssetManager assets = new AssetManager();

	public static final String VERSION = "1.0.0";
	
	@Override
	public void create() 
	{	
		GLTexture.setEnforcePotImages(false);
		
		loadAssets();
		batch = new SpriteBatch();
		font = new BitmapFont();
	
		this.setScreen(new MainMenuScreen());
		
	}
	private void loadAssets()
	{
		assets.load("sounds/background/background_piano.ogg", Music.class);
		assets.load("sounds/background/game_music.ogg", Music.class);
		assets.load("sounds/effect/sound_lightning.ogg", Sound.class);
		assets.load("sounds/effect/pop.ogg", Sound.class);
		assets.load("sounds/effect/heart_loss.ogg", Sound.class);
		
		assets.load("backgrounds/background_fog.png", Texture.class);
		assets.load("backgrounds/background.jpg", Texture.class);
		assets.load("backgrounds/light_background.jpg", Texture.class);
		assets.load("hud/heart.png", Texture.class);
		//assets.load("objects/objects.png", TextureRegion.class); dont load texture regions for atlas. 
		assets.load("player.png", Texture.class);
		assets.load("playerSpriteSheet.png", Texture.class);
		
		assets.finishLoading(); //dont do anything until all assets are loaded
		
	}
	
	public static BitmapFont generateFont(String internalPath, int size, Color color) 
	{
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(internalPath));
        BitmapFont font = null;

        font = gen.generateFont(size);
        font.setColor(color);
        font.setUseIntegerPositions(false);
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        gen.dispose();
        return font;
    }
	@Override
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
	
	@Override
	public void dispose()
	{
		batch.dispose();
		font.dispose();
		assets.dispose();
	}
}
