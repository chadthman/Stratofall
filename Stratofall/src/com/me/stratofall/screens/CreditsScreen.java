package com.me.stratofall.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.me.stratofall.Stratofall;
import com.me.stratofall.objects.balloons.BalloonManager;
import com.me.stratofall.objects.clouds.CloudManager;
import com.me.stratofall.utils.ScrollingLayer;

//Credits structure:
///////////////////////////////////////////////
// Stratofall is Copyright 2013 Shane Israel, 
// 			  Chad Marmon
// Programming: Shane Israel, Chad Marmon
// Artwork:	    Shane Israel
// Game Music: Go Cart - CC by Incompetech.com
///////////////////////////////////////////////

public class CreditsScreen implements Screen{

	private CloudManager cloudManager;
	private BalloonManager balloonManager;
	private ScrollingLayer backgroundLayer;
	private Stage stage;
	private Texture backgroundTexture;
	private TextureAtlas buttonAtlas;
	private Skin buttonSkin;
	private TextureAtlas textureAtlas;
	private Skin textureSkin;
	private Table outerTable;
	private Table innerTable;
	private BitmapFont bananaBrick;
	private BitmapFont bananaBrickHeader;
	private Label topHeading;
	private Label creditsLabel;
	private Button buttonBack;
	private Viewport viewport;

	public CreditsScreen(ScrollingLayer background, CloudManager cloudMan, BalloonManager balloonMan) 
	{
		cloudManager = cloudMan;
		balloonManager = balloonMan;
		backgroundLayer = background;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cloudManager.spawnMenuClouds();
		balloonManager.update();
		stage.act(delta);
		stage.draw();
		//Table.drawDebug(stage);
		// tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		//stage.setViewport(Stratofall.WIDTH, Stratofall.HEIGHT);
	}

	@Override
	public void show() {
		//Set up stage
		stage = new Stage(new StretchViewport(Stratofall.WIDTH, Stratofall.HEIGHT));
		//stage = new Stage(Stratofall.WIDTH, Stratofall.HEIGHT);
		Gdx.input.setInputProcessor(stage); //now we can touch stage objects and get input
		
		//For scrolling background
		backgroundTexture = Stratofall.assets.get("backgrounds/background.jpg", Texture.class);
		final Group cloudGroup = new Group();
		final Group balloonGroup = new Group();
		cloudManager = new CloudManager(cloudGroup);
		balloonManager = new BalloonManager(balloonGroup);
		backgroundLayer = new ScrollingLayer(backgroundTexture, .85f);
		
		//Add background layer to stage
		stage.addActor(backgroundLayer);
		stage.addActor(balloonGroup);
		stage.addActor(cloudGroup);
		
		//Import texture atlas
		buttonAtlas = new TextureAtlas("ui/buttons.pack");
		buttonSkin = new Skin(buttonAtlas);
		textureAtlas = new TextureAtlas("textures/textures.pack");
		textureSkin = new Skin(textureAtlas);
		
		//Outer table to hold the main table which includes the header
		outerTable = new Table(buttonSkin);
		outerTable.setBounds(0, 0,  Stratofall.WIDTH, Stratofall.HEIGHT);
		
		//Inner table for holding the credits
		innerTable = new Table(buttonSkin);
		innerTable.setBounds(0, 0, outerTable.getMaxWidth(), outerTable.getHeight());
		
		//create fonts
		bananaBrick = Stratofall.generateFont("fonts/BBrick.ttf", 28, Color.BLACK);
		bananaBrickHeader = Stratofall.generateFont("fonts/BBrick.ttf", 108, Color.BLACK);
		
		//Start building the header and credits
		LabelStyle blackStyleHeaderLarge = new LabelStyle(bananaBrickHeader, new Color(0f, 0f, 0f, .65f)); //Black
		LabelStyle blackStyle = new LabelStyle(bananaBrick, new Color(0f, 0f, 0f, .75f)); //Black
		LabelStyle whiteStyle = new LabelStyle(bananaBrick, new Color(1f, 1f, 1f, .75f)); //White
		
		//Create the header
		topHeading = new Label("Credits", blackStyleHeaderLarge);
		outerTable.add(topHeading).padBottom(150).row(); //adds a 150px margin below this cell and the next
		
		//Create the credits with First word black
		creditsLabel = new Label("Stratofall ", blackStyle);
		innerTable.add(creditsLabel).expandX().align(Align.right);
		
		creditsLabel = new Label("is Copyright 2013 Shane Israel,", whiteStyle);
		innerTable.add(creditsLabel).expandX().align(Align.left).row().padBottom(20f);
		innerTable.add(new Label("", blackStyle));//dummy column
		creditsLabel = new Label("Chad Marmon", whiteStyle);
		innerTable.add(creditsLabel).expandX().align(Align.left).row().padBottom(20f);
		
		creditsLabel = new Label("Programming: ", blackStyle);
		innerTable.add(creditsLabel).expandX().align(Align.right);
		
		creditsLabel = new Label("Shane Israel, Chad Marmon", whiteStyle);
		innerTable.add(creditsLabel).expandX().align(Align.left).row().padBottom(20f);;
		
		creditsLabel = new Label("Artwork: ", blackStyle);
		innerTable.add(creditsLabel).expandX().align(Align.right);
		
		creditsLabel = new Label("Shane Israel", whiteStyle);
		innerTable.add(creditsLabel).expandX().align(Align.left).row().padBottom(20f);
		
		creditsLabel = new Label("Game Music: ", blackStyle);
		innerTable.add(creditsLabel).expandX().align(Align.right);
		
		creditsLabel = new Label("Go Cart - CC by Incompetech.com", whiteStyle);
		innerTable.add(creditsLabel).expandX().align(Align.left).row();
		
		//add the inner table to the outer table
		//innerTable.debug();
		outerTable.add(innerTable);
		outerTable.row();
		
		//back button style
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = bananaBrick;
		textButtonStyle.up = buttonSkin.getDrawable("back");
		textButtonStyle.down = buttonSkin.getDrawable("back_pressed");
		textButtonStyle.over = buttonSkin.getDrawable("back_pressed");
		
		//Create the continue button
		buttonBack = new Button(textButtonStyle);
		buttonBack.setTouchable(Touchable.enabled);
		buttonBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
						dispose();
					}
				})));
			}
		});
		
		//Add button to outer table
		outerTable.add(buttonBack).padTop(150);
		
		//Finish stage
		stage.addActor(outerTable);
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
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
	public void dispose() {
		stage.dispose();
		bananaBrick.dispose();
		bananaBrickHeader.dispose();
	}

}
