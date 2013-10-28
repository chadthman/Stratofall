package com.me.stratofall.screens;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.me.stratofall.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.me.stratofall.Stratofall;
import com.me.stratofall.objects.Score;

public class FinalScoreScreen implements Screen {

	final Stratofall game;
	OrthographicCamera camera;
	
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table outterTable, innerTable;
	private Button buttonBack;
	private BitmapFont white;
	private Label topHeading, subLabelHeading, playerData;
	private Dialog dialogWindow;
	
	//scoring variables
	private Player player;
	private Score playerScore;
	private String playerName;
	
	//Window variables
	private boolean isPopupWindowUp;
	private Window pauseWindow;
	
	public FinalScoreScreen(final Stratofall game, final Player player)
	{
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
		this.player = player;
		generateScore();
		
	}
	
	private void generateScore()
	{
		//TODO maybe have all the score calculation here
		//cast them to ints since aint no body got time for floats
		playerScore = new Score("You",(int)player.getMetersFallen(), (int)player.getMetersFallen());
	}
	
	private ArrayList<Score> getHighScores()
	{
		//TODO logic for getting the stored highscores probably on some xml or json file
		ArrayList<Score> scores = new ArrayList<Score>();
		//Junk data for now
		scores.add(new Score("Chad", 10000, 2011));
		scores.add(new Score("Shane", 90001, 15));
		
		return scores;
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
			//oldScores.add(score);
			//storeScore(oldScores);
			return true;
		} else {
			for (Score oldScore : oldScores)
			{
				if (score.getScore() > oldScore.getScore())
				{
					//addScore(score); //adds to the data
					return true;
				} else if (score.getScore() == oldScore.getScore()) {
					//addScore(score); //adds to the data
					return true;
				} else {
					return false;
				}
			}
			return false;
		}
	}
	
	private void storeScore(ArrayList<Score> scores)
	{
		//logic here to store the score again back into our data storage
	}
	
	/*
	 * adds the new highscore to the list and removes the smallest to keep 10 in the list
	 */
	private void addScore(Score score)
	{
		int smallestItem = 0;
		int index = 0;
		
		ArrayList<Score> oldScores = getHighScores();
		oldScores.add(score);
		for (Score oldScore : oldScores)
		{
			if (smallestItem > oldScore.getScore())
			{
				smallestItem = oldScore.getScore();
				index = oldScores.indexOf(oldScore);
			}
		}
		oldScores.remove(oldScores.remove(index));
		storeScore(oldScores);
	}
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		
		game.batch.end();

		Table.drawDebug(stage);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {

		isPopupWindowUp = false; //initially sets the back button to active
		stage = new Stage();
		Gdx.input.setInputProcessor(stage); //now we can touch stage objects and get input
		
		atlas = new TextureAtlas("ui/button.pack");
		skin = new Skin(atlas);
		
		outterTable = new Table(skin);
		outterTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		innerTable = new Table(skin);
		innerTable.setBounds(0, 0, outterTable.getMaxWidth(), outterTable.getHeight());
		
		//create font
		white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);
		
		LabelStyle headingStyle = new LabelStyle(white, Color.WHITE);
		
		if (isHighScore(playerScore) == true)
		{
			topHeading = new Label("New High Score!", headingStyle);
			topHeading.setFontScale(2f); //change the text size basically
			outterTable.add(topHeading).padBottom(150); //adds a 150px margin below this cell and the next
			outterTable.row();
		} else {
			topHeading = new Label("Final Score!", headingStyle);
			topHeading.setFontScale(2f); //change the text size basically
			outterTable.add(topHeading).padBottom(150); //adds a 150px margin below this cell and the next
			outterTable.row();
		}
		subLabelHeading = new Label("Player Name", headingStyle);
		subLabelHeading.setScale(2f);
		innerTable.add(subLabelHeading);
		subLabelHeading = new Label("Distance", headingStyle);
		innerTable.add(subLabelHeading);
		subLabelHeading = new Label("Score", headingStyle);
		innerTable.add(subLabelHeading);
		innerTable.row();
		
		if (isHighScore(playerScore) == true)
		{
			isPopupWindowUp = true;
			//Create Pause window
			WindowStyle pauseStyle = new WindowStyle();
			pauseStyle.titleFont = white;
			pauseStyle.background = skin.getDrawable("exit_button");
	//		pauseStyle.stageBackground = skin.getDrawable("blur_background"); //TODO make apha blur over the background.
			
			TextButtonStyle textButtonStyle = new TextButtonStyle();
			textButtonStyle.font = white;
			textButtonStyle.up = skin.getDrawable("play_button");
			textButtonStyle.down = skin.getDrawable("play_button");
			
			pauseWindow = new Window("Please Enter Your Name:", pauseStyle);
			
			TextButton continueButton = new TextButton("Continue", textButtonStyle);
			continueButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					pauseWindow.setVisible(false);
					buttonBack.setTouchable(Touchable.enabled);
					isPopupWindowUp = false;
				}
			});
			
			TextFieldStyle textFieldStyle = new TextFieldStyle();
			textFieldStyle.font = white;
			textFieldStyle.disabledFontColor = Color.DARK_GRAY;
			textFieldStyle.fontColor = Color.ORANGE;
			
			TextField nameField = new TextField("Name", textFieldStyle);
			nameField.setTextFieldListener(new TextFieldListener() {
				public void keyTyped (TextField nameField, char key) {
					if (key == '\n') nameField.getOnscreenKeyboard().show(false);
				}
			});
			
			playerName = nameField.getText();
			
			pauseWindow.padTop(64);
			pauseWindow.add(nameField).minWidth(100).expandX().fillX().colspan(3).row();
			pauseWindow.add(continueButton);
			pauseWindow.pack();
			pauseWindow.setPosition(game.WIDTH/2 - pauseWindow.getWidth()/2, game.HEIGHT/2 - pauseWindow.getHeight()/2);
			pauseWindow.setMovable(false);
		}
		//Only enter the user's name if they have a high score
