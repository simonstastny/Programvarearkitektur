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
import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
	private SpriteBatch batch;
//	private PolygonSpriteBatch polygonBatch;
	private ShapeRenderer renderer;

	//Catapult textures
	private Texture catapultTex;
	private TextureRegion[] catapultRegion, catapult2Region;
	private Animation catapult1Ani, catapult2Ani;
	private TextureRegion currentCata1Frame, currentCata2Frame;
	private float animationTimer;
	private float animationDelta = 0.02f;
	//numbers needed to get the right size catapults
	private int catapultHeightPx = 273;
	private int catapultWidthPx = 448;
	private int catapultWidthIncrement = 450;
	private int catapultMap1stRow = 95;
	private int catapultMap2ndRow = 545;
	private float catapultSize = 0.1f; //for scaling down the texture
	private Vector2 catPos1, catPos2;
	
	private Landscape landscape;
	private Vector2[] landVertices;
	
	
	private Body ball, landBody, catapult1, catapult2;
	private Vector2 target, force;
	private boolean ballExists = false;
	private boolean ballBeingFired = false;

	public GameScreen(MyGdxGame gameRef) {
		game = gameRef;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MyGdxGame.w, MyGdxGame.h);
		batch = new SpriteBatch();

		renderer = new ShapeRenderer();
		renderer.setTransformMatrix(MyGdxGame.scalingMatrix);
		batch.setTransformMatrix(MyGdxGame.scalingMatrix);

		catapultTex = new Texture(Gdx.files.internal("data/mangonel.png"));
		catapultTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);// not sure if filter is needed
		
		//The individual catapult images are 448px*448px There are 2 px of padding around each image
		//see .txt file for detailed position of each image
		//IN ADDITION!: 95 empty pixels above and 80 empty pixels below each image
		catapultRegion = new TextureRegion[18];
		catapult2Region = new TextureRegion[18];
		for (int i = 0; i < 9; i++){
			catapultRegion[i] = new TextureRegion(catapultTex, 2+i*catapultWidthIncrement, catapultMap1stRow, catapultWidthPx, catapultHeightPx);
			catapult2Region[i] = new TextureRegion(catapultTex, 2+i*catapultWidthIncrement+catapultWidthPx, catapultMap1stRow, -catapultWidthPx, catapultHeightPx);
//			catapult2Region[i].flip(true, false);
		}
		for (int i = 0; i < 9; i++){
			catapultRegion[i+9] = new TextureRegion(catapultTex, 2+i*catapultWidthIncrement, catapultMap2ndRow, catapultWidthPx, catapultHeightPx);
			catapult2Region[i+9] = new TextureRegion(catapultTex, 2+i*catapultWidthIncrement+catapultWidthPx, catapultMap2ndRow, -catapultWidthPx, catapultHeightPx);
//			catapult2Region[i].flip(true, false);
		}
		catapult1Ani = new Animation(animationDelta,catapultRegion);
		catapult2Ani = new Animation(animationDelta,catapult2Region);
		animationTimer = 0;
		currentCata1Frame = catapult1Ani.getKeyFrame(animationTimer);
		currentCata2Frame = catapult2Ani.getKeyFrame(animationTimer);
		
		
		
//		p1Region = new TextureRegion(catapultTex, 2, catapultMap1stRow, catapultWidthPx, catapultHeightPx);
//		p2Region = new TextureRegion(p1Region);

		//below: Vector holds x and y components of gravity
		world = new World(new Vector2(0, -50), false); // FIXME what does this
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
		
		
		
		
		
		//Making the physical part of the catapults
		catPos1 = new Vector2(MyGdxGame.wR/8, MyGdxGame.hR);
		catPos2 = new Vector2(MyGdxGame.wR*7/8, MyGdxGame.hR);
		bd.type = BodyType.DynamicBody;
		bd.position.x = catPos1.x;
		bd.position.y =	catPos1.y;
		bd.angularDamping = 15.0f;
		catapult1 = world.createBody(bd);
		bd.position.x = catPos2.x;
		bd.position.y =	catPos2.y;
		catapult2 = world.createBody(bd);
		PolygonShape cataShape = new PolygonShape();
		FixtureDef fd = new FixtureDef(); //this fixturedef is reused for the projectile "ball"
		fd.friction = 10.0f;
		fd.restitution = 0.2f;
		fd.density = 1.0f;
		float[] catBodArray = {0,0,catapultWidthPx*catapultSize,0,catapultWidthPx*catapultSize,catapultHeightPx*catapultSize,0,catapultHeightPx*catapultSize};
		cataShape.set(catBodArray);
		fd.shape = cataShape;
		catapult1.createFixture(fd);
		catapult2.createFixture(fd);
		cataShape.dispose();
		
		
		//The ball used for testing will be the new projectile instead
		//makeBall(MyGdxGame.wR/2,MyGdxGame.hR);
		
		target = new Vector2(0,0);
		force = new Vector2(0,0);

		
		
	}
	


	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		

		renderer.setProjectionMatrix(camera.combined);
		
		renderer.begin(ShapeType.Rectangle);
		renderer.setColor(Color.GREEN);		
		for(Rectangle slice : landscape.getSlices()) {
			renderer.rect(slice.x,slice.y,slice.width,slice.height);//names were changed for clarity, slice.y is always 0
		}
		renderer.end();
		
		if(ballExists){
			
			renderer.begin(ShapeType.FilledCircle);
			renderer.setColor(Color.BLUE);
			renderer.filledCircle(ball.getPosition().x, ball.getPosition().y, 5f, 25);
			renderer.end();
		}else{
			renderer.begin(ShapeType.FilledCircle);
			if(MyGdxGame.activePlayer==1){
				renderer.setColor(Color.RED);
			}else{
				renderer.setColor(Color.BLACK);
			}
			renderer.filledCircle(target.x, target.y, 5f, 25);
			renderer.end();
		}
		
		batch.setProjectionMatrix(camera.combined);

		if(ballBeingFired){
			if(MyGdxGame.activePlayer==0){
				currentCata1Frame = catapult1Ani.getKeyFrame(animationTimer+=delta,true);
			}else{
				currentCata2Frame = catapult2Ani.getKeyFrame(animationTimer+=delta,true);
			}
			if(animationTimer > 18*animationDelta){
				ballBeingFired = false;
				fireBall();
			}
		}
		
		//The catapult is now either red or black, red or invisible, no other colors work.		
		batch.begin();
