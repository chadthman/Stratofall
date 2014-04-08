package com.me.stratofall.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;
import com.me.stratofall.hud.GameHud;
import com.me.stratofall.objects.balloons.BalloonManager;
import com.me.stratofall.objects.balloons.BalloonPattern;
import com.me.stratofall.objects.clouds.CloudManager;
import com.me.stratofall.utils.DifficultyManager;
import com.me.stratofall.utils.ScrollingLayer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * 
 * @author Shane 
 * All game objects and HUD are drawn and displayed here.
 */

public class GameScreen implements Screen
{
	FPSLogger fpsLogger = new FPSLogger();

	private ParticleEffect dustEffect;

	private Player player;
	private Music gameMusic;
	private Texture backgroundImage;
	private ScrollingLayer backgroundLayer;
	private GameHud hud;

	private float background_y = -2560; // starting position
	private float background_rate = .25f; // the rate at which the background
											// progresses up the screen

	// private BalloonPattern balloonPattern;

	private Stage stage;
	private Group cloudGroup, cloudGroup2, balloonGroup;
	private CloudManager cloudManager;
	private CloudManager cloudManager2;
	private BalloonManager balloonManager;
	private BalloonPattern balloonPattern, balloonPattern2;
	private DifficultyManager difficulty;
	private Viewport viewport;

	public GameScreen()
	{
		player = new Player();
		hud = new GameHud(player); // create a new instance of hud referencing
									// player

		// load images
		backgroundImage = Stratofall.assets.get("backgrounds/background.jpg",
				Texture.class);
		backgroundLayer = new ScrollingLayer(backgroundImage, .85f);

		// load sounds (should be background noises, other sounds should be
		// loaded within their respective classes)
		gameMusic = Stratofall.assets.get("sounds/background/game_music.mp3",
				Music.class);
		gameMusic.setLooping(true);

		// load effects
		dustEffect = new ParticleEffect();
		dustEffect.load(Gdx.files.internal("particles/effects/dust.p"),
				Gdx.files.internal("particles/effects"));
		dustEffect.setPosition(Stratofall.WIDTH / 2, Stratofall.HEIGHT / 2); // center
																				// of
																				// screen
																				// at
																				// the
																				// bottom
		dustEffect.start();
		
		stage = new Stage(new StretchViewport(Stratofall.WIDTH, Stratofall.HEIGHT));
		//stage = new Stage(Stratofall.WIDTH, Stratofall.HEIGHT);
//		stage = new Stage();
//		viewport = new FitViewport(Stratofall.WIDTH, Stratofall.HEIGHT);
//		stage.setViewport(viewport);
		
		Gdx.input.setInputProcessor(stage);

		cloudGroup = new Group();
		cloudGroup2 = new Group();
		balloonGroup = new Group();
		
		
		cloudManager2 = new CloudManager(cloudGroup2, player);
		cloudManager = new CloudManager(cloudGroup, player);
			cloudManager.setMaxNormalClouds(10);
			cloudManager.setMaxLightningClouds(0);
			cloudManager2.setMaxNormalClouds(10);
			cloudManager2.setMaxLightningClouds(1);
		stage.addActor(backgroundLayer);
		//balloonPattern = new BalloonPattern(player, stage, 10);
		stage.addActor(cloudGroup2);
		stage.addActor(player);
		balloonPattern = new BalloonPattern(player, stage, 6);
		//balloonPattern2 = new BalloonPattern(player, stage, 18);
		stage.addActor(balloonGroup);
		stage.addActor(cloudGroup);
		
		balloonManager = new BalloonManager(balloonGroup, player);
		difficulty = new DifficultyManager(player, cloudManager, cloudManager2);
		//balloonManager.setMaxRedBalloon(100);
		stage.addActor(hud);

		//System.gc();
	}

	@Override
	public void render(float delta)
	{
		// any gamescreen logic goes here.

		Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();
		stage.act(delta);
		stage.draw();

		// fpsLogger.log();

		stage.getSpriteBatch().begin();
		dustEffect.draw(stage.getSpriteBatch(), delta);
		stage.getSpriteBatch().end();

	}

	private void update()
	{
		cloudManager.update();
		cloudManager2.update();
		balloonManager.update();
		difficulty.update();
		

		if (background_y < 0) // move the background
			background_y += background_rate;

		if (player.getLives() == 0)
		{
			//System.gc();
			((Game) Gdx.app.getApplicationListener())
					.setScreen(new FinalScoreScreen(player));
			dispose();
		}

		// keyboard input
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
		{
			((Game) Gdx.app.getApplicationListener())
					.setScreen(new MainMenuScreen());
			dispose();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.F))
		{
			((Game) Gdx.app.getApplicationListener())
					.setScreen(new FinalScoreScreen(player));
			dispose();
		}

		if (Gdx.input.justTouched())
		{
			System.out.println(Gdx.input.getX() + ", "
					+ (Gdx.graphics.getHeight() - Gdx.input.getY()));
		}
	}
	

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height);
		//stage.setViewport(Stratofall.WIDTH, Stratofall.HEIGHT);
	}

	@Override
	public void show()
	{
		gameMusic.play();
	}

	@Override
	public void hide()
	{
		// TODO Auto-generated method stub
		gameMusic.stop();
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose()
	{
		// gameMusic.dispose();
		dustEffect.dispose();
	}
}
