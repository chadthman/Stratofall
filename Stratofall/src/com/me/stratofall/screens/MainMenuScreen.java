package com.me.stratofall.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.stratofall.Stratofall;
import com.me.stratofall.objects.balloons.BalloonManager;
import com.me.stratofall.objects.clouds.CloudManager;
import com.me.stratofall.tween.ActorAccessor;
import com.me.stratofall.utils.ScrollingLayer;


public class MainMenuScreen implements Screen
{
	
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private Button buttonPlay, buttonExit, buttonHighScore, buttonCredits;
	private BitmapFont white, bananaBrick, xolonium;
	private Label heading;
	private TweenManager tweenManager;
	private Texture backgroundTexture;
	
	private Music menuMusic;
	
	private CloudManager cloudManager;
	private BalloonManager balloonManager;
	private ScrollingLayer backgroundLayer;
	
	private Label versionLabel;
	
	public MainMenuScreen()
	{
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		cloudManager.spawnMenuClouds();
		balloonManager.update();
		
		
		stage.act(delta);
		stage.draw();
		
		tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height)
	{
		stage.setViewport(Stratofall.WIDTH, Stratofall.HEIGHT);
	}

	@Override
	public void show()
	{		
		//stage = new Stage(Stratofall.WIDTH, Stratofall.HEIGHT);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage); //now we can touch stage objects and get input
		
		menuMusic = Stratofall.assets.get("sounds/background/background_piano.ogg", Music.class);
		menuMusic.setLooping(true);
		menuMusic.play();
		
		backgroundTexture = Stratofall.assets.get("backgrounds/background.jpg", Texture.class);
		Group cloudGroup = new Group();
		Group balloonGroup = new Group();
		cloudManager = new CloudManager(cloudGroup);
		balloonManager = new BalloonManager(balloonGroup);
		backgroundLayer = new ScrollingLayer(backgroundTexture, .85f);
		
		stage.addActor(backgroundLayer);
		stage.addActor(balloonGroup);
		stage.addActor(cloudGroup);

		atlas = new TextureAtlas("ui/buttons.pack");
		skin = new Skin(atlas);
		table = new Table(skin);
		table.setBounds(0, 0, Stratofall.WIDTH, Stratofall.HEIGHT);
		
		
		//create font
		//white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);
		bananaBrick = Stratofall.generateFont("fonts/BBrick.ttf", 108, Color.BLACK);
		//xolonium = Stratofall.generateFont("fonts/Xolonium.otf", 32, Color.WHITE);
		
		//LabelStyle versionStyle = new LabelStyle(xolonium, Color.WHITE);
		
		//versionLabel = new Label("version "+Stratofall.VERSION, versionStyle);
		//TextBounds bounds = xolonium.getBounds("version " + Stratofall.VERSION);
		//versionLabel.setPosition(Stratofall.WIDTH - bounds.width - 5, 0);
		//stage.addActor(versionLabel);
		
		//creating buttons
		ButtonStyle textButtonStyleEXIT = new ButtonStyle();
		textButtonStyleEXIT.up = skin.getDrawable("quit"); //button released
		textButtonStyleEXIT.down = skin.getDrawable("quit_pressed"); //button pressed
		textButtonStyleEXIT.over = skin.getDrawable("quit_pressed");
		
		ButtonStyle textButtonStylePLAY = new ButtonStyle();
		textButtonStylePLAY.up = skin.getDrawable("play"); //button released
		textButtonStylePLAY.down = skin.getDrawable("play_pressed"); //button pressed
		textButtonStylePLAY.over = skin.getDrawable("play_pressed"); //roll over
		textButtonStylePLAY.pressedOffsetY = -1; //move it 1 down when pressed
		//textButtonStylePLAY.fontColor = Color.BLACK; //color the font black
		//textButtonStylePLAY.font = white; //the white bitmap font we created, this probably isnt needed since the text is written on the image
		
		//Credits Buttons
		ButtonStyle textButtonStyleCREDITS = new ButtonStyle();
		textButtonStyleCREDITS.up = skin.getDrawable("credits"); //button released
		textButtonStyleCREDITS.down = skin.getDrawable("credits_pressed");//button pressed'
		textButtonStyleCREDITS.over = skin.getDrawable("credits_pressed"); //roll over
		
		//Button to High Score screen
		ButtonStyle textButtonStyleHIGHSCORE = new ButtonStyle();
		textButtonStyleHIGHSCORE.up = skin.getDrawable("highscores"); //button released
		textButtonStyleHIGHSCORE.down = skin.getDrawable("highscores_pressed");//button pressed'
		textButtonStyleHIGHSCORE.over = skin.getDrawable("highscores_pressed"); //roll over
		
		buttonExit = new Button(textButtonStyleEXIT);
		buttonExit.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				Gdx.app.exit(); //exit the game
			}
		});
		
		buttonPlay = new Button(textButtonStylePLAY);
		buttonPlay.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						menuMusic.stop();
						((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen()); //TODO remove null
						
						disableTouch(); //Disables the buttons
						//dispose(); //breaks the application right now
					}
				})));
			}
		});
		
		buttonCredits = new Button(textButtonStyleCREDITS);
		buttonCredits.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						((Game) Gdx.app.getApplicationListener()).setScreen(new CreditsScreen(backgroundLayer, cloudManager, balloonManager));
						disableTouch(); //Disables the buttons
						//dispose(); //breaks the application right now
					}
				})));
			}
		});
		
		buttonHighScore = new Button(textButtonStyleHIGHSCORE);
		buttonHighScore.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						((Game) Gdx.app.getApplicationListener()).setScreen(new HighscoreScreen(backgroundLayer, cloudManager, balloonManager));
						disableTouch(); //Disables the buttons
						//dispose(); //breaks the application right now
					}
				})));
			}
		});
		
		//creating heading
		LabelStyle headingStyle = new LabelStyle(bananaBrick, new Color(0/255f, 0/255f, 0/255f, .65f));
		
		heading = new Label("Stratofall", headingStyle);
		//heading.setFontScale(3f); //change the text size basically
			
		//putting stuff together
		table.add(heading).padBottom(150); //adds a 150px margin below this cell and the next
		table.row();
		table.add(buttonPlay); //contains our buttons
		table.row(); //adds a row, now buttonExit is below buttonPlay
		table.add(buttonHighScore);
		table.row();
		table.add(buttonCredits);
		table.row();
		table.add(buttonExit);
		//table.debug(); //enables all the debug lines "red lines"
		stage.addActor(table);
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(.5f)));
		
		//creating animations
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        //Makes the heading change colors : probably will be removed
//        Timeline.createSequence().beginSequence()
//        .push(Tween.to(heading, ActorAccessor.RGB, 0.75f).target(0, 0, 1))
//        .push(Tween.to(heading, ActorAccessor.RGB, 0.75f).target(0, 1, 0))
//        .push(Tween.to(heading, ActorAccessor.RGB, 0.75f).target(1, 0, 0))
//        .push(Tween.to(heading, ActorAccessor.ALPHA, 1.5f).target(.5f, .5f, .5f))
//        .push(Tween.to(heading, ActorAccessor.ALPHA, 0.75f).target(.25f, .25f, .25f))
//        .push(Tween.to(heading, ActorAccessor.ALPHA, 0.75f).target(.5f, .5f, .5f))
//        .push(Tween.to(heading, ActorAccessor.ALPHA, 0.75f).target(.75f, .75f, .75f))
//        .push(Tween.to(heading, ActorAccessor.ALPHA, 1.5f).target(1f, 1f, 1f))
//        .end()
//        .repeat(Tween.INFINITY, 0)
//        .start(tweenManager);
        
//        Tween.from(table, ActorAccessor.ALPHA, 1.5f).target(0).start(tweenManager);
//        //Tween.from(table, ActorAccessor.Y, 1.5f).target(Gdx.graphics.getHeight() / 2).start(tweenManager);
//        tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void hide()
	{
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

	private void disableTouch()
	{
		buttonHighScore.setTouchable(Touchable.disabled);
		buttonPlay.setTouchable(Touchable.disabled);
		buttonExit.setTouchable(Touchable.disabled);
	}
	
	@Override
	public void dispose()
	{
		menuMusic.dispose();
		buttonHighScore.setTouchable(Touchable.disabled);
		buttonPlay.setTouchable(Touchable.disabled);
		buttonExit.setTouchable(Touchable.disabled);
		stage.dispose();
		skin.dispose();
		bananaBrick.dispose();
	}

}
