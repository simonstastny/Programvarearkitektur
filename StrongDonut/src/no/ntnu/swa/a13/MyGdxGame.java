package no.ntnu.swa.a13;

import no.ntnu.swa.a13.screens.*;

import com.badlogic.gdx.Game;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.GL10;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.Texture.TextureFilter;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends Game {
//	private OrthographicCamera camera;
//	private SpriteBatch batch;
//	private Texture texture;
//	private Sprite sprite;
	
	MainMenuScreen mainMenuScreen;
	OptionsScreen optionsScreen;
	GameScreen gameScreen;	
	
	//Because it is easier to make and move code than the other way around
	//it might look a bit nasty here for a while - Sindre
	//At some point I will need to relinquish my love for global variables :(
	
	//Great discoveries made, making a separate folder for screens
	
	@Override
	public void create() {		
		
		mainMenuScreen = new MainMenuScreen(this);
		optionsScreen = new OptionsScreen(this);
		gameScreen = new GameScreen(this);
		
		setScreen(mainMenuScreen);
		
//		float w = Gdx.graphics.getWidth();
//		float h = Gdx.graphics.getHeight();
//		
//		
//		
//		camera = new OrthographicCamera(1, h/w);
//		batch = new SpriteBatch();
//		
//		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
//		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		
//		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
//		
//		sprite = new Sprite(region);
//		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
//		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
//		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
	}

//	@Override
//	public void dispose() {
//		batch.dispose();
//		texture.dispose();
//	}
//
//	@Override
//	public void render() {		
//		Gdx.gl.glClearColor(1, 1, 1, 1);
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		sprite.draw(batch);
//		batch.end();
//	}
//
//	@Override
//	public void resize(int width, int height) {
//	}
//
//	@Override
//	public void pause() {
//	}
//
//	@Override
//	public void resume() {
//	}
}
