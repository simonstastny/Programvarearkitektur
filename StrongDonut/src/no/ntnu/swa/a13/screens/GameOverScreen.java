package no.ntnu.swa.a13.screens;

import no.ntnu.swa.a13.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class GameOverScreen implements Screen {// A the game class, it will hold some cross-screen relevant variables
	//but as long as those variables are static, it won't be needed
	MyGdxGame game;
	
	//The camera displays a given area across the entire screen.
	private OrthographicCamera camera;
	
	//the SpriteBatch takes care of rendering images
	private SpriteBatch batch;
	
	//Menu elements need textures
	private Texture newGameTex;
	private TextureRegion mainMenuButton, newGameButton, optionsButton, exitButton;
	
	
	
	
	public GameOverScreen(MyGdxGame gameRef){
		game = gameRef;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MyGdxGame.w, MyGdxGame.h);
		batch = new SpriteBatch();
		batch.setTransformMatrix(MyGdxGame.scalingMatrix);
		
		newGameTex = new Texture(Gdx.files.internal("data/game_over.png"));
		newGameTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);//not sure if filter is needed
		
		mainMenuButton = new TextureRegion(newGameTex, 0, 0, 405, 420);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		//menu goes here
		batch.begin();
		batch.setColor(Color.GREEN);
		batch.draw(mainMenuButton, 0, 0, MyGdxGame.wR, MyGdxGame.hR);
		
		batch.end();
		
		if(Gdx.input.justTouched()){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(),Gdx.input.getY(), 0);
//			camera.unproject(touchPos);
			if(touchPos.y < MyGdxGame.hR/4){
				MyGdxGame.gameScreen = new GameScreen(game,MyGdxGame.getLandscapeGenerator()); 
				game.setScreen(MyGdxGame.mainMenuScreen);
			}
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
		// TODO Auto-generated method stub
		
	}

}
