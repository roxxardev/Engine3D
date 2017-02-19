package toolbox;

import java.util.HashMap;


import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Vector2f;

public class InputManager {

	private static Map<Integer, Boolean> keyMap = new HashMap<Integer, Boolean>();
	private static Map<Integer, Boolean> previousKeyMap = new HashMap<Integer, Boolean>();

	private static Vector2f mouseCoords= new Vector2f(0,0);
	
	public InputManager() {
		
	}
	
	public static void update() {
		for(Entry<Integer, Boolean> it: keyMap.entrySet()) {
			previousKeyMap.put(it.getKey(), it.getValue());
		}
	}
	
	public static void pressKey(int keyID) {
		keyMap.put(keyID, true);
	}
	
	public static void releaseKey(int keyID) {
		keyMap.put(keyID, false);
	}
	
	public static void setMouseCoords(float x, float y) {
		mouseCoords.x = x;
		mouseCoords.y = y;
	}
	
	public static boolean isKeyDown(int keyID) {
		Object it = keyMap.get(keyID); 
		if(it != null) {
			return (boolean) it;
		} else return false; 
	}
	
	public static boolean isKeyPressed(int keyID) {
		if(isKeyDown(keyID)==true && wasKeyDown(keyID)==false) {
			return true;
		}
		return false;
	}
	
	private static boolean wasKeyDown(int keyID) {
		Object it = previousKeyMap.get(keyID);
		if(it != null) {
			return (boolean) it;
		} else return false;
	}

	public static Vector2f getMouseCoords() {
		return mouseCoords;
	}
	
	public static int getPressedKey() {
		for(Entry<Integer, Boolean> it: keyMap.entrySet()) {
			if(it.getValue()) {
				return it.getKey();
			}
		}
		return -1;
	}
	
}
