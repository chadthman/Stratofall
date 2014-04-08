package com.me.stratofall.screens;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.XmlWriter;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;
import com.me.stratofall.objects.balloons.BalloonManager;
import com.me.stratofall.objects.clouds.CloudManager;
import com.me.stratofall.utils.GlobalScore;
import com.me.stratofall.utils.Score;
import com.me.stratofall.utils.ScrollingLayer;

/*
 * Stackmob has been deprecated so the commented out code is made for the global scores using Stackmob
 * Until a replacement has been found global scores are not used.
 */

public class FinalScoreScreen implements Screen {

	//Main variables
	private Stage stage;
	private TextureAtlas buttonAtlas, textureAtlas;
	private Skin buttonSkin, textureSkin;
	private Table outerTable, innerTable;
	private Button windowButtonContinue, buttonContinue;
	private Label topHeading, subLabelHeading, playerData;
	private BitmapFont bananaBrick, bananaBrickMenu, bananaBrickHeader;
	
	//scoring variables
	private Player player;
	private Score playerScore;
	
	//Window variables
	private Window enterNameWindow;
	private TextField nameField;
	
	//Background variables
	private CloudManager cloudManager;
	private BalloonManager balloonManager;
	private ScrollingLayer backgroundLayer;
	private Texture backgroundTexture;
	private Window globalHighScoreWindow;
	private Button globalHighScoreButtonContinue;
	
	public FinalScoreScreen(final Player player)
	{
		this.player = player;
		generateScore();
	}
	
	private void generateScore()
	{
		playerScore = new Score("You", player.getMetersFallen(), player.getFinalScore());
	}
	
	private void setPlayerName(String name)
	{
		playerScore.setName(name);
	}
	
