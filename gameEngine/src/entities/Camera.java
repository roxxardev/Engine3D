package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import toolbox.InputManager;

public class Camera {

	private float distanceFromPlayer = 30;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 10;
	private float yaw = 0;
	private float roll; //not used
	
	private boolean reset = false;
	
	private Player player;
	
	public Camera( Player player) {
		this.player = player;
	}
	
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPostion(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		resetPosition();
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPostion(float horizDistance, float verticDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance + 5;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.020f;
		distanceFromPlayer -=zoomLevel;
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -=pitchChange;
			pitch=pitch%360;
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
			angleAroundPlayer = angleAroundPlayer % 360;
		}
	}
	
	private void resetPosition() {
		if(reset == false)
			reset = InputManager.isKeyPressed(Keyboard.KEY_R);
		if(angleAroundPlayer==0 && distanceFromPlayer == 30 && pitch == 10) {
			reset =false;
		}
		
		if(reset) {
			if(angleAroundPlayer >0) {
				if(angleAroundPlayer <2.5f) angleAroundPlayer=0;
				else angleAroundPlayer-=1.9f;
			} else if(angleAroundPlayer<0) {
				if(angleAroundPlayer>-2.5f) angleAroundPlayer=0;
				else angleAroundPlayer+=1.9f;
			}
			if(distanceFromPlayer>30f) {
				if(distanceFromPlayer<33f) distanceFromPlayer = 30;
				else distanceFromPlayer-=2f;
			} else if(distanceFromPlayer<30f) {
				if(distanceFromPlayer>27f) distanceFromPlayer=30;
				else distanceFromPlayer+=2f;
			}
			if(pitch >10) {
				if(pitch<13) pitch =10;
				else pitch-=2;
			} else if(pitch<10) {
				if(pitch>7) pitch =10;
				else pitch +=2;
			}
		}
		
	}
}
