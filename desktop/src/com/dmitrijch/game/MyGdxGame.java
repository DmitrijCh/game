package com.dmitrijch.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture myImageTexture;
    private float x, y;
    private OrthographicCamera camera;
    private Player player;

    @Override
    public void create() {
        batch = new SpriteBatch();
        myImageTexture = new Texture("street.png");
        x = 0;
        y = 0;

        // Установка логической ширины и высоты экрана
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float logicalWidth = 800;
        float logicalHeight = screenHeight / (screenWidth / logicalWidth);

        // Создание камеры с масштабированием
        camera = new OrthographicCamera(logicalWidth, logicalHeight);
        camera.position.set(logicalWidth / 2f, logicalHeight / 2f, 0);
        camera.update();

        // Создание персонажа
        player = new Player();
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        // Обновление персонажа
        player.update(deltaTime);

        // Очистка экрана
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Начало отрисовки
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Отрисовка фона
        batch.draw(myImageTexture, x, y, camera.viewportWidth, camera.viewportHeight);

        // Отрисовка персонажа
        player.render(batch);

        // Завершение отрисовки
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        myImageTexture.dispose();
    }
}