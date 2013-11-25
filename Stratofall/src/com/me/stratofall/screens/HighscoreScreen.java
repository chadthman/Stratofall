package com.me.stratofall.screens;

import java.io.IOException;
import java.util.ArrayList;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.me.stratofall.Stratofall;
import com.me.stratofall.objects.balloons.BalloonManager;
import com.me.stratofall.objects.clouds.CloudManager;
import com.me.stratofall.utils.Score;
import com.me.stratofall.utils.ScrollingLayer;

public class HighscoreScreen implements Screen 
{
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table outterTable, innerTable;
	private Button buttonBack;
	private Label topHeading, subLabelHeading, playerScore, playerName,
			playerDistance;
	private TweenManager tweenManager;

	private ScrollingLayer backgroundLayer;
	private CloudManager cloudManager;
	private BalloonManager balloonManager;
	
	private Texture backgroundImage;
	private BitmapFont bananaBrick, xolonium;

	public HighscoreScreen(ScrollingLayer background, CloudManager cloudMan, BalloonManager balloonMan) 
	{
		cloudManager = cloudMan;
		balloonManager = balloonMan;
		backgroundLayer = background;
	}

	private ArrayList<Score> getHighScores() {
		ArrayList<Score> scores = new ArrayList<Score>();
		FileHandle highScoresXmlFile = Gdx.files.local("data/highscores.xml");
		
		if (!highScoresXmlFile.exists()) {
			try {
				highScoresXmlFile.file().getParentFile().mkdirs();
				highScoresXmlFile.file().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (!highScoresXmlFile.readString().equals("")) //in case no scores have been added
			{
				XmlReader reader = new XmlReader();
				Element root = reader.parse(highScoresXmlFile.readString());
				Array<Element> items = root.getChildrenByName("person");
				for (Element child : items) {
					String name = child.getAttribute("name");
					int distance = child.getInt("distance");
					int score = child.getInt("score");
					scores.add(new Score(name, distance, score));
				}
			}
		}

		// Junk data for now
//		 scores.add(new Score("Chad", 99999, 25649));
//		 scores.add(new Score("Shane", 90001, 24052));
//		 scores.add(new Score("Talon", 6547, 18005));
//		 scores.add(new Score("Dionne", 4500, 9567));
//		 scores.add(new Score("Broseph", 1337, 4810));

		return scores;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cloudManager.spawnMenuClouds();
		balloonManager.update();
		stage.act(delta);
		stage.draw();
		// tweenManager.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(Stratofall.WIDTH, Stratofall.HEIGHT);
	}

	@Override
	public void show() {
		stage = new Stage(Stratofall.WIDTH, Stratofall.HEIGHT);
		Gdx.input.setInputProcessor(stage); // now we can touch stage objects
											// and get input
		atlas = new TextureAtlas("ui/buttons.pack");
		skin = new Skin(atlas);

		outterTable = new Table(skin);
		outterTable.setBounds(0, 0,  Stratofall.WIDTH, Stratofall.HEIGHT);

		innerTable = new Table(skin);
		innerTable.setBounds(0, 0, outterTable.getMaxWidth(),
				outterTable.getHeight());

		// create font
		bananaBrick = Stratofall.generateFont("fonts/BBrick.ttf", 108,
				Color.BLACK);
		xolonium = Stratofall.generateFont("fonts/Xolonium.otf", 108, Color.BLACK);

		LabelStyle titleStyle = new LabelStyle(bananaBrick, new Color(0f, 0f,
				0f, .65f));
		LabelStyle headingStyle = new LabelStyle(bananaBrick, new Color(1f, 1f,
				1f, .75f));
		LabelStyle distanceStyle = new LabelStyle(bananaBrick, new Color(0f,
				0f, 0f, .75f));

		topHeading = new Label("High Scores", titleStyle);
		// topHeading.setFontScale(3f); //change the text size basically

		outterTable.add(topHeading).padBottom(100); // adds a 150px margin below
													// this cell and the next
		outterTable.row();

		// Labels the categories of the score fields
		float subLabelHeadingScale = 0.5f;
		float scoresHeadingScale = 0.3f;

		subLabelHeading = new Label("Name", headingStyle);
		subLabelHeading.setFontScale(subLabelHeadingScale);
		innerTable.add(subLabelHeading).padRight(30);
		subLabelHeading = new Label("Distance", distanceStyle);
		subLabelHeading.setFontScale(subLabelHeadingScale);
		innerTable.add(subLabelHeading).padRight(30);
		subLabelHeading = new Label("Score", headingStyle);
		subLabelHeading.setFontScale(subLabelHeadingScale);
		innerTable.add(subLabelHeading);
		innerTable.left();
		innerTable.row();

		ArrayList<Score> scores = getHighScores();

		// Prints out the scores of the players
		for (Score score : scores) {
			playerName = new Label(score.getName(), headingStyle);
			playerName.setFontScale(scoresHeadingScale);
			innerTable.add(playerName).padRight(30);
			;

			playerDistance = new Label(score.getDistance() + " Meters",
					distanceStyle);
			playerDistance.setFontScale(scoresHeadingScale);
			innerTable.add(playerDistance).padRight(30);
			;

			playerScore = new Label(score.getScore() + "", headingStyle);
			playerScore.setFontScale(scoresHeadingScale);
			innerTable.add(playerScore);

			innerTable.row();
		}
		// Just for debugging
		// outterTable.debug();
		// innerTable.debug();

		outterTable.add(innerTable); // add the innertable to the outertable
		outterTable.row();

		// creating buttons
		ButtonStyle textButtonStyleBACK = new ButtonStyle();
		textButtonStyleBACK.up = skin.getDrawable("back"); // button released
		textButtonStyleBACK.down = skin.getDrawable("back_pressed"); // button
																		// pressed
		textButtonStyleBACK.over = skin.getDrawable("back_pressed"); // button
																		// pressed

		buttonBack = new Button(textButtonStyleBACK);
		buttonBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addAction(Actions.sequence(Actions.fadeOut(0.5f),
						Actions.run(new Runnable() {

							@Override
							public void run() {
								((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen()); //TODO remove null
								disableTouch();
								dispose();
								// dispose(); //breaks the application right now
							}
						})));

			}
		});

		outterTable.add(buttonBack).padTop(60); // contains our button
		
		stage.addActor(backgroundLayer);
		stage.addActor(balloonManager.getBalloonGroup());
		stage.addActor(cloudManager.getCloudGroup());
		stage.addActor(outterTable);
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));

		// creating animations
		// tweenManager = new TweenManager();
		// Tween.registerAccessor(Actor.class, new ActorAccessor());

		// Tween.from(outterTable, ActorAccessor.ALPHA,
		// 1.5f).target(0).start(tweenManager);
		// tweenManager.update(Gdx.graphics.getDeltaTime());
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

	private void disableTouch() {
		buttonBack.setTouchable(Touchable.disabled);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		buttonBack.setTouchable(Touchable.disabled);
		stage.dispose();
		bananaBrick.dispose();

	}

}
