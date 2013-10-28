package com.me.stratofall.objects;

public class Score {
	
	//The score of the person for each game.
	
	private int distance;
	private int score;
	private String name;

	public Score (String name, int score, int distance) //for test purposes at the momnent
	{
		this.name = name;
		this.score = score;
		this.distance = distance;
	}

	public void setScore(int score)
	{
		this.score = score;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setDistance(int distance)
	{
		this.distance = distance;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getDistance()
	{
		return distance;
	}

}
