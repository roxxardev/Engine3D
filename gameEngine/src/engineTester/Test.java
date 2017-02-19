package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.InputManager;
import toolbox.LightsSorter;
import toolbox.MousePicker;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;

public class Test {
	
	public static void main(String[] args) {
		Test test = new Test();
		test.test();
	}
	
	public void test() {

		DisplayManager.createDisplay();
		Loader loader = new Loader();

		// ***TERRAIN***

		TerrainTexture backgroundTexture = new TerrainTexture(
				loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(
				loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(
				backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(
				loader.loadTexture("blendMap"));

		// *************

		ModelTexture fernTextureAtlas = new ModelTexture(
				loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);

		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern",
				loader), fernTextureAtlas);
		fern.getTexture().setHasTransparency(true);

		/*
		 * ModelData data = OBJFileLoader.loadOBJ("tree"); RawModel treeModel =
		 * loader.loadToVAO(data.getVertices(), data.getTextureCoords(),
		 * data.getNormals(), data.getIndices()); TexturedModel tree = new
		 * TexturedModel(treeModel, new
		 * ModelTexture(loader.loadTexture("tree")));
		 */

		// TexturedModel staticModel = new
		// TexturedModel(OBJLoader.loadObjModel("tree", loader), new
		// ModelTexture(loader.loadTexture("tree")));

		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel(
				"grassModel", loader), new ModelTexture(
				loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		// TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern",
		// loader), new ModelTexture(loader.loadTexture("fern")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel(
				"grassModel", loader), new ModelTexture(
				loader.loadTexture("flower")));
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		;
		TexturedModel lowPolyTree = new TexturedModel(OBJLoader.loadObjModel(
				"pine", loader), new ModelTexture(loader.loadTexture("pine")));

		TexturedModel dragon = new TexturedModel(OBJLoader.loadObjModel(
				"dragon", loader),
				new ModelTexture(loader.loadTexture("white")));
		dragon.getTexture().setShineDamper(1);
		dragon.getTexture().setReflectivity(1);

		TexturedModel bunny = new TexturedModel(OBJLoader.loadObjModel(
				"stanfordBunny", loader), new ModelTexture(
				loader.loadTexture("white")));
		bunny.getTexture().setShineDamper(100);
		bunny.getTexture().setReflectivity(1);

		TexturedModel man = new TexturedModel(OBJLoader.loadObjModel("female",
				loader), new ModelTexture(loader.loadTexture("white")));

		TexturedModel stall = new TexturedModel(OBJLoader.loadObjModel("stall",
				loader), new ModelTexture(loader.loadTexture("stallTexture")));

		TexturedModel playerTexture = new TexturedModel(OBJLoader.loadObjModel(
				"exec_rigged", loader), new ModelTexture(
				loader.loadTexture("diffus2")));
		// playerTexture.getTexture().setReflectivity(0.5f);
		// playerTexture.getTexture().setShineDamper(2);

		TexturedModel box = new TexturedModel(OBJLoader.loadObjModel("box",
				loader), new ModelTexture(loader.loadTexture("box")));

		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp",
				loader), new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap,
				"heightmap",800);

		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random(676452);
		for (int i = 0; i < 1000; i++) {
			if (i % 10 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -800;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(
						x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));

				 x = random.nextFloat() * 400;
				 z = random.nextFloat() * -300;
				 y = terrain.getHeightOfTerrain(x, z);
				 entities.add(new Entity(flower, new Vector3f(x,y,z),0,random.nextFloat() * 360,0,2.3f));
			}

			if (i % 3 == 0) {
				 float x = random.nextFloat() * 400;
				 float z = random.nextFloat() * -300;
				 float y = terrain.getHeightOfTerrain(x, z);
				 entities.add(new Entity(grass, new Vector3f(x, y,z),
				 0,random.nextFloat() * 360,0,1.8f));
			}

			if (i % 2 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -800;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(lowPolyTree, new Vector3f(x, y, z), 0,
						random.nextFloat() * 360, 0,
						random.nextFloat() * 0.1f + 0.9f));
