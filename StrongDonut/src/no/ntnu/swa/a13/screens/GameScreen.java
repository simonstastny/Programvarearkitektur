package no.ntnu.swa.a13.screens;

import no.ntnu.swa.a13.MyGdxGame;
import no.ntnu.swa.a13.PhysicsHelper;
import no.ntnu.swa.a13.Player;
import no.ntnu.swa.a13.landscape.Landscape;
import no.ntnu.swa.a13.landscape.LandscapeFactory;
import no.ntnu.swa.a13.landscape.LandscapeGenerator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen implements Screen {
	
//	private GameLogic gameLogic= new  GameLogic(2, new FreeForAllStrategy());
	
	public static final int WIDTH = Gdx.graphics.getWidth();
	public static final int HEIGHT = Gdx.graphics.getHeight();
	
	float gone = 0; //FIXME debug simon

	World world;

	MyGdxGame game;
	
	BodyFactory bodyFactory;
	

	// The camera displays a given area across the entire screen.
	private OrthographicCamera camera;

	// the SpriteBatch takes care of rendering images
	private SpriteBatch batch;
	private ShapeRenderer renderer;
	
	//the (crude) buttons for "fire" and "menu"
	private Texture gameButtonTex;
	private TextureRegion menuButton, fireButton;

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
	
	private Landscape landscape;
	private Vector2[] landVertices;
	
	private int ballSize = 5;
	
	private Vector2 target, force;
	private boolean ballExists = false;
	private boolean destroyBall = false;
	private boolean ballBeingFired = false;
	private boolean ignoreHit = true;
	private boolean updateLandscape = false;
	
	// game bodies
	private Body ball, landBody, catapult1, catapult2;
	private Vector2 catPos1, catPos2;
//	private List<Body> playerBodies;

	
	/** ------------------------- */
	public GameScreen(MyGdxGame gameRef, LandscapeGenerator generator) {
		
		bodyFactory = new BodyFactory();
		
		game = gameRef;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MyGdxGame.w, MyGdxGame.h);
		batch = new SpriteBatch();

		renderer = new ShapeRenderer();
		renderer.setTransformMatrix(MyGdxGame.scalingMatrix);
		batch.setTransformMatrix(MyGdxGame.scalingMatrix);
		
		gameButtonTex = new Texture(Gdx.files.internal("data/game_buttons.png"));
		gameButtonTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		menuButton = new TextureRegion(gameButtonTex, 0, 0, 256, 128);
		fireButton = new TextureRegion(gameButtonTex, 256, 0, 256, 128);
		
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
		world = new World(new Vector2(0, -10), false); // FIXME what does this mean

		landscape = LandscapeFactory.makeLandscape(generator);

		landBody = bodyFactory.makeLandmass();
		
		world.setContactListener(new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
//				if(PhysicsHelper.wasHit(contact)) {
//					landscape.deform(ball.getPosition(), 50);
//				}
				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void endContact(Contact contact) {

				
			}
			
			@Override
			public void beginContact(Contact contact) {
				//solving problems with boolean variables is so pretty
				//the following function's boolean checks are there to prevent catapults from self-destructing
				if(!ignoreHit){
					
					if(PhysicsHelper.tankHit(contact)) {
						// someone's hit
						
//						System.out.println("TANK HIT ---------------");
						
						Fixture tank = PhysicsHelper.getTank(contact.getFixtureA(), contact.getFixtureB());
						
						if(tank.getBody().equals(catapult1)) {
							Player.Status stat = MyGdxGame.players[0].causeDamage(100); //FIXME full force by default
							
							if(stat == Player.Status.DESTROYED) {
								gameOver();
							}
						} else {
							Player.Status stat = MyGdxGame.players[1].causeDamage(100); //FIXME full force by default
							
							if(stat == Player.Status.DESTROYED) {
								gameOver();
							}
						}
						
						destroyBall = true;
						
					} else if(PhysicsHelper.landHit(contact)) {
						//I should change deform input parameters instead, but this is faster
						Vector3 defVec = new Vector3();
						defVec.x = ball.getPosition().x;
						defVec.y = ball.getPosition().y;
						defVec.z = 0;
						camera.project(defVec);
						Vector2 defVec2 = new Vector2();
						defVec2.x = defVec.x;
						defVec2.y = defVec.y;
						
						landscape.deform(defVec2, 50);
						
						updateLandscape = true;
						destroyBall = true;
					}				
					
					ignoreHit = true;
				}else{
					ignoreHit = false;
				}
				

				
			}
		});
		
		// initialize players, create catapults
		
