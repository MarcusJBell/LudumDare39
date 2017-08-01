package com.gmail.sintinium.ludumdare39.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.gmail.sintinium.ludumdare39.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
	    try {
	        packImages();
        } catch (Exception e) {
//	        e.printStackTrace();
//            System.out.println("Raw images not present, or error thrown. Using previously packed images...");
        }
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = false;
		config.foregroundFPS = 0;
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new Game(), config);
	}

	private static void packImages() {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.stripWhitespaceX = true;
        settings.stripWhitespaceY = true;
        settings.useIndexes = true;
        TexturePacker.process(settings, "./rawimages", "./packed", "textures");
    }

}
