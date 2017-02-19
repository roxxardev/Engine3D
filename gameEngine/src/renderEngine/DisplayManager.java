package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
//import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import toolbox.InputManager;

public class DisplayManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta;
	
	private static boolean polygonMode = false;
	
	private static long lastFPS;
	private static int fps;
	
	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3,2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(), attribs); 
			Display.setTitle("Nothing here :(");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
		lastFPS =getCurrentTime();
	}
	
	public static void updateDisplay() {
		
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
		
		if(InputManager.isKeyPressed(Keyboard.KEY_Z)) {
			if(polygonMode) {
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
				polygonMode = false;
			} else {
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
				polygonMode = true;
			}
		} 
		updateFPS();
	}
	
	public static float getFrameTimeSeconds() {
		 return delta;
	}
	
	public static void closeDisplay() {
		
		Display.destroy();
	}
	
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 /Sys.getTimerResolution();
	}
	
	public static void updateFPS() {
	    if (getCurrentTime() - lastFPS > 1000) {
	        Display.setTitle("Nothing here :(" + "        FPS: " + fps); 
	        fps = 0;
	        lastFPS += 1000; //add one second
	    }
	    fps++;
	}
}
