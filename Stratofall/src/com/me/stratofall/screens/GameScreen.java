package com.me.stratofall.screens;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.me.stratofall.Player;
import com.me.stratofall.ScrollingLayer;
import com.me.stratofall.Stratofall;
import com.me.stratofall.hud.GameHud;
import com.me.stratofall.objects.Cloud;
import com.me.stratofall.objects.BalloonPattern;
import com.me.stratofall.objects.LightningCloud;
import com.me.stratofall.objects.NormalCloud;

/**
 * 
 * @author Shane
 * All game objects and HUD are drawn and displayed here. 
 */
		

public class GameScreen implements Screen 
{
	FPSLogger fpsLogger = new FPSLogger();
	final Stratofall game;
	public OrthographicCamera camera;
	
	//light source: to get a shadow from objects you need to create a body out of them. Pretty much would have had to start this project with shadows
	//in mind. I think a light source without shadows would be good enough anyways. adds a little depth.
	private RayHandler rayHandler;
	private World world;
	
	
	private ParticleEffect dustEffect;
	
	private Player player;
	private Music gameMusic;
	private Texture backgroundImage;
	private Texture fogImage;
	private ScrollingLayer backgroundLayer;
	private GameHud hud;
	
	private float background_y = -2560; //starting position
	private float background_rate = .25f; //the rate at which the background progresses up the screen
	private float fog_y = -2560; //starting position
	private float fog_rate = .5f; //the rate at which the background progresses up the screen
	
	
	private final int MAX_NORMAL_CLOUDS = 6;
	private final int MAX_LIGHTNING_CLOUDS = 2; 
	private final int TOTAL_CLOUDS = MAX_NORMAL_CLOUDS + MAX_LIGHTNING_CLOUDS;
	
	private int normal_clouds = 0; //current amount of normal clouds
	private int lightning_clouds = 0; //current amount of lightning clouds
	
	private final float CLOUD_SPAWN_TIME = 1 * 1000000000f; //every 1 seconds
	private final float LIGHTNING_CLOUD_SPAWN_TIME = 5 * 1000000000f; //every 3 seconds
	
	private float lastCloudSpawnTime = TimeUtils.nanoTime();
	private float lastLightningCloudSpawnTime = TimeUtils.nanoTime();
	
	Array<Cloud> clouds = new Array<Cloud>();
	
	private BalloonPattern cp1;
	
	public GameScreen(final Stratofall gam)
	{
		game = gam;
		
		//load images
		backgroundImage = Stratofall.assets.get("backgrounds/background.jpg", Texture.class);
		backgroundLayer = new ScrollingLayer(backgroundImage, 1.5f);
		
		//load sounds (should be background noises, other sounds should be loaded within their respective classes)
		gameMusic = Stratofall.assets.get("sounds/background/background_piano.ogg", Music.class);
		gameMusic.setLooping(true);
		
		//load effects
		dustEffect = new ParticleEffect();
		dustEffect.load(Gdx.files.internal("particles/effects/dust.p"), Gdx.files.internal("particles/effects"));
		dustEffect.setPosition(Stratofall.WIDTH/2, Stratofall.HEIGHT/2); //center of screen at the bottom
		dustEffect.start();
		
		//create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
		
		player = new Player(game);
		hud = new GameHud(player); //create a new instance of hud referencing player
		
		//create world
		//world = new World(new Vector2(0, -9.8f), false);
		//create our light source
		//rayHandler = new RayHandler(world);
		//rayHandler.setCombinedMatrix(camera.combined);
		//new PointLight(rayHandler, 3000, new Color(255f, 252f, 163f, 255f),3000, Stratofall.WIDTH/2, Stratofall.HEIGHT + 275); //Possibly add light effects to lightning cloud to give a "flash" every now and then
		
		//create coin patterns
		cp1 = new BalloonPattern(player);
		
	}

	@Override
	public void render(float delta) 
	{	
		//any gamescreen logic goes here. 
        update();
        
			Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 0f);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
	       fpsLogger.log();
	        
	        //update the camera
	        camera.update();
	        
	        //tell the Spritebatch to render in the coordinate system specified by the camera
	        game.batch.setProjectionMatrix(camera.combined);
	        
	        //begin a new batch and draw the player/objects 
	        backgroundLayer.draw(game);
	        game.batch.begin();
	        //draw the background
	       // game.batch.draw(backgroundImage, 0, background_y);
	        //draw the coin patterns
	        dustEffect.draw(game.batch, delta);
	        cp1.draw(game.batch, delta);
	        game.batch.end();
	        
	       
	        
	        player.draw(delta);
	        
	      	//fogLayer.draw(game);
	        for (Cloud cloud : clouds)
			{
				cloud.draw(game, delta);
	
			}
	        
	        //draw light
	 //       rayHandler.updateAndRender(); 
	        //the hud overlays everything and thus should be drawn last
	        hud.draw(game.batch, delta);
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
	        
	        if(player.getLives() == 0)
	        {
	        	game.setScreen(new FinalScoreScreen(game, player));
				dispose();
	        } 
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
		gameMusic.play();
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
		gameMusic.dispose();
		dustEffect.dispose();
		//rayHandler.dispose();
		//world.dispose();
	}

}