//		if (isHighScore(playerScore) == true)
//		{
//			//for getting a popup box
//			class GetUserName implements TextInputListener 
//			{
//			   @Override
//			   public void input (String text) {
//				   playerScore.setName(text);
//			   }
//
//			   @Override
//			   public void canceled () {
//			   }
//			}
//			GetUserName listener = new GetUserName();
//			Gdx.input.getTextInput(listener, "Please Enter Your Name:", "Name");
//
//		}
		//Show the player score on the page if they didnt get a high score it just says "You"
		playerData = new Label(playerScore.getName(), headingStyle);
		playerData.setScale(2f);
		innerTable.add(playerData);
		
		playerData = new Label(playerScore.getDistance() + "", headingStyle);
		innerTable.add(playerData);
		
		playerData = new Label(playerScore.getScore() + "" , headingStyle);
		innerTable.add(playerData);
		innerTable.row();
		
		outterTable.add(innerTable); //add the innertable to the outertable
		outterTable.row();
		
		//creating buttons
		ButtonStyle textButtonStyleBACK = new ButtonStyle();
		textButtonStyleBACK.up = skin.getDrawable("back_to_main_menu_button"); //button released
		textButtonStyleBACK.down = skin.getDrawable("back_to_main_menu_button"); //button pressed
		
		buttonBack = new Button(textButtonStyleBACK);
		buttonBack.addListener(new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y)
			{
				game.setScreen(new MainMenuScreen(game));
				dispose();
			}
		});
		
		if (isPopupWindowUp)
		{
			buttonBack.setTouchable(Touchable.disabled);
		}
		
		outterTable.add(buttonBack).padTop(150); //contains our button
		
		stage.addActor(outterTable);
		stage.addActor(pauseWindow);
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
		// TODO Auto-generated method stub
		buttonBack.setTouchable(Touchable.disabled);
		stage.dispose();
		
	}

}
