package com.me.stratofall.objects.balloons;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.me.stratofall.Player;
import com.me.stratofall.Stratofall;

public class BalloonPattern
{
	private Player player;
	private final Stage stage; //this stage is a reference to the main stage, not a new one. 
	
	private int rows = 5; //all patterns must be 5 by 5
	private int cols = 5;
	
	/**
	 * 1 = red balloon
	 * 2 = blue balloon
	 * 3 = yellow balloon
	 */
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
	
	private int[][] wtf = 	{{3,2,3,2,3},
							{2,3,2,3,2},
							{3,2,3,2,3},
							{2,3,2,3,2},
							{3,2,3,2,3}};

	private int[][] currentPattern;
	private HashMap<String, int[][]> patternMap;
	
	ArrayList<String> patternArray;
	
	public float x_loc;
	private float x_pattern; //the patterns x location
	private float y_pattern; //the patterns y location
	private float y_loc = 0;
	private float balloonWidth = 64;
	private float time = 0;
	private float spawnTime;
	
	private Group balloonGroup;
	SnapshotArray<Actor> balloons;
	
	public TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("objects/objects.pack"));
	
	private TextureRegion redBalloonTexture = new TextureRegion(atlas.findRegion("balloon_red"));
	private TextureRegion blueBalloonTexture = new TextureRegion(atlas.findRegion("balloon_blue"));
	private TextureRegion yellowBalloonTexture = new TextureRegion(atlas.findRegion("balloon_yellow"));
	
	public class RedBalloonPool extends Pool<Actor>{
		@Override
		protected RedBalloon newObject()
		{
			// TODO Auto-generated method stub
			return new RedBalloon(player,redBalloonTexture, x_loc, y_loc, false, false);
		}
	}
	public class BlueBalloonPool extends Pool<Actor>{
		@Override
		protected BlueBalloon newObject()
		{
			// TODO Auto-generated method stub
			return new BlueBalloon(player,blueBalloonTexture, x_loc, y_loc, false, false);
		}
	}
	public class YellowBalloonPool extends Pool<Actor>{
		@Override
		protected YellowBalloon newObject()
		{
			// TODO Auto-generated method stub
			return new YellowBalloon(player,yellowBalloonTexture, x_loc, y_loc, false, false);
		}
	}
	
	RedBalloonPool rbPool = new RedBalloonPool();
	BlueBalloonPool bbPool = new BlueBalloonPool();
	YellowBalloonPool ybPool = new YellowBalloonPool();
	
	/*
	 * Thoughts: After the patten finishes, take a snapshot array, free the balloons to the pool.
	 */
	
	/**
	 * 
	 * @param p Reference to the {@link Player}
	 * @param stage Reference to the parent {@link Stage}
	 * @param time Interval in second before a new pattern is created
	 */
	public BalloonPattern(Player p, Stage stage, float time)
	{
		this.player = p;
		this.stage = stage;
		spawnTime = time;
		
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
		patternMap.put("wtf", wtf);
		
		patternArray = new ArrayList<String>(patternMap.keySet());
		
		/*
		 * Add initial ballons to the pool
		 */
		balloonGroup = new Group();
		stage.addActor(balloonGroup);
		populatePools();
		
	}
	private void populatePools()
	{
		/*
		 * This is an idea I had to give the pools some initial objects, so when the first pattern
		 * is created, it wouldnt have lag. However, this did not help. 
		 */
		Balloon b;
		for(int i = 0; i < 15; i++)
		{
			if(i < 5) //add 5 blue and yellow ballons to the pool
			{
				b = bbPool.newObject();
				bbPool.free(b);
				b = ybPool.newObject();
				ybPool.free(b);
			}
			else //add 10 red balloons to the pool
			{
				b = rbPool.newObject();
				rbPool.free(b);
			}
		}
		
	}
	public void update()
	{
		time += Gdx.graphics.getDeltaTime();
		if(time > spawnTime)
		{
			x_pattern = MathUtils.random(Stratofall.WIDTH - (cols * balloonWidth));
			y_pattern = 0; //this will change depending on how many rows of the pattern contain a coin
			
			setPattern();
			time = 0;
		}
	}
	private void setPattern() 
	{
		currentPattern = patternMap.get(patternArray.get(MathUtils.random(patternArray.size()-1)));
		createBalloons();
	}
	private void createBalloons() 
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				
			}
		}, "BalloonSpawn").start();
		//Group temp = balloonGroup;
		/*
		 * This method adds the old balloons to the pool to be re-used before creating the new group
		 */
		freeBalloons(balloonGroup);
		/*
		 * the size of our balloon is 64 so we need to make sure each balloon in the
		 * group is offset by 64 and add them to the group.
		 */
		
		
		
		int[][] localArray = currentPattern; //the loop doesnt need to look up the pattern from memory
		
		/*
		 * Creating a local array means that it doesnt need to look up the array
		 * every loop from memory. Also, looping as done below is much faster than
		 * using traditional for loops. 
		 * 
		 */
		Balloon balloon;
		
		for(int[] array:localArray)
		{
			x_loc = x_pattern;
			for(int num:array)
			{
				if(num == 1)
				{
					//balloon = rbPool.newObject();
					//balloon.allowReset(false);
					balloonGroup.addActor(rbPool.obtain());
				}
				else if(num == 2)
				{
					//balloon = bbPool.newObject();
					//balloon.allowReset(false); //the balloon does not reset
					balloonGroup.addActor(bbPool.obtain()); //add the balloon to the group
				}
				else if(num == 3)
				{
					//balloon = ybPool.newObject();
					//balloon.allowReset(false);
					balloonGroup.addActor(ybPool.obtain());
				}
				x_loc = x_loc + (balloonWidth);
			}
			y_loc = y_loc - (balloonWidth);
			y_pattern = y_loc;
		}
	}
	private void freeBalloons(Group balloonGroup)
	{
		/*
		 * Before a new pattern is made, we free the balloons from the old
		 * balloonGroup to be used in the new one.
		 */
		if(balloonGroup != null)
		{
			balloons = balloonGroup.getChildren();
			if(balloons.size > 0) //there are balloons to free
			{
				for (Actor balloon : balloons)
				{
					/*
					 * free each balloon to add to the pool
					 */
					String name = balloon.getName();
					if(name.equals("red"))
					{
						rbPool.free(balloon);
					}
					else if(name.equals("blue"))
					{
						bbPool.free(balloon);
					}
					else if(name.equals("yellow"))
					{
						ybPool.free(balloon);
					}
					else
						System.out.println("ERROR: Balloon not added to pool!");
				}
			}
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
//	public Group getBalloonGroup()
//	{
//		return balloonGroup;
//	}
}
