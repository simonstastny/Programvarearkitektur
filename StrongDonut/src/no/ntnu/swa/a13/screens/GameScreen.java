package no.ntnu.swa.a13.screens;

import no.ntnu.swa.a13.MyGdxGame;
import no.ntnu.swa.a13.landscape.Landscape;
import no.ntnu.swa.a13.landscape.LandscapeFactory;
import no.ntnu.swa.a13.landscape.LandscapeGenerator;
import no.ntnu.swa.a13.landscape.SimonsStupidGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen implements Screen {
	
	public static final int WIDTH = Gdx.graphics.getWidth();
	public static final int HEIGHT = Gdx.graphics.getHeight();
	
	float gone = 0; //FIXME debug simon

	World world;

	MyGdxGame game;

	// The camera displays a given area across the entire screen.
	private OrthographicCamera camera;

	// the SpriteBatch takes care of rendering images
	private SpriteBatch batch;
	private PolygonSpriteBatch polygonBatch;
	private ShapeRenderer renderer;

	// Menu elements need textures
	private Texture newGameTex;
	private TextureRegion sketchRegion;
	
	private Landscape landscape;

	public GameScreen(MyGdxGame gameRef) {
		game = gameRef;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MyGdxGame.w, MyGdxGame.h);
		batch = new SpriteBatch();
		polygonBatch = new PolygonSpriteBatch();
		renderer = new ShapeRenderer();
		renderer.setTransformMatrix(MyGdxGame.scalingMatrix);
		batch.setTransformMatrix(MyGdxGame.scalingMatrix);

		newGameTex = new Texture(Gdx.files.internal("data/help_res/gamesketch_1.png"));
		newGameTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);// not sure if filter is needed
		sketchRegion = new TextureRegion(newGameTex, 0, 0, WIDTH, 512);

		world = new World(new Vector2(0, 0), false); // FIXME what does this
														// mean

		landscape = LandscapeFactory.makeLandscape(new SimonsStupidGenerator());

		int sliceCounter = 0;
		BodyDef bd = new BodyDef();
		bd.position.y = 0;
		bd.type = BodyType.StaticBody;
		
		for (Rectangle slice : landscape.getSlices()) {
			
			bd.position.x = sliceCounter++*Landscape.SLICE_WIDTH;
			
			Body b = world.createBody(bd);
			
			//FIXME create fixtures!!!!
			
			//b.createFixture(new , 1500); //FIXME density
		}
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		renderer.setProjectionMatrix(camera.combined);
		
		renderer.begin(ShapeType.Rectangle);
		
		for(Rectangle rect : landscape.getSlices()) {
			renderer.rect(rect.x,rect.y,rect.width,rect.height);
		}
		
		renderer.end();

		//FIXME wifikundace
		if(Gdx.input.isTouched()){
			landscape.deform(new Vector2(Gdx.input.getX(),HEIGHT-Gdx.input.getY()), 50);
		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();
		// TODO Auto-generated method stub

	}

}
