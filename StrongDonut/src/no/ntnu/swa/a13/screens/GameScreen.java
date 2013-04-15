package no.ntnu.swa.a13.screens;

import no.ntnu.swa.a13.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScreen implements Screen {	
	
	MyGdxGame game;
	
	//The camera displays a given area across the entire screen.
	private OrthographicCamera camera;
	
	//the SpriteBatch takes care of rendering images
	private SpriteBatch batch;
	
	//Menu elements need textures
	private Texture newGameTex;
	private TextureRegion sketchRegion;
	
	public GameScreen(MyGdxGame gameRef){
		game = gameRef;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, MyGdxGame.w, MyGdxGame.h);
		batch = new SpriteBatch();
		batch.setTransformMatrix(MyGdxGame.scalingMatrix);
		
		newGameTex = new Texture(Gdx.files.internal("data/help_res/gamesketch_1.png"));
		newGameTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);//not sure if filter is needed
		
		sketchRegion = new TextureRegion(newGameTex, 0, 0, 1024, 512);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(sketchRegion, 0, 0, MyGdxGame.wR, MyGdxGame.hR);
		batch.end();
		
		// TODO Auto-generated method stub
		
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
