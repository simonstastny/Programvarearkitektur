package no.ntnu.swa.a13.landscape;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;

public class LandscapeFactory {
	
	public final static int NUMBER_OF_SLICES = 300;
	
	public static Landscape makeLandscape(LandscapeGenerator function) {
		
		List<Rectangle> generated = new ArrayList<Rectangle>();
		
		for(int i = 0; i < NUMBER_OF_SLICES; i++) {
			
			float left = i*Landscape.SLICE_WIDTH;
			
			Rectangle rect = new Rectangle(left, 0, Landscape.SLICE_WIDTH, function.f(left));
			
			generated.add(rect);
		}	
		return new Landscape(generated);
	}
}
