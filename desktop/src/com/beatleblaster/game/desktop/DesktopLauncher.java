package com.beatleblaster.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.beatleblaster.game.BeatleBlaster;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Beatle Blaster";
		config.width = 500;
		config.height = 1000;
		new LwjglApplication(new BeatleBlaster(), config);
	}
}
