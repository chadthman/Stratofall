package com.me.stratofall.screens;

import objects.Cloud;
import objects.LightningCloud;
import objects.NormalCloud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

/**
 * 
 * @author Shane
 * All game objects and HUD are drawn and displayed here. 
 */
		

public class GameScreen implements Screen 
{
	final Stratofall game;
	public OrthographicCamera camera;
	
	private Player player;
	private Music windMusic;
	private Texture backgroundImage;
	private Texture fogImage;
	
	private float background_y = -2560; //starting position
	private float background_rate = .25f; //the rate at which the background progresses up the screen
	private float fog_y = -2560; //starting position
	private float fog_rate = .5f; //the rate at which the background progresses up the screen
	
	
	private final int MAX_NORMAL_CLOUDS = 4;
	private final int MAX_LIGHTNING_CLOUDS = 2; 
	private final int TOTAL_CLOUDS = MAX_NORMAL_CLOUDS + MAX_LIGHTNING_CLOUDS;
	
	private int normal_clouds = 0; //current amount of normal clouds
	private int lightning_clouds = 0; //current amount of lightning clouds
	
	private final float CLOUD_SPAWN_TIME = 1 * 1000000000f; //every 1 seconds
	private final float LIGHTNING_CLOUD_SPAWN_TIME = 5 * 1000000000f; //every 3 seconds
	
	private float lastCloudSpawnTime = TimeUtils.nanoTime();
	private float lastLightningCloudSpawnTime = TimeUtils.nanoTime();
	
	Array<Cloud> clouds = new Array<Cloud>();
	
	public GameScreen(final Stratofall gam)
	{
		game = gam;
		
		//load images
		backgroundImage = Stratofall.assets.get("backgrounds/background.jpg", Texture.class);
		fogImage = Stratofall.assets.get("backgrounds/background_fog.png", Texture.class);
		
		//load sounds (should be background noises, other sounds should be loaded within their respective classes)
		windMusic = Stratofall.assets.get("sounds/background/background_wind.wav", Music.class);
		windMusic.setLooping(true);
		
		//create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
		
		player = new Player(game);
		
	}

	@Override
	public void render(float delta) 
	{	
		//This is just temporary
		if(Gdx.input.isTouched())
		{
			game.setScreen(new MainMenuScreen(game));
			dispose(); //dispose of any running sounds
		}
		
		Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //any gamescreen logic goes here. 
        update();
        
        //update the camera
        camera.update();
        
        //tell the Spritebatch to render in the coordinate system specified by the camera
        game.batch.setProjectionMatrix(camera.combined);
        
        //begin a new batch and draw the player/objects 
        game.batch.begin();
        //draw the background
        game.batch.draw(backgroundImage, 0, background_y);
        game.batch.end();
        player.draw(delta);
//        game.batch.begin();
//        game.batch.draw(fogImage, 0, fog_y);
//        game.batch.end();
        
        
        for (Cloud cloud : clouds)
		{
			cloud.draw(game);

		}
        
        /*
         * HUD
         * We will need a hud class to draw the scores/meters fallen and what not
         */
     

	}
	private void update()
	{
		//check if we should be spawning anything
        if(normal_clouds < MAX_NORMAL_CLOUDS)
        	spawnNormalCloud();
        if(lightning_clouds < MAX_LIGHTNING_CLOUDS)
        	spawnLightningCloud();
        
        if(background_y < 0) //move the background
        	background_y += background_rate;
        if(fog_y < 0) //move the fog
        	fog_y += fog_rate;
	}

	public void spawnNormalCloud()
	{
		if(TimeUtils.nanoTime() - lastCloudSpawnTime > CLOUD_SPAWN_TIME)
		{	
			normal_clouds++;
			Cloud c = new NormalCloud(player); //reference player so upon collision, we can do stuff to player
			clouds.add(c);
			lastCloudSpawnTime = TimeUtils.nanoTime();
		}
	}
	public void spawnLightningCloud()
	{
		if(TimeUtils.nanoTime() - lastLightningCloudSpawnTime > LIGHTNING_CLOUD_SPAWN_TIME)
		{	
			lightning_clouds++;
			Cloud c = new LightningCloud(player);
			clouds.add(c);
			c.playSoundEffect(); //play the effect on spawn to notify player 
			lastLightningCloudSpawnTime = TimeUtils.nanoTime();
		}
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() 
	{
		//executes when the screen is shown, start music/sounds here.
		windMusic.play();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() 
	{
		windMusic.dispose();
	}

}
