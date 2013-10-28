package com.me.stratofall.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.stratofall.Stratofall;
import com.me.stratofall.objects.Score;

public class HighscoreScreen implements Screen {

	final Stratofall game;
	OrthographicCamera camera;

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table outterTable, innerTable;
	private Button buttonBack;
	private BitmapFont white;
	private Label topHeading, subLabelHeading, playerScore, playerName, playerDistance;
	
//	Preferences prefs = Gdx.app.getPreferences("my-preferences");
	
	public HighscoreScreen(final Stratofall game)
	{
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
	}
	
	private ArrayList<Score> getHighScores()
	{
		//TODO logic for getting the stored highscores probably on some xml or json file
		ArrayList<Score> scores = new ArrayList<Score>();
		//Junk data for now
		scores.add(new Score("Chad", 99999, 2011));
		scores.add(new Score("Shane", 90001, 15));
		
		return scores;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
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
		
		topHeading = new Label("High Scores", headingStyle);
		topHeading.setFontScale(3f); //change the text size basically
		
		outterTable.add(topHeading).padBottom(150); //adds a 150px margin below this cell and the next
		outterTable.row();
		
		//Labels the categories of the score fields
		subLabelHeading = new Label("Player Name", headingStyle);
		subLabelHeading.setScale(2f);
		innerTable.add(subLabelHeading);
		subLabelHeading = new Label("Distance", headingStyle);
		innerTable.add(subLabelHeading);
		subLabelHeading = new Label("Score", headingStyle);
		innerTable.add(subLabelHeading);
		innerTable.row();
		
		ArrayList<Score> scores = getHighScores();
		
		//Prints out the scores of the players
		for (Score score : scores)
		{
			playerName = new Label(score.getName(), headingStyle);
			playerName.setScale(2f);
			innerTable.add(playerName);
			
			playerDistance = new Label(score.getDistance() + "", headingStyle);
			playerDistance.setScale(2f);
			innerTable.add(playerDistance);
			
			playerScore = new Label(score.getScore() + "" , headingStyle);
			playerScore.setScale(2f);
			innerTable.add(playerScore);
			
			innerTable.row();
		}
		//Just for debugging
		outterTable.debug();
		innerTable.debug();
		
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
		
		outterTable.add(buttonBack).padTop(150); //contains our button
		
		stage.addActor(outterTable);
		
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
