package com.me.stratofall;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main
{
	public static void main(String[] args)
	{
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Stratofall";
		cfg.useGL20 = true;
		cfg.width = 576;
		cfg.height = 960;
		cfg.resizable = false; //So that it doesn't resize on smaller screens.

		new LwjglApplication(new Stratofall(), cfg);
	}
}