//		initializePlayers();
		//Since we like the falling catapults, i'll save some work and keep them
		//if we want them making a less weird entrance all we need to do is fetch
		//the height of the slices underneath and set it to the 2nd parameter in the
		//following vectors
		catPos1 = new Vector2(MyGdxGame.w/8, MyGdxGame.h*7/8);
		catPos2 = new Vector2(MyGdxGame.w*7/8, MyGdxGame.h*7/8);
		
		catapult1 = bodyFactory.makeCatapult(catPos1); 
		MyGdxGame.players[0].setCatapultBody(catapult1);
		catapult2 = bodyFactory.makeCatapult(catPos2); 
		MyGdxGame.players[1].setCatapultBody(catapult2);
		
		//The ball used for testing will be the new projectile instead
		//makeBall(MyGdxGame.wR/2,MyGdxGame.hR);
		
		force = new Vector2();
		target = new Vector2(); //FIXME WTF - Haha, this was just an easy place to put it, nothing more. Centre of screen should be better. SN.
		setTarget(MyGdxGame.w/2, MyGdxGame.h/2);
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
		
		
		if(destroyBall){
			setTarget(MyGdxGame.w/2, MyGdxGame.h/2);
//			world.destroyBody(ball);
//			ballExists = false;
			destroyBall = false;
		}
		
		if(updateLandscape){
			ChainShape landShape = new ChainShape();
			int it = 0;
			for (Rectangle slice : landscape.getSlices()){
				landVertices[it] = new Vector2(slice.x*MyGdxGame.b2dScale,slice.height*MyGdxGame.b2dScale);
				it++;
			};
			landShape.createChain(landVertices);		
			landBody.createFixture(landShape , 1f); //static body density does not matter
			landShape.dispose();
			landBody.destroyFixture(landBody.getFixtureList().get(1)); //the fixture at 0 is the floor
			updateLandscape = false;
		}
		
		if(ballExists){
			renderer.begin(ShapeType.FilledCircle);
			renderer.setColor(Color.BLUE);
			renderer.filledCircle(ball.getPosition().x/MyGdxGame.b2dScale, ball.getPosition().y/MyGdxGame.b2dScale, ballSize, 25);
			renderer.end();
			
			if(ball.getPosition().x < 0 || ball.getPosition().x > MyGdxGame.w){
				setTarget(MyGdxGame.w/2, MyGdxGame.h/2);
			}
			
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
			//experiment			
			if(animationTimer > 18*animationDelta){
				ballBeingFired = false;
				fireBall();
			}
		}
		
		//The catapult is now either red or black, red or invisible, no other colors work.		
		batch.begin();
		batch.setColor(Color.WHITE);
		batch.draw(menuButton,0,MyGdxGame.hR*7/8,MyGdxGame.wR/8,MyGdxGame.hR/8);
		batch.draw(fireButton,MyGdxGame.wR*7/8,MyGdxGame.hR*7/8,MyGdxGame.wR/8,MyGdxGame.hR/8);
//		batch.setColor(Color.CLEAR);
		batch.setColor(Color.BLACK);
		batch.draw(currentCata1Frame,catapult1.getPosition().x/MyGdxGame.b2dScale, catapult1.getPosition().y/MyGdxGame.b2dScale, 0, 0, catapultWidthPx, catapultHeightPx, catapultSize, catapultSize, (int)(catapult1.getAngle()*57.29578));
