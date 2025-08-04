package com.cfc.makingitbettergame;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.cfc.makingitbettergame.MakingItBetterGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("MakingItBetterGame");
		config.setWindowIcon(Files.FileType.Internal, "img/icons/gameIcon.png");
		config.setWindowedMode(MakingItBetterGame.V_WIDTH, MakingItBetterGame.V_HEIGHT);
		new Lwjgl3Application(new MakingItBetterGame(), config);
	}
}