//		batch.setColor(Color.CLEAR);
		batch.setColor(Color.BLACK);
		batch.draw(currentCata1Frame,catapult1.getPosition().x, catapult1.getPosition().y, 0, 0, catapultWidthPx, catapultHeightPx, catapultSize, catapultSize, (int)(catapult1.getAngle()*57.29578));
//		batch.setColor(Color.CLEAR);
		batch.setColor(Color.RED);
		batch.draw(currentCata2Frame,catapult2.getPosition().x, catapult2.getPosition().y, 0, 0, catapultWidthPx, catapultHeightPx, catapultSize, catapultSize, (int)(catapult2.getAngle()*57.29578));
		batch.end();

		//FIXME wifikundace
		//Because there is little time left and the game should be playable, a lot of modifiability is sacrificed right here:
		if(Gdx.input.justTouched()/*isTouched()*/){
			
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), MyGdxGame.hR-Gdx.input.getY(), 0);
//			camera.unproject(touchPos);
			
			if(touchPos.y > MyGdxGame.hR*7/8 && (touchPos.x < MyGdxGame.wR/8 || touchPos.x > MyGdxGame.wR*7/8)){
				if(touchPos.x < MyGdxGame.wR/8){
					game.setScreen(MyGdxGame.mainMenuScreen);
				}
				if(touchPos.x > MyGdxGame.wR*7/8){
					animationTimer = 0;
					MyGdxGame.players[0].setCoordinates(catapult1.getPosition());
					MyGdxGame.players[1].setCoordinates(catapult2.getPosition());
					ballBeingFired = true;
//					fireBall();
				}
			}else{
				System.out.println(""+touchPos.x+", "+touchPos.y);
				MyGdxGame.players[0].setCoordinates(catapult1.getPosition());
				MyGdxGame.players[1].setCoordinates(catapult2.getPosition());
				setTarget(touchPos.x, touchPos.y);
			}
			
			
			//KEEP THE FOLLOWING CODE UNTIL IT'S IMPLEMENTED ELSEWHERE!!
			
//			animationTimer += delta;
//			currentCata1Frame = catapult1Ani.getKeyFrame(animationTimer, true);
//			currentCata2Frame = catapult2Ani.getKeyFrame(animationTimer, true);
			
			
//			landscape.deform(new Vector2(Gdx.input.getX(),HEIGHT-Gdx.input.getY()), 50);
//			ChainShape landShape = new ChainShape();
//			int it = 0;
//			for (Rectangle slice : landscape.getSlices()){
//				landVertices[it] = new Vector2(slice.x,slice.height);
//				it++;
//			};
//			landShape.createChain(landVertices);		
//			landBody.createFixture(landShape , 1f); //static body density does not matter
//			landShape.dispose();
//			landBody.destroyFixture(landBody.getFixtureList().get(1)); //the fixture at 0 is the floor
		}
		
		
		
		world.step(delta, 6, 2);//put a fraction in place of delta for a fixed framerate

	}
	
	private void setTarget(float posX, float posY){
		if(ballExists){
			world.destroyBody(ball);
			ballExists = false;
		}
		this.target.x = posX;
		this.target.y = posY;
		setForce();
	}
	private void setForce(){
		this.force.x = Math.abs(target.x-MyGdxGame.players[MyGdxGame.activePlayer].getCoordinates().x)*1000;
		this.force.y = Math.abs(target.y-MyGdxGame.players[MyGdxGame.activePlayer].getCoordinates().y)*1000;
	}
	private void fireBall(){
		if(ballExists){
			world.destroyBody(ball);
			ballExists = false;
		}
		
		makeBall(MyGdxGame.players[MyGdxGame.activePlayer].getCoordinates().x+catapultWidthPx*catapultSize/2,MyGdxGame.players[MyGdxGame.activePlayer].getCoordinates().y+catapultHeightPx*catapultSize);
		
		if(MyGdxGame.activePlayer==0){
//			ball.applyForce(force,ball.getWorldCenter());
			ball.applyLinearImpulse(force,ball.getWorldCenter());
			MyGdxGame.activePlayer=1;
		}else{
//			ball.applyForce(-force.x,force.y,ball.getWorldCenter().x,ball.getWorldCenter().y);
			ball.applyLinearImpulse(-force.x,force.y,ball.getWorldCenter().x,ball.getWorldCenter().y);
			MyGdxGame.activePlayer=0;
		}
		
	}
	
	private void makeBall(float posX, float posY){
		BodyDef bd = new BodyDef();
		bd.position.x = posX;
		bd.position.y = posY;
		FixtureDef fd = new FixtureDef();
		bd.type = BodyType.DynamicBody;
		ball = world.createBody(bd);
		CircleShape ballShape = new CircleShape();
		ballShape.setRadius(5);
		fd.shape = ballShape;
		fd.friction = 1.0f;
		fd.restitution = 0.0f;
		fd.density = 1.0f;
		ball.createFixture(fd);
		ballShape.dispose();
		ballExists = true;
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
		renderer.dispose();
		// TODO Auto-generated method stub

	}

}
