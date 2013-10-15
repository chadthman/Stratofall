package com.me.stratofall.screens;

import java.util.ArrayList;

import objects.Score;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.me.stratofall.Stratofall;

public class HighscoreScreen implements Screen {

	final Stratofall game;
	OrthographicCamera camera;

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private BitmapFont white;
	private Label heading, scoreText;
	
//	Preferences prefs = Gdx.app.getPreferences("my-preferences");
	
	public HighscoreScreen(final Stratofall game)
	{
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
	}
	
	public ArrayList<Score> getHighScores()
	{
		//TODO logic for getting the stored highscores probably on some xml or json file
		ArrayList<Score> scores = new ArrayList<Score>();
		//Junk data for now
		scores.add(new Score("Chad", 10000, 2011));
		scores.add(new Score("Shane", 2, 15));
		
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
		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		//create font
		white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);
		
		LabelStyle headingStyle = new LabelStyle(white, Color.WHITE);
		
		heading = new Label("High Scores", headingStyle);
		heading.setFontScale(3f); //change the text size basically
		
		table.add(heading).padBottom(150); //adds a 150px margin below this cell and the next
		table.row();
		
		ArrayList<Score> scores = getHighScores();
		
		for (Score score : scores)
		{
			scoreText = new Label(score.getName() + "\t" + score.getDistance() + "\t" + score.getScore(), headingStyle);
			scoreText.setScale(2f);
			table.add(scoreText);
			table.row();
		}
		
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
		
	}

}
