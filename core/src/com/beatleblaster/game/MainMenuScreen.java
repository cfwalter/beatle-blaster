package com.beatleblaster.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenuScreen implements Screen {
    final BeatleBlaster game;
    OrthographicCamera camera;

    public MainMenuScreen(final BeatleBlaster gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 500, 1000);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.getData().setScale(2.0f);
        game.font.setColor(1,1,1,1);
        game.font.draw(game.batch, "Welcome to Beatle Blaster!", 100, 250);
        game.font.draw(game.batch, "Press ENTER to begin", 100, 200);
        game.font.draw(game.batch, "SPACE to shoot", 100, 150);
        game.font.draw(game.batch, "R and E to rotate", 100, 100);
        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}