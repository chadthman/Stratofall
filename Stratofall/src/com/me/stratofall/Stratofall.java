package com.me.stratofall;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.stratofall.screens.MainMenuScreen;

public class Stratofall extends Game
{
	public SpriteBatch batch;
	public BitmapFont font;
	
	public static final int WIDTH = 768;
	public static final int HEIGHT = 1280;
	
	
	public void create() 
	{
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}
	public void render()
	{
		super.render();
	}
	public void dispose()
	{
		batch.dispose();
		font.dispose();
	}
}
