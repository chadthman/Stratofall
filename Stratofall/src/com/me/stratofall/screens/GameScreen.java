package com.me.stratofall.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.me.stratofall.Stratofall;

public class GameScreen implements Screen 
{
	final Stratofall game;
	public OrthographicCamera camera;
	
	Texture playerImage;
	Texture objectImage;
	
	Rectangle player;
	
	//Player physics
	private final float VELOCITY_DX = .35f;
	private final float VELOCITY_DY = .45f;
	private final float MAX_VELOCITY_X = 15f;
	private final float MAX_VELOCITY_Y = 15f;
	private final float MOMENTUM = .025f;
	
	private final float STOP_DISTANCE = 500; //The furthest the player can be down the screen
	
	
	private float accel_X = 0;
	
	
	public GameScreen(final Stratofall gam)
	{
		game = gam;
		
		//load images
		playerImage = new Texture(Gdx.files.internal("test.png"));
		
		//load sounds
		
		//create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
		
		//create rectangles to logically represent the player and any game objects
		player = new Rectangle();
		player.x = game.WIDTH/2 - player.width/2;
		player.y = 1180;
		player.width = playerImage.getWidth();
		player.height = playerImage.getHeight();
		
	
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
        game.batch.begin();
        game.batch.draw(playerImage, player.x, player.y);
        game.batch.end();
        
        //Update player position (new methods)
        updatePlayer();
        
        //Check for collisions with objects
       

	}

	private void updatePlayer() 
	{
		
		//keep on the screen
		if(player.x < 0)
		{
			player.x = 0;
			accel_X = 0;
		}
		if(player.x > game.WIDTH - player.width)
		{
			player.x = game.WIDTH - player.width;
			accel_X = 0;
		}
		if(player.y < 0)
			player.y = 0;
		
		//Update momentum
		float max_accel_x = Gdx.input.getAccelerometerX();
		
		//Calculate momentum 
		if(accel_X != max_accel_x)
		{
			if(max_accel_x < 0)
				accel_X -= MOMENTUM * Math.abs(max_accel_x);
			else if(max_accel_x > 0)
				accel_X += MOMENTUM * Math.abs(max_accel_x);
			else
				accel_X = 0;
		
		}
		
		//update player location on screen
		player.x = player.x + (-accel_X  * MAX_VELOCITY_X * VELOCITY_DX);
		
		//System.out.println("accel_X: "+accel_X + " max_accel: "+max_accel_x);
		
		//Update falling
		player.y = player.y - MAX_VELOCITY_Y * VELOCITY_DY;
		if(player.y < STOP_DISTANCE)
			player.y = STOP_DISTANCE;
		
		
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
