package no.ntnu.swa.a13.landscape;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;


public class LandscapeFactory {
	
	public final static int NUMBER_OF_SLICES = 30;
	
	static Landscape makeLandscape(LandscapeGeneratingFunction function) {
		
		List<SliceShape> generated = new ArrayList<SliceShape>();
		
		for(int i = 0; i < NUMBER_OF_SLICES; i++) {
			
			SliceShape slice = new SliceShape();
			
			float left = i*Landscape.SLICE_WIDTH;
			float right = left + Landscape.SLICE_WIDTH;
			
			Vector2 bl = new Vector2(left, 0);
			Vector2 br = new Vector2(right, 0);		
				
			Vector2 tl = new Vector2(function.getY(left), 0);
			Vector2 tr = new Vector2(function.getY(right), 0);		
				
			slice.set(new Vector2[] {bl,br,tl,tr});
			
			generated.add(slice);
		}	
		return new Landscape(generated);
	}
}
