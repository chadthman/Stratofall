package com.me.stratofall.objects;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public class BalloonPattern 
{
	private Player player;
	
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
	
	private ArrayList<Balloon> balloons;
	
	private float x_loc;
	private float x_pattern; //the patterns x location
	private float y_pattern; //the patterns y location
	private float y_loc = 0;
	private float balloonWidth = 64;
	
	//TODO convert pattterns to useful names, remove switch statement and use hashmap, then just pull a random element from it.
	public BalloonPattern(Player p)
	{
		this.player = p;
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
		
		reset();
	}
	public void draw(SpriteBatch batch, float delta)
	{
		for (Balloon balloon : balloons)
		{
			balloon.draw(batch, delta); //draw each coin and update
			balloon.update();
		}
		update();
	}
	public void update()
	{
		for(int i = 0; i < balloons.size(); i++)
		{
			Balloon balloon = balloons.get(i);
			checkStatus(balloon);
		}
		if(balloons.isEmpty())//if all coins collected or off screen
			reset();
	}
	private void reset()
	{
		//reset the pattern, new x-location
		x_pattern = MathUtils.random(Stratofall.WIDTH - (cols * balloonWidth));
		y_pattern = 0; //this will change depending on how many rows of the pattern contain a coin
		
		setPattern();
	}
	private void setPattern() 
	{
		
		//currentPattern = pattern6; //used to test patterns
		currentPattern = patternMap.get(patternArray.get(MathUtils.random(patternArray.size()-1)));
		createCoins();
	}
	private void createCoins() 
	{
		/*
		 * the size of our coins is 64 so we need to make sure each coin in the
		 * lit is offset by 64 and add them to the list.
		 */
		balloons = new ArrayList<Balloon>();
		
		for(int i = 0; i < rows; i++)
		{
			x_loc = x_pattern;
			for(int j = 0; j < cols; j++)
			{
				if(currentPattern[i][j] == 1)
					balloons.add(new RedBalloon(player, x_loc, y_loc));
				else if(currentPattern[i][j] == 2)
					balloons.add(new BlueBalloon(player, x_loc, y_loc));
				
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
	private void checkStatus(Balloon balloon)
	{
		if(balloon.isAlive())
			balloon.checkCollisions();
		else if(balloon.effectCompleted())
			balloons.remove(balloon);
		
		if(!(balloon.getLocation().y < Stratofall.HEIGHT))
		{
			balloon.dispose();
			balloons.remove(balloon);
		}
	}
}