	private ArrayList<Score> getHighScores()
	{
		ArrayList<Score> scores = new ArrayList<Score>();
		FileHandle highScoresXMLFile = Gdx.files.local("data/highscores.xml");
		
		if (!highScoresXMLFile.exists()) {
			try {
				highScoresXMLFile.file().getParentFile().mkdirs();
				highScoresXMLFile.file().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			if (!highScoresXMLFile.readString().equals("")) //in case no scores have been added
			{
				XmlReader reader = new XmlReader();
				Element root = reader.parse(highScoresXMLFile.readString());
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
	
	private void storeScore(ArrayList<Score> scores)
	{
		Collections.sort(scores);
		StringWriter writer = new StringWriter();
		XmlWriter xml = new XmlWriter(writer);
		try {
			xml.element("players");
			for (Score score : scores)
			{
		 		xml.element("person")
			        .attribute("name", score.getName())
			        .attribute("distance", score.getDistance())
			        .attribute("score", score.getScore())
			    .pop();
			}
			xml.pop();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileHandle file = Gdx.files.local("data/highscores.xml");
		file.writeString(writer.toString(), false);
	}
	
	/*
	 * if the highscore total is less than 10 then store the score immediately
	 * else check if score is larger than the other scores. Store if true
	 */
	
	private boolean isHighScore(Score score)
	{
		ArrayList<Score> oldScores = getHighScores();
		if (oldScores.size() < 10)
		{
			return true;
		} else {
			for (Score oldScore : oldScores)
			{
				if (score.getScore() >= oldScore.getScore())
				{
					 return true;
				}
			}
			return false;
		}
	}
	
	/*
	 * adds the new highscore to the list and removes the smallest to keep 10 in the list
	 */
	private void addScore(Score score)
	{
		int smallestItem = 0;
		int index = 0;
		
		ArrayList<Score> oldScores = getHighScores();
		if (oldScores.size() < 10)
		{
			oldScores.add(score);
			storeScore(oldScores);
		} else {
			oldScores.add(score);
			smallestItem = oldScores.get(0).getScore();
			for (Score oldScore : oldScores)
			{
				if (smallestItem >= oldScore.getScore())
				{
					smallestItem = oldScore.getScore();
					index = oldScores.indexOf(oldScore);
				}
			}
			oldScores.remove(oldScores.remove(index));
			storeScore(oldScores);
		}
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
		
		//Inner table for holding score information and buttons
		innerTable = new Table(buttonSkin);
		innerTable.setBounds(0, 0, outerTable.getMaxWidth(), outerTable.getHeight());
		
		//create fonts
		bananaBrick = Stratofall.generateFont("fonts/BBrick.ttf", 80, Color.BLACK);
		bananaBrickMenu = Stratofall.generateFont("fonts/BBrick.ttf", 50, Color.BLACK);
		bananaBrickHeader = Stratofall.generateFont("fonts/BBrick.ttf", 108, Color.BLACK);
		
		//Start building the header
		LabelStyle blackStyleHeaderLarge = new LabelStyle(bananaBrickHeader, new Color(0f, 0f, 0f, .65f)); //Black
		LabelStyle blackStyleHeaderSmall = new LabelStyle(bananaBrick, new Color(0f, 0f, 0f, .65f)); //Black
		LabelStyle blackStyle = new LabelStyle(bananaBrick, new Color(0f, 0f, 0f, .75f)); //Black
		LabelStyle whiteStyle = new LabelStyle(bananaBrick, new Color(1f, 1f, 1f, .75f)); //White
		
		//if is a high score show use the correct label
		if (isHighScore(playerScore) == true)
		{
			topHeading = new Label("High Score!", blackStyleHeaderLarge);
		} else {
			topHeading = new Label("Final Score!", blackStyleHeaderLarge);
		}
		
		//Add it to the outer table
		outerTable.add(topHeading).padBottom(150); //adds a 150px margin below this cell and the next
		outerTable.row();
		
		//Print the Distance
		subLabelHeading = new Label("Distance ", whiteStyle);
		innerTable.add(subLabelHeading).row();
		playerData = new Label(playerScore.getDistance() + "", whiteStyle);
		innerTable.add(playerData).padBottom(100).row();
		
		//Print the Score
		subLabelHeading = new Label("Score", blackStyle);
		innerTable.add(subLabelHeading).row();
		playerData = new Label(playerScore.getScore() + "", blackStyle);
		innerTable.add(playerData).row();
		
		
		innerTable.row();
		
		//add the inner table to the outer table
		outerTable.add(innerTable);
		outerTable.row();
		
		//Continue button style
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = bananaBrickMenu;
		textButtonStyle.up = buttonSkin.getDrawable("continue");
		textButtonStyle.down = buttonSkin.getDrawable("continue_pressed");
		textButtonStyle.over = buttonSkin.getDrawable("continue_pressed");
		
		//Create the continue button
		buttonContinue = new Button(textButtonStyle);
		buttonContinue.setTouchable(Touchable.enabled);
		buttonContinue.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if(isHighScore(playerScore))
				{
					stage.clear();
					stage.addActor(backgroundLayer);
					stage.addActor(balloonGroup);
					stage.addActor(cloudGroup);
					stage.addActor(enterNameWindow);
					stage.setKeyboardFocus(nameField);
					stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
					Gdx.input.setOnscreenKeyboardVisible(true);
				}
				else
				{
					stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
						
						@Override
						public void run() {
							((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
							dispose();
						}
					})));
				}
			}
		});
		
		//Add button to outer table
		outerTable.add(buttonContinue).padTop(150);
		
		//Finish stage
		stage.addActor(outerTable);
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
		
		//High Score Window creation
		////////////////////////////////////////////////////////////////////////////////////////////
		
		//Create the window style
		WindowStyle windowStyle = new WindowStyle();
		windowStyle.titleFont = bananaBrickMenu;
		
		//Create the window
		enterNameWindow = new Window("Please enter a name", windowStyle);
		
		//Create the text field style
		TextFieldStyle textFieldStyle = new TextFieldStyle();
		textFieldStyle.font = bananaBrickMenu;
		textFieldStyle.disabledFontColor = Color.DARK_GRAY;
		textFieldStyle.fontColor = Color.ORANGE;
		textFieldStyle.background = textureSkin.getDrawable("textfield");
		textFieldStyle.cursor = textureSkin.getDrawable("textfield");
		
		//Create the text field
		nameField = new TextField("", textFieldStyle);
		nameField.setMaxLength(8); //8 character max length name
		nameField.setBlinkTime(.5f);
		nameField.setColor(Color.CYAN);
		nameField.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped (TextField nameField, char key) 
			{
				if (key == '\n') nameField.getOnscreenKeyboard().show(false);
			}
		});
		
		//Create the continue button style
		ButtonStyle textButtonStyleCONTINUE = new ButtonStyle();
		textButtonStyleCONTINUE.up = buttonSkin.getDrawable("continue"); //button released
		textButtonStyleCONTINUE.down = buttonSkin.getDrawable("continue_pressed"); //button pressed
		textButtonStyleCONTINUE.over = buttonSkin.getDrawable("continue_pressed"); //button hover
		
		//Create the continue button
		windowButtonContinue = new Button(textButtonStyleCONTINUE);
		windowButtonContinue.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				setPlayerName(nameField.getText());
				addScore(playerScore);
				Gdx.input.setOnscreenKeyboardVisible(false);
