package com.me.stratofall.utils;

public class Score implements Comparable<Score> {
	
	//The score of the person for each game.
	
	private int distance;
	private int score;
	private String name;

	public Score (String name, int distance, int score) //for test purposes at the moment
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

	@Override
	public int compareTo(Score score) {
		return score.getScore() - this.score;
	}

}
