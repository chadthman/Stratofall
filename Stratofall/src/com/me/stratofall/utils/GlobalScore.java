package com.me.stratofall.utils;

import java.util.ArrayList;

import com.stackmob.sdk.model.StackMobModel;


/*
 * Stackmob has been deprecated so this will change to something else if it fully becomes implemented
 *
 */
public class GlobalScore extends StackMobModel {

	private ArrayList<Score> globalScore;
	
	public GlobalScore(ArrayList<Score> globalScore)
	{
		super(GlobalScore.class);
		this.globalScore = globalScore;
	}
	
	public ArrayList<Score> getGlobalScores()
	{
		return globalScore;
	}
	
	public void setGlobalScore(ArrayList<Score> globalScore)
	{
		this.globalScore = globalScore;
	}

}