//				 x = random.nextFloat() *800-400;
//				 z = random.nextFloat() * -600;
//				 y = terrain.getHeightOfTerrain(x, z);
//				 entities.add(new Entity(tree, new Vector3f(x, y,z),
//				 0,0,0,random.nextFloat() * 1 + 4));
			}
		}

		entities.add(new Entity(box, new Vector3f(20, 4f, -40), 0, 0, 0, 4));

		Entity a = new Entity(dragon, new Vector3f(0, 20, -80), 0, 0, 0, 1);
		Entity b = new Entity(bunny, new Vector3f(40, 20, -80), 0, 0, 0, 1);
		Entity c = new Entity(stall, new Vector3f(0, 0, -80), 0, 180, 0, 1.8f);
		Entity d = new Entity(man, new Vector3f(-20, 0, -80), 0, 0, 0, 1);
		
		List<Light> lights = new ArrayList<Light>();
		Light light = new Light(new Vector3f(20000, 40000, 20000),
				new Vector3f(1, 1, 1));
		lights.add(light);
		// lights.add(new Light(new Vector3f(0,1000,-7000), newVector3f(0.4f,0.4f,0.4f)));
		lights.add(new Light(new Vector3f(185, 10, -293),
				new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(370, 17, -300),
				new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
		// lights.add(new Light(new Vector3f(293,7,-305),new Vector3f(2,2,0),
		// new Vector3f(1,0.01f, 0.002f)));
		Light lampLight = new Light(new Vector3f(293, 7, -305), new Vector3f(2,
				2, 0), new Vector3f(1, 0.01f, 0.002f));
		lights.add(lampLight);
		entities.add(new Entity(lamp, new Vector3f(185, -4.7f, -293), 0, 0, 0,
				1));
		entities.add(new Entity(lamp, new Vector3f(370, 4.2f, -300), 0, 0, 0, 1));
		// entities.add(new Entity(lamp,new Vector3f(293,-6.8f,-305), 0,0,0,1));
		Entity lampEntity = new Entity(lamp, new Vector3f(293, -6.8f, -305), 0,
				0, 0, 1);
		entities.add(lampEntity);

		MasterRenderer renderer = new MasterRenderer(loader, light);

		Player player = new Player(playerTexture, new Vector3f(140, 200, -140),
				0, 180, 0, 3f);
		Camera camera = new Camera(player);

		//List<GuiTexture> guis = new ArrayList<GuiTexture>();
//		GuiTexture gui = new GuiTexture(loader.loadTexture("dd"),
//				new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//		guis.add(gui);

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		MousePicker picker = new MousePicker(camera,
				renderer.getProjectionMatrix(), terrain);

		LightsSorter lightsSorter = new LightsSorter(player, lights, camera);


		while (!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();

			picker.update();
			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			if (terrainPoint != null) {
				lampEntity.setPosition(terrainPoint);
				lampLight.setPosition(new Vector3f(terrainPoint.x,
						terrainPoint.y + 15, terrainPoint.z));

				if (InputManager.isKeyPressed(Keyboard.KEY_P)) {
					entities.add(new Entity(lamp, terrainPoint, 0, 0, 0, 1));
					lights.add(new Light(new Vector3f(terrainPoint.x,
							terrainPoint.y + 15, terrainPoint.z), new Vector3f(
							random.nextFloat() * 4, random.nextFloat() * 4,
							random.nextFloat() * 4), new Vector3f(1, 0.01f,
							0.002f)));
					System.out.println(lights.size() + " lights");

				}
			}
			// System.out.println(picker.getCurrentRay());
			
			Input.processInput(renderer);

			a.increaseRotation(0.01f, 1, 0.05f);
			b.increaseRotation(-0.01f, -1, -0.05f);

			lightsSorter.sortLights();

			renderer.processEntity(player);
			renderer.processTerrain(terrain);

			for (Entity entity : entities) {
				renderer.processEntity(entity);
			}
			renderer.processEntity(a);
			renderer.processEntity(b);
			renderer.processEntity(c);
			renderer.processEntity(d);

			renderer.render(lights, camera);

			//guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}

		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	
}