//		batch.setColor(Color.CLEAR);
		batch.setColor(Color.RED);
		batch.draw(currentCata2Frame,catapult2.getPosition().x/MyGdxGame.b2dScale, catapult2.getPosition().y/MyGdxGame.b2dScale, 0, 0, catapultWidthPx, catapultHeightPx, catapultSize, catapultSize, (int)(catapult2.getAngle()*57.29578));
		batch.end();

		//FIXME wifikundace
		
		//touch-functionality of gameScreen is made here, 
		//Because there is little time left and the game should be playable, a lot of modifiability is sacrificed right here:
		if(Gdx.input.justTouched()/*isTouched()*/){
			
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), /*MyGdxGame.h-*/Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			
			if(touchPos.y > MyGdxGame.h*7/8 && (touchPos.x < MyGdxGame.w/8 || touchPos.x > MyGdxGame.w*7/8)){
				if(touchPos.x < MyGdxGame.w/8){
					game.setScreen(MyGdxGame.mainMenuScreen);
				}
				if(touchPos.x > MyGdxGame.w*7/8){
					animationTimer = 0;
//					MyGdxGame.players[0].setCoordinates(catapult1.getPosition());
//					MyGdxGame.players[1].setCoordinates(catapult2.getPosition());
					ballBeingFired = true;
//					fireBall();
				}
			}else{
//				System.out.println(""+touchPos.x+", "+touchPos.y);//TODO remove this
//				MyGdxGame.players[0].setCoordinates(catapult1.getPosition());
//				MyGdxGame.players[1].setCoordinates(catapult2.getPosition());
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
	
	//setTarget is also used as a standard function to destroy the ball/ projectile
	private void setTarget(float posX, float posY){
		if(ballExists){
			world.destroyBody(ball);
			ballExists = false;
		}
		this.target.x = posX/MyGdxGame.b2dScale;
		this.target.y = posY/MyGdxGame.b2dScale;
		setForce(posX,posY);
	}
	private void setForce(float posX, float posY){
//		System.out.println("Currently active player: "+MyGdxGame.activePlayer);//TODO remove this
//		System.out.println("Active player Xforce: "+Math.abs(target.x-(MyGdxGame.players[MyGdxGame.activePlayer].getCatapult().getPosition().x)));//TODO remove this
		this.force.x = Math.abs(posX-(MyGdxGame.players[MyGdxGame.activePlayer].getCatapult().getPosition().x))*1.5f;
		this.force.y = Math.abs(posY-MyGdxGame.players[MyGdxGame.activePlayer].getCatapult().getPosition().y)*1.5f;
	}
	private void fireBall(){
		ignoreHit = true;
		if(ballExists){
			world.destroyBody(ball);
			ballExists = false;
		}
		
		//Player player = gameLogic.nextPlayer(); //FIXME - another function not going to be used because it adds more complexity than functionality at this point
		
		if(MyGdxGame.activePlayer==0){
			ball = bodyFactory.makeBall(new Vector2((catapult1.getPosition().x+catapult1.getLocalCenter().x), (catapult1.getPosition().y+catapult1.getLocalCenter().y)));
//			ball.applyForce(force,ball.getWorldCenter());
			ball.applyLinearImpulse(force,ball.getWorldCenter());
			MyGdxGame.activePlayer=1;
		} else{
			ball = bodyFactory.makeBall(new Vector2((catapult2.getPosition().x+catapult2.getLocalCenter().x), (catapult2.getPosition().y+catapult2.getLocalCenter().y)));
//			ball.applyForce(-force.x,force.y,ball.getWorldCenter().x,ball.getWorldCenter().y);
			ball.applyLinearImpulse(-force.x,force.y,ball.getWorldCenter().x,ball.getWorldCenter().y);
			MyGdxGame.activePlayer=0;
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
		renderer.dispose();
		// TODO Auto-generated method stub

	}
	
	void gameOver() {
		game.setScreen(MyGdxGame.gameOverScreen);
	}
	
	//We already have a list of players
	
//	private void initializePlayers() {	
//		playerBodies = new ArrayList<Body>(gameLogic.getActivePlayers().size());
//		
//		for(Player player : gameLogic.getActivePlayers()) {
//			playerBodies.add(bodyFactory.makeCatapult(player));			
//		}	
//	}
	
	class BodyFactory {
		
		private Body makeBall(Vector2 vector) {
			Body ballBody;
			
			BodyDef bd = new BodyDef();
			bd.position.x = vector.x;
			bd.position.y = vector.y;
			FixtureDef fd = new FixtureDef();
			bd.type = BodyType.DynamicBody;
			ballBody = world.createBody(bd);
			CircleShape ballShape = new CircleShape();
			ballShape.setRadius(ballSize*MyGdxGame.b2dScale);
			fd.shape = ballShape;
			fd.friction = 1.0f;
			fd.restitution = 0.0f;
			fd.density = 10.0f;
			fd.isSensor = true;
			ballBody.createFixture(fd);
			ballBody.setUserData(PhysicsHelper.BALL);
			ballShape.dispose();
			ballExists = true;
//			System.out.println("Catapult1 is at: "+catapult1.getPosition().x+", "+catapult1.getPosition().y);//TODO remove this
//			System.out.println("Catapult2 is at: "+catapult2.getPosition().x+", "+catapult2.getPosition().y);//TODO remove this
//			System.out.println("Ball made at: "+ballBody.getPosition().x+", "+ballBody.getPosition().y);//TODO remove this
//			System.out.println("Ball force at: "+force.x+", "+force.y);//TODO remove this
			
			return ballBody;
			
		}
		
		private Body makeCatapult(Vector2 catPos/*Player player*/) {
			Body catapult = null;
			
			BodyDef bd = new BodyDef();
			
			// set position - playerCoordinates are not used or updated
			// as the catapult might move it would change the player and make chaos
			// instead each player has a catapult, and catapults have positions
			// that way the position is not stored as two different positions
//			bd.position.x = player.getCoordinates().x;
//			bd.position.y = player.getCoordinates().y;
			
			bd.position.x = catPos.x;
			bd.position.y = catPos.y;

			bd.type = BodyType.DynamicBody;
			
			bd.angularDamping = 15.0f;
			bd.linearDamping = 3.0f;
			
			catapult = world.createBody(bd);

			PolygonShape cataShape = new PolygonShape();
			
			FixtureDef fd = new FixtureDef();
			
			fd.friction = 10.0f;
			fd.restitution = 0.0f;
			fd.density = 1.0f;
			float[] catBodArray = {0,0,catapultWidthPx*catapultSize*MyGdxGame.b2dScale,0,catapultWidthPx*catapultSize*MyGdxGame.b2dScale,catapultHeightPx*catapultSize*MyGdxGame.b2dScale,0,catapultHeightPx*catapultSize*MyGdxGame.b2dScale};
			cataShape.set(catBodArray);
			fd.shape = cataShape;
			catapult.createFixture(fd);
			cataShape.dispose();
			
			catapult.setUserData(PhysicsHelper.CATAPULT);
//			catapult.setUserData(player); //With recent changes this would make a catapult hold a reference to a player with a reference to a catapult etc.
			
			return catapult;
		}
		
		private Body makeLandmass() {
			
			Body landMass = null;
			
			BodyDef bd = new BodyDef();
			bd.position.y = 0;
			bd.position.x = 0;
			bd.type = BodyType.StaticBody;
			landMass = world.createBody(bd);
			FixtureDef fd = new FixtureDef();
			fd.restitution = 0.0f;
			fd.friction = 10.0f;
			
			// A floor below the ground made for testing,
			// stops elements from falling through ground when all ground is destroyed
			EdgeShape floor = new EdgeShape();
			fd.shape = floor;
			floor.set(new Vector2(0,0), new Vector2(MyGdxGame.w,0));
			landMass.createFixture(fd);
			floor.dispose();
			
			ChainShape landShape = new ChainShape();
			landVertices = new Vector2[landscape.getSlices().size()];		
			int it = 0;
			for (Rectangle slice : landscape.getSlices()){
				landVertices[it] = new Vector2(slice.x*MyGdxGame.b2dScale,slice.height*MyGdxGame.b2dScale);
				it++;
			};
			landShape.createChain(landVertices);
			fd.shape = landShape;
			landMass.createFixture(fd); //static body density does not matter
			landShape.dispose();
			
			
			return landMass;
		}
		
		
	}

}
