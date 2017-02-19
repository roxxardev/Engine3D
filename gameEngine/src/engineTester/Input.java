package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import renderEngine.MasterRenderer;
import toolbox.InputManager;

public class Input {
	
	public static void processInput(MasterRenderer renderer) {
		InputManager.update();

		while (Keyboard.next()) {

			int key = Keyboard.getEventKey();
			String keyName = Keyboard.getKeyName(key);
			

			if (Keyboard.getEventKeyState()) {
				InputManager.pressKey(key);
				
				
			} else {
				InputManager.releaseKey(key);
				
				System.out.println(keyName + "(" + key + ") was pressed!");
			}
		}
		while (Mouse.next()) {
			//int key = Mouse.getEventButton();
			/*
			 * System.out.println(key + ": "+Mouse.getButtonName(key) +
			 * " is pressed!"); if(Mouse.getEventButtonState()) {
			 * InputManager.pressKey(key); } else {
			 * InputManager.releaseKey(key); }
			 */
			InputManager.setMouseCoords(Mouse.getX(), Mouse.getY());
		}

		boolean hasChanged = false;
		if (InputManager.isKeyDown(Keyboard.KEY_NUMPAD8)) {
			renderer.setDensity(renderer.getDensity() + 0.00001f);
			hasChanged = true;
		}
		if (InputManager.isKeyDown(Keyboard.KEY_NUMPAD2)) {
			renderer.setDensity(renderer.getDensity() - 0.00001f);
			hasChanged = true;
		}
		if (InputManager.isKeyDown(Keyboard.KEY_NUMPAD6)) {
			renderer.setGradient(renderer.getGradient() + 0.01f);
			hasChanged = true;
		}
		if (InputManager.isKeyDown(Keyboard.KEY_NUMPAD4)) {
			renderer.setGradient(renderer.getGradient() - 0.01f);
			hasChanged = true;
		}
		if (hasChanged) {
			System.out.println("Density: " + renderer.getDensity()
					+ ", Gradient: " + renderer.getGradient());
			hasChanged = false;
		}

	}

}
