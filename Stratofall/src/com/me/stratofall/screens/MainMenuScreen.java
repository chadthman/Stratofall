package com.me.stratofall.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.stratofall.Stratofall;

public class MainMenuScreen implements Screen
{
	final Stratofall game;
	OrthographicCamera camera;

	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private Button buttonPlay, buttonExit, buttonHighScore;
	private BitmapFont white;
	private Label heading; 
	

	public MainMenuScreen(final Stratofall game)
	{
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.WIDTH, game.HEIGHT);

	}

	@Override
	public void render(float delta)
	{
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
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void show()
	{
		stage = new Stage();
		
		Gdx.input.setInputProcessor(stage); //now we can touch stage objects and get input
		
		atlas = new TextureAtlas("ui/button.pack");
		skin = new Skin(atlas);
		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		//create font
		white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);
		
		//creating buttons
		ButtonStyle textButtonStyleEXIT = new ButtonStyle();
		textButtonStyleEXIT.up = skin.getDrawable("exit_button"); //button released
		textButtonStyleEXIT.down = skin.getDrawable("exit_button"); //button pressed

		ButtonStyle textButtonStylePLAY = new ButtonStyle();
		textButtonStylePLAY.up = skin.getDrawable("play_button"); //button released
		textButtonStylePLAY.down = skin.getDrawable("play_button"); //button pressed
		textButtonStylePLAY.pressedOffsetY = -1; //move it 1 down when pressed
		//textButtonStylePLAY.fontColor = Color.BLACK; //color the font black
		//textButtonStylePLAY.font = white; //the white bitmap font we created, this probably isnt needed since the text is written on the image
		
		//Button to High Score screen
		ButtonStyle textButtonStyleHIGHSCORE = new ButtonStyle();
		textButtonStyleHIGHSCORE.up = skin.getDrawable("high_score_button"); //button released
		textButtonStyleHIGHSCORE.down = skin.getDrawable("high_score_button");//button pressed
		
		buttonExit = new Button(textButtonStyleEXIT);
		buttonExit.addListener(new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y)
			{
				Gdx.app.exit(); //exit the game
			}
		});
		
		buttonPlay = new Button(textButtonStylePLAY);
		buttonPlay.addListener(new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y)
			{
				game.setScreen(new GameScreen(game));
				dispose();
			}
		});
		
		buttonHighScore = new Button(textButtonStyleHIGHSCORE);
		buttonHighScore.addListener(new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y)
			{
				game.setScreen(new HighscoreScreen(game));
				dispose();
			}
		});
		
		//creating heading
		LabelStyle headingStyle = new LabelStyle(white, Color.WHITE);
		
		heading = new Label("Stratofall", headingStyle);
		heading.setFontScale(3f); //change the text size basically
			
		//putting stuff together
		table.add(heading).padBottom(150); //adds a 150px margin below this cell and the next
		table.row();
		table.add(buttonPlay); //contains our buttons
		table.row(); //adds a row, now buttonExit is below buttonPlay
		table.add(buttonExit);
		table.row();
		table.add(buttonHighScore);
		table.debug(); //enables all the debug lines "red lines"
		stage.addActor(table);
		
		
	}

	@Override
	public void hide()
	{
		// TODO Auto-generated method stub

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
		Gdx.input.setInputProcessor(null);
		stage.dispose();
	}

}
