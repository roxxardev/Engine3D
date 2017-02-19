package skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;

public class SkyboxRenderer {

private static final float SIZE = 900f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};
	private static String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};
	
	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	private float time = 0;
	
	private float red = 0.5444f;
	private float green = 0.62f;
	private float blue = 0.69f;
	
	private int startNight=0;
	private int nigthToDayStart=16000;
	private int startDay=26000;
	private int dayToNightStart=42000;
	private int endOfDay=60000;
	
	
	private Light light;
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix, Light light) {
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		this.light = light;
	}
	
	public void render(Camera camera) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColour(red, green, blue);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures() {
		time += DisplayManager.getFrameTimeSeconds() * 1000;
		time %= endOfDay;
		int texture1;
		int texture2;
		float blendFactor;	
		
		if(time >= startNight && time < nigthToDayStart){
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = (time - startNight)/(nigthToDayStart - startNight);
			red=0f;
			green=0f;
			blue=0f;
			light.setPosition(new Vector3f(-20000,-1,20000));
			light.setColor(new Vector3f(0.0f,0.0f,0.0f));
		} else if(time >= nigthToDayStart && time < startDay){
			texture1 = nightTexture;
			texture2 = texture;
			blendFactor = (time - nigthToDayStart)/(startDay - nigthToDayStart);
			red=0.5444f*blendFactor;
			green=0.62f*blendFactor;
			blue=0.69f*blendFactor;
			light.setPosition(new Vector3f(20000 - 10000*blendFactor,40000*blendFactor,20000));
			light.setColor(new Vector3f(1f*blendFactor,1f*blendFactor,1f*blendFactor));
		} else if(time >= startDay && time < dayToNightStart){
			texture1 = texture;
			texture2 = texture;
			red=0.5444f;
			green=0.62f;
			blue=0.69f;
			int halfDay = (startDay+dayToNightStart)/2;
			if(time < halfDay) {
				blendFactor = (time - startDay)/(halfDay - startDay);
				light.setPosition(new Vector3f(10000-10000*blendFactor,40000,20000));
			} else {
				blendFactor = (time - halfDay)/(dayToNightStart - halfDay);
				light.setPosition(new Vector3f(-10000*blendFactor,40000,20000));
			}
			light.setColor(new Vector3f(1,1,1));
		} else {
			texture1 = texture;
			texture2 = nightTexture;
			blendFactor = (time - dayToNightStart)/(endOfDay - dayToNightStart);
			red=0.5444f-0.5444f*blendFactor;
			green=0.62f-0.62f*blendFactor;
			blue=0.69f-0.69f*blendFactor;
			light.setPosition(new Vector3f(-10000 - 10000*blendFactor,40000-40001*blendFactor,20000));
			light.setColor(new Vector3f(1f - 1f*blendFactor,1f - 1f*blendFactor,1f - 1f*blendFactor));
		}
		
		//System.out.print(light.getPosition() + " time:");
		//System.out.println(time/(endOfDay/24));
		
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}

	public float getRed() {
		return red;
	}

	public void setRed(float red) {
		this.red = red;
	}

	public float getGreen() {
		return green;
	}

	public void setGreen(float green) {
		this.green = green;
	}

	public float getBlue() {
		return blue;
	}

	public void setBlue(float blue) {
		this.blue = blue;
	}


	
}
