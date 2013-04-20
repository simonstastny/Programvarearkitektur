package no.ntnu.swa.a13.screens;

import java.util.Vector;

import no.ntnu.swa.a13.MyGdxGame;
import no.ntnu.swa.a13.landscape.Landscape;
import no.ntnu.swa.a13.landscape.LandscapeFactory;
import no.ntnu.swa.a13.landscape.LandscapeGenerator;
import no.ntnu.swa.a13.landscape.SimonsStupidGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
//	private SpriteBatch batch;
//	private PolygonSpriteBatch polygonBatch;
	private ShapeRenderer renderer;

	// Menu elements need textures
//	private Texture newGameTex;
//	private TextureRegion sketchRegion;
	
	private Landscape landscape;
	private Vector2[] landVertices;
	
	
	private Body ball, landBody;

	public GameScreen(MyGdxGame gameRef) {
		game = gameRef;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MyGdxGame.w, MyGdxGame.h);
//		batch = new SpriteBatch();
//		polygonBatch = new PolygonSpriteBatch();
		renderer = new ShapeRenderer();
		renderer.setTransformMatrix(MyGdxGame.scalingMatrix);
//		batch.setTransformMatrix(MyGdxGame.scalingMatrix);

//		newGameTex = new Texture(Gdx.files.internal("data/help_res/gamesketch_1.png"));
//		newGameTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);// not sure if filter is needed
//		sketchRegion = new TextureRegion(newGameTex, 0, 0, WIDTH, 512);

		world = new World(new Vector2(0, -100), false); // FIXME what does this
														// mean

		landscape = LandscapeFactory.makeLandscape(new SimonsStupidGenerator());

//		int sliceCounter = 0;
		BodyDef bd = new BodyDef();
		bd.position.y = 0;
		bd.position.x = 0;
		bd.type = BodyType.StaticBody;
		landBody = world.createBody(bd);
		
		// A floor below the ground made for testing,
		// stops elements from falling through ground when all ground is destroyed
		EdgeShape floor = new EdgeShape();
		floor.set(new Vector2(0,0), new Vector2(MyGdxGame.wR,0));
		landBody.createFixture(floor,1f);
		floor.dispose();
		
		ChainShape landShape = new ChainShape();
		landVertices = new Vector2[landscape.getSlices().size()];		
		int it = 0;
		for (Rectangle slice : landscape.getSlices()){
			landVertices[it] = new Vector2(slice.x,slice.height);
			it++;
		};
		landShape.createChain(landVertices);		
		landBody.createFixture(landShape , 1f); //static body density does not matter
		landShape.dispose();
		
		
		
		bd.position.x = MyGdxGame.wR/2;
		bd.position.y = MyGdxGame.hR/2;
		bd.type = BodyType.DynamicBody;
		ball = world.createBody(bd);
		CircleShape ballShape = new CircleShape();
		ballShape.setRadius(15);
		ball.createFixture(ballShape, 100f);
		ballShape.dispose();
		  
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		renderer.setProjectionMatrix(camera.combined);
		
		renderer.begin(ShapeType.Rectangle);
		renderer.setColor(Color.GREEN);		
		for(Rectangle slice : landscape.getSlices()) {
			renderer.rect(slice.x,slice.y,slice.width,slice.height);//names were changed for clarity, slice.y is always 0
		}
		
		renderer.end();
		renderer.begin(ShapeType.Circle);
		renderer.setColor(Color.BLUE);
		renderer.circle(ball.getPosition().x, ball.getPosition().y, 15);
		renderer.end();

		//FIXME wifikundace
		if(Gdx.input.isTouched()){
			landscape.deform(new Vector2(Gdx.input.getX(),HEIGHT-Gdx.input.getY()), 50);
			ChainShape landShape = new ChainShape();
			int it = 0;
			for (Rectangle slice : landscape.getSlices()){
				landVertices[it] = new Vector2(slice.x,slice.height);
				it++;
			};
			landShape.createChain(landVertices);		
			landBody.createFixture(landShape , 1f); //static body density does not matter
			landShape.dispose();
			landBody.destroyFixture(landBody.getFixtureList().get(1)); //the fixture at 0 is the floor
		}
		
		world.step(delta, 6, 2);//put a fraction in place of delta for a fixed framerate

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
//		batch.dispose();
		// TODO Auto-generated method stub

	}

}
