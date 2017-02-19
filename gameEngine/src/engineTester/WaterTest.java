package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.InputManager;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class WaterTest {
	
	private final static int GRID_SIZE = 150;
	
	private Loader loader;
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;

	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	private ModelTexture fernTextureAtlas;
	private TexturedModel fern;
	private TexturedModel playerTexture;
	
	private Terrain terrain;
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	private Light light;
	private List<Light> lights = new ArrayList<Light>();
	
	private MasterRenderer renderer;

	private Player player;
	private Camera camera;
	
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private Terrain terrain2;
	private WaterShader waterShader;
	private WaterRenderer waterRenderer;
	private List<WaterTile> waters = new ArrayList<WaterTile>();
	
	private TexturedModel rock;

	public WaterTest() {
		DisplayManager.createDisplay();
		
		loader = new Loader();
		backgroundTexture = new TerrainTexture(
				loader.loadTexture("grassy2"));
		rTexture = new TerrainTexture(loader.loadTexture("mud"));
		gTexture = new TerrainTexture(
				loader.loadTexture("grassFlowers"));
		bTexture = new TerrainTexture(loader.loadTexture("path"));
		texturePack = new TerrainTexturePack(
				backgroundTexture, rTexture, gTexture, bTexture);
		blendMap = new TerrainTexture(
				loader.loadTexture("blendMapp"));
		fernTextureAtlas = new ModelTexture(
				loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		fern = new TexturedModel(OBJLoader.loadObjModel("fern",
				loader), fernTextureAtlas);
		fern.getTexture().setHasTransparency(true);
		playerTexture = new TexturedModel(OBJLoader.loadObjModel(
				"exec_rigged", loader), new ModelTexture(
				loader.loadTexture("diffus2")));
		terrain = new Terrain(0, -1, loader, texturePack, blendMap,
				"heightmapp",GRID_SIZE);
		terrains.add(terrain);
		
		terrain2 = new Terrain(0,0,loader,texturePack,blendMap,"heightMapp",GRID_SIZE);
		terrains.add(terrain2);
		
		light = new Light(new Vector3f(20000, 40000, 20000),
				new Vector3f(1, 1, 1));
		lights.add(light);
		
		renderer = new MasterRenderer(loader, light);
		player = new Player(playerTexture, new Vector3f(75, 100, -20),
				0, 180, 0, 3f);
		camera = new Camera(player);
		entities.add(player);
		
		waterShader = new WaterShader();
		waterRenderer= new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
		waters.add(new WaterTile(75,-75,0));
		
		rock = new TexturedModel(OBJLoader.loadObjModel("rocks", loader), 
				new ModelTexture(loader.loadTexture("rocks")));
		
		entities.add(new Entity(rock, new Vector3f(75,4,-75), 
				0, 180, 0, 70));
	}
	
	public void gameLoop() {
		while (!Display.isCloseRequested()) {
			System.out.printf("(%f,%f)\n",terrain.getX(),terrain.getZ());
			
			for(Terrain i:terrains) {
				if(player.getPosition().x >= i.getX() && player.getPosition().x <= i.getX() +GRID_SIZE
						&& player.getPosition().z >=i.getZ() &&  player.getPosition().z <=i.getZ() + GRID_SIZE) {
					player.move(i);
					break;
				}
			}
			
			
			camera.move();
			processInput(renderer);
			
			renderer.renderScene(entities, terrains, lights, camera);
			waterRenderer.render(waters, camera);
			
			DisplayManager.updateDisplay();
		}
		waterShader.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	
	
	public static void processInput(MasterRenderer renderer) {
		InputManager.update();

		while (Keyboard.next()) {

			int key = Keyboard.getEventKey();
					
			if (Keyboard.getEventKeyState()) {
				InputManager.pressKey(key);
			} else {
				InputManager.releaseKey(key);
			}
		}
		while (Mouse.next()) {
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




