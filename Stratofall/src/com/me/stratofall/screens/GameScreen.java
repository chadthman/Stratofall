package com.me.stratofall.screens;

import objects.Cloud;
import objects.NormalCloud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public class GameScreen implements Screen 
{
	final Stratofall game;
	public OrthographicCamera camera;
	
	private Player player;
	
	private final int MAX_CLOUDS = 20;
	private final float CLOUD_SPAWN_TIME = 1 * 1000000000f; //every 1 seconds
	
	private float lastCloudSpawnTime = TimeUtils.nanoTime();
	
	Array<Cloud> clouds = new Array<Cloud>();
	
	public GameScreen(final Stratofall gam)
	{
		game = gam;
		
		//load images
		
		//load sounds
		
		//create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
		
		player = new Player(game);
		
		
	}

	@Override
	public void render(float delta) 
	{	
		if(Gdx.input.isTouched())
		{
			game.setScreen(new MainMenuScreen(game));
		}
		Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //update the camera
        camera.update();
        
        //tell the Spritebatch to render in the coordinate system specified by the camera
        game.batch.setProjectionMatrix(camera.combined);
        
        //begin a new batch and draw the player/objects
        player.draw(delta);
        for (Cloud cloud : clouds)
		{
			cloud.draw(delta, game);
		}
        
        //check if we should be spawning anything
        if(clouds.size < MAX_CLOUDS)
        	spawnCloud();
        
        
     

	}
	public void spawnCloud()
	{
		if(TimeUtils.nanoTime() - lastCloudSpawnTime > CLOUD_SPAWN_TIME)
		{
			Cloud c = new NormalCloud();
			clouds.add(c);
			lastCloudSpawnTime = TimeUtils.nanoTime();
		}
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		//executes when the screen is shown, start music/sounds here.

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
		// TODO Auto-generated method stub

	}

}
