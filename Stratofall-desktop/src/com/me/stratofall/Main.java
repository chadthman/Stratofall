package com.me.stratofall;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Stratofall";
		cfg.useGL20 = true;
		cfg.width = 768;
		cfg.height = 1280;
		
		new LwjglApplication(new Stratofall(), cfg);
	}
}
