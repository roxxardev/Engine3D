package toolbox;

import java.util.Comparator;
import java.util.List;

import entities.Camera;
import entities.Light;
import entities.Player;

public class LightsSorter {
	
	private Player player;
	private Camera camera;
	private LightComparator lightComparator = new LightComparator();
	List<Light> lights;

	public LightsSorter(Player player, List<Light> lights, Camera camera) {
		super();
		this.player = player;
		this.lights = lights;
		this.camera = camera;
	}
	
	class LightComparator implements Comparator<Light> {
		@Override
		public int compare(Light a, Light b) {
			double distanceA =Math.sqrt(Math.pow(a.getPosition().x - player.getPosition().x,2) + Math.pow(a.getPosition().y - player.getPosition().y,2) + Math.pow(a.getPosition().z -player.getPosition().z , 2));
			double distanceB =Math.sqrt(Math.pow(b.getPosition().x - player.getPosition().x,2) + Math.pow(b.getPosition().y - player.getPosition().y,2) + Math.pow(b.getPosition().z -player.getPosition().z , 2));

			return distanceA < distanceB ? -1 : distanceA == distanceB ? 0 : 1;
		}
		
	}
	
	public void sortLights() {
		lights.sort(lightComparator);
		lights.add(0, lights.get(lights.size() - 1));
		lights.remove(lights.size() - 1);
	}
	
}
