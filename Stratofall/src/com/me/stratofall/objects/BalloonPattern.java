package com.me.stratofall.objects;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public class BalloonPattern
{
	private Player player;
	private final Stage stage; //this stage is a reference to the main stage, not a new one. 
	
	private int rows = 5; //all patterns must be 5 by 5
	private int cols = 5;
	
	private int[][] line_center = {{0,0,1,0,0}, //middle line pattern
								{0,0,1,0,0},
								{0,0,1,0,0},
								{0,0,1,0,0},
								{0,0,1,0,0}};

	private int[][] diamond = {{0,0,1,0,0}, //diamond pattern
								{0,1,0,1,0},
								{1,0,2,0,1},
								{0,1,0,1,0},
								{0,0,1,0,0}};
	
	private int[][] heart = {{0,1,0,1,0}, //heart pattern
								{1,1,1,1,1},
								{1,1,2,1,1},
								{0,1,1,1,0},
								{0,0,1,0,0}};
	
	private int[][] upvote = {{0,0,1,0,0}, //upvote pattern
								{0,1,1,1,0},
								{1,1,1,1,1},
								{0,0,1,0,0},
								{0,0,1,0,0}};
	
	private int[][] scatter = {{0,1,0,1,0}, //scatter pattern
								{1,0,1,0,1},
								{0,1,0,1,0},
								{1,0,1,0,1},
								{0,1,0,1,0}};
	
	private int[][] sadface = {{0,0,0,0,0}, //sadface pattern
								{0,2,0,2,0},
								{0,0,0,0,0},
								{0,1,1,1,0},
								{1,0,0,0,1}};
	
	private int[][] TIE_fighter = {{1,0,0,0,1}, //TIE fighter pattern
								{1,0,1,0,1},
								{1,1,1,1,1},
								{1,0,1,0,1},
								{1,0,0,0,1}};
	
	private int[][] line_left = {{1,0,0,0,0}, //left column pattern
								{1,0,0,0,0},
								{1,0,0,0,0},
								{1,0,0,0,0},
								{1,0,0,0,0}};
	
	private int[][] line_right = {{0,0,0,0,1}, //right coloumn pattern
								{0,0,0,0,1},
								{0,0,0,0,1},
								{0,0,0,0,1},
								{0,0,0,0,1}};

	private int[][] currentPattern;
	private HashMap<String, int[][]> patternMap;
	
	ArrayList<String> patternArray;
	
	private float x_loc;
	private float x_pattern; //the patterns x location
	private float y_pattern; //the patterns y location
	private float y_loc = 0;
	private float balloonWidth = 64;
	private float lastSpawnTime = TimeUtils.nanoTime();
	private float spawnTime;
	
	private Group balloonGroup;
	
	/**
	 * 
	 * @param p Reference to the {@link Player}
	 * @param stage Reference to the parent {@link Stage}
	 * @param time Interval in second before a new pattern is created
	 */
	public BalloonPattern(Player p, Stage stage, int time)
	{
		this.player = p;
		this.stage = stage;
		spawnTime = time * 1000000000f;
		
		patternMap = new HashMap<String, int[][]>();
		
		//add patterns to map
		patternMap.put("line",line_center);
		patternMap.put("line_right", line_right);
		patternMap.put("line_left", line_left);
		patternMap.put( "diamon",diamond);
		patternMap.put("heart",heart);
		patternMap.put("sadface",sadface);
		patternMap.put("upvote",upvote);
		patternMap.put("scatter",scatter);
		patternMap.put("TIE fighter",TIE_fighter);
		
		patternArray = new ArrayList<String>(patternMap.keySet());
		
		summon();
	}
	public void summon()
	{
		if(TimeUtils.nanoTime() - lastSpawnTime > spawnTime)
		{
			x_pattern = MathUtils.random(Stratofall.WIDTH - (cols * balloonWidth));
			y_pattern = 0; //this will change depending on how many rows of the pattern contain a coin
			
			setPattern();
			lastSpawnTime = TimeUtils.nanoTime();
		}
	}
	private void setPattern() 
	{
		currentPattern = patternMap.get(patternArray.get(MathUtils.random(patternArray.size()-1)));
		createBalloons();
	}
	private void createBalloons() 
	{
		/*
		 * the size of our coins is 64 so we need to make sure each coin in the
		 * lit is offset by 64 and add them to the list.
		 */
		Group temp = balloonGroup;
		
		balloonGroup = new Group();
		if(stage.getActors().contains(balloonGroup, true))
		{
			System.out.println("Does not contain a balloon group");
		}
		else
		{
			stage.getActors().removeValue(temp, true);
			stage.addActor(balloonGroup);
		}
		
		for(int i = 0; i < rows; i++)
		{
			x_loc = x_pattern;
			for(int j = 0; j < cols; j++)
			{
				if(currentPattern[i][j] == 1)
				{
					Balloon balloon = new RedBalloon(player, x_loc, y_loc);
					balloonGroup.addActor(balloon);
				}
				else if(currentPattern[i][j] == 2)
				{
					Balloon balloon = new BlueBalloon(player, x_loc, y_loc);
					balloonGroup.addActor(balloon);
				}
				
				x_loc = x_loc + (balloonWidth);
			}
			y_loc = y_loc - (balloonWidth);
			y_pattern = y_loc;
		}
	}
	public float getY()
	{
		return y_pattern;
	}
	public void setPattern(String pattern)
	{
		currentPattern = patternMap.get(pattern);
	}
	public Group getBalloonGroup()
	{
		return balloonGroup;
	}
}