//				if(isGlobalHighScore(playerScore))
//				{
//					addGlobalScore(playerScore);
//					stage.clear();
//					stage.addActor(backgroundLayer);
//					stage.addActor(balloonGroup);
//					stage.addActor(cloudGroup);
//					stage.addActor(globalHighScoreWindow);
//					stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f)));
//				} else {
					stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
						
						@Override
						public void run() {
							((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
							dispose();
						}
					})));
				//}
			}
		});
		
		//Set up the window layout
		enterNameWindow.setBounds(0, 0, 500, 500);
		enterNameWindow.add(nameField).padBottom(75).minWidth(100).expandX().fillX().colspan(3).row();
		enterNameWindow.add(windowButtonContinue).expandX().align(Align.center).row();
		enterNameWindow.setPosition(Stratofall.WIDTH/2 - enterNameWindow.getWidth()/2, 100 + (Stratofall.HEIGHT/2 - enterNameWindow.getHeight()/2));
		enterNameWindow.setMovable(false);
		
		//Stackmob global high scores window
		//////////////////////////////////////////////////////////////////////////////////////////////
		
		//Create the window
		globalHighScoreWindow = new Window("New Global High Score!", windowStyle);
		
		//Create the continue button
		globalHighScoreButtonContinue = new Button(textButtonStyleCONTINUE);
		globalHighScoreButtonContinue.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
//				setPlayerName(nameField.getText());
//				addScore(playerScore);
//				Gdx.input.setOnscreenKeyboardVisible(false);
				stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
					
					@Override
					public void run() {
						((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
						dispose();
					}
				})));
			}
		});
		
		globalHighScoreWindow.setBounds(0, 0, 500, 500);
		globalHighScoreWindow.add(globalHighScoreButtonContinue).expandX().align(Align.center).row();
		
	}
	
//	private void addGlobalScore(Score score)
//	{
//		int smallestItem = 0;
//		int index = 0;
//		
//		ArrayList<Score> oldScores = getGlobalHighScores();
//		if (oldScores.size() < 10)
//		{
//			oldScores.add(score);
//			storeGlabalScore(oldScores);
//		} else {
//			oldScores.add(score);
//			smallestItem = oldScores.get(0).getScore();
//			for (Score oldScore : oldScores)
//			{
//				if (smallestItem >= oldScore.getScore())
//				{
//					smallestItem = oldScore.getScore();
//					index = oldScores.indexOf(oldScore);
//				}
//			}
//			oldScores.remove(oldScores.remove(index));
//			storeGlobalScore(oldScores);
//		}
//	}
//	
//	private void storeGlobalScore(ArrayList<Score> score)
//	{
//		GlobalScore globalScore = new GlobalScore(score);
//		globalScore.save();
//	}
	
//	private boolean isGlobalHighScore(Score score)
//	{
//		ArrayList<Score> oldScores = getGlobalHighScores();
//		if (oldScores.size() < 10)
//		{
//			return true;
//		} else {
//			for (Score oldScore : oldScores)
//			{
//				if (score.getScore() >= oldScore.getScore())
//				{
//					 return true;
//				}
//			}
//			return false;
//		}
//	}
	
//	ArrayList<Score> globalScores;
//	private ArrayList<Score> getGlobalHighScores()
//	{
//		
//		GlobalScore globalScore = new GlobalScore();
//		
//		
////		GlobalScore.query(GlobalScore.class, new StackMobQuery(), new StackMobQueryCallback<GlobalScore>() {
////
////			@Override
////			public void failure(StackMobException arg0) {
////				// TODO Auto-generated method stub
////				
////			}
////
////			@Override
////			public void success(List<Score> scores) {
////				globalScores = (ArrayList<Score>) scores;
////			}
////
////			@Override
////			public void success(List<GlobalScore> arg0) {
////				// TODO Auto-generated method stub
////				
////			}
////		});
//		return globalScores;
//		
//	}

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
//		windowButtonContinue.setTouchable(Touchable.disabled);
//		buttonContinue.setTouchable(Touchable.disabled);
		stage.dispose();
		bananaBrick.dispose();
		bananaBrickMenu.dispose();
		bananaBrickHeader.dispose();
	}

}
