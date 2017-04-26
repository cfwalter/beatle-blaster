package com.beatleblaster.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    final BeatleBlaster game;
    Texture enemyImage;
    Texture ballImage;
    Texture johnImage;
    Texture paulImage;
    Texture georgeImage;
    Texture ringoImage;
    OrthographicCamera camera;
    Rectangle ship;
    Array<Actor> enemies;
    Array<Bullet> bullets;
    Array<Head> heads;
    long lastDropTime;
    int angle;
    int textY;
    int textX;
    int score;
    int rotating;

    public GameScreen(final BeatleBlaster gam) {
        this.game = gam;

        // load the images for the droplet and the ship, 64x64 pixels each
        enemyImage = new Texture(Gdx.files.internal("enemy.png"));
        ballImage = new Texture(Gdx.files.internal("ball.gif"));
        johnImage = new Texture(Gdx.files.internal("john.png"));
        paulImage = new Texture(Gdx.files.internal("paul.png"));
        georgeImage = new Texture(Gdx.files.internal("george.png"));
        ringoImage = new Texture(Gdx.files.internal("ringo.png"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 500, 1000);

        // create a Rectangle to logically represent the ship
        ship = new Rectangle();
        ship.x = 500 / 2 - 64 / 2; // center the ship horizontally
        ship.y = 20; // bottom left corner of the ship is 20 pixels above
        // the bottom screen edge
        ship.width = 0;
        ship.height = 0;

        heads = new Array<Head>();
        class JohnGun implements Gun {
            @Override
            public void fire(GameScreen screen, Head head) {
                if (TimeUtils.nanoTime() - head.lastShotTime > 50000000) {
                    Bullet bullet = new Bullet(ballImage, 0.0, 1000.0);
                    double rad = Math.toRadians( 360 * (double) TimeUtils.millis() / 1000.0 );
                    bullet.x = head.x + head.width/2 + (int) ( head.width/3.0 * Math.cos(rad) );
                    bullet.y = head.y;
                    bullet.height = 5;
                    bullet.width = 5;
                    screen.bullets.add(bullet);
                    head.lastShotTime = TimeUtils.nanoTime();
                }
            }
        }
        class PaulGun implements Gun {
            @Override
            public void fire(GameScreen screen, Head head) {
                if (TimeUtils.nanoTime() - head.lastShotTime > 100000000) {
                    for (int i = 0; i < 2; i++) {
                        Bullet bullet = new Bullet(ballImage, 0.0, 1000.0);
                        bullet.x = head.x + (head.width)*i;
                        bullet.y = head.y;
                        bullet.height = 5;
                        bullet.width = 5;
                        screen.bullets.add(bullet);
                    }
                    head.lastShotTime = TimeUtils.nanoTime();
                }
            }
        }
        class GeorgeGun implements Gun {
            @Override
            public void fire(GameScreen screen, Head head) {
                if (TimeUtils.nanoTime() - head.lastShotTime > 300000000) {
                    for (int i = 0; i < 5; i++) {
                        Bullet bullet = new Bullet(ballImage, 0.0, 700.0);
                        bullet.x = head.x + (head.width /2);
                        bullet.y = head.y-i;
                        bullet.height = 5;
                        bullet.width = 5;
                        screen.bullets.add(bullet);
                    }
                    head.lastShotTime = TimeUtils.nanoTime();
                }
            }
        }
        class RingoGun implements Gun {
            @Override
            public void fire(GameScreen screen, Head head) {
                if (TimeUtils.nanoTime() - head.lastShotTime > 300000000) {
                    for (int i = 0; i < 5; i++) {
                        double angle = Math.toRadians(93.0 - 1.5*i);
                        double dx = 1000.0 * Math.cos(angle);
                        double dy = 1000.0 * Math.sin(angle);
                        Bullet bullet = new Bullet(ballImage, dx, dy);
                        bullet.x = head.x + (head.width/5)*i;
                        bullet.y = head.y - Math.abs(2*(i-2));
                        bullet.height = 5;
                        bullet.width = 5;
                        screen.bullets.add(bullet);
                    }
                    head.lastShotTime = TimeUtils.nanoTime();
                }
            }
        }
        heads.add( new Head(johnImage, 90, new Color(0.95f, 0.26f, 0.78f, 1), new JohnGun()) );
        heads.add( new Head(paulImage, 180, new Color(0.26f, 0.78f, 0.95f, 1), new PaulGun()) );
        heads.add( new Head(georgeImage, 0, new Color(0.95f, 0.26f, 0.26f, 1), new GeorgeGun()) );
        heads.add( new Head(ringoImage, 270, new Color(0.61f, 0.95f, 0.26f, 1), new RingoGun()) );

        // create the enemies array and spawn the first enemy
        enemies = new Array<Actor>();
        bullets = new Array<Bullet>();
        spawnEnemy();

        angle = 0;
        textX = 50;
        textY = 900;
        score = 0;
        rotating = 0;
    }

    private void spawnEnemy() {
        Actor enemy = new Actor(enemyImage);
        enemy.x = MathUtils.random(0, 500 - 64);
        enemy.y = 1600;
        enemies.add(enemy);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the ship and
        // all drops
        game.batch.begin();

        angle += (30 * rotating) % 360;
        if (angle % 90 == 0) {
            rotating = 0;
        }
        Head topHead = null;
        Iterator<Head> headIter = heads.iterator();
        while (headIter.hasNext()) {
            Head head = headIter.next();
            double rad = Math.toRadians(angle + head.angle);
            if ((360 + (angle%360) + head.angle)%360 == 90) {
                topHead = head;
            }
            game.font.setColor(head.color);
            game.font.draw(
                    game.batch, Integer.toHexString(head.health).toUpperCase(),
                    (int) (textX + 25 * Math.cos(rad)),
                    (int) (textY + 25 * Math.sin(rad))
            );
            head.setCenter(
                    (int) (ship.x + 25 * Math.cos(rad)),
                    (int) (ship.y + 25 * Math.sin(rad))
            );
            game.batch.draw(head.image, head.x, head.y, head.width, head.height);
            if (head.health <= 0) {
                headIter.remove();
            }
        }
        game.font.setColor(1,1,1,1);
        game.font.draw( game.batch, Integer.toString(score),textX + 100, textY );
        for (Actor enemy : enemies) {
            game.batch.draw(enemy.image, enemy.x, enemy.y, enemy.width, enemy.height);
        }
        for (Bullet bullet : bullets) {
            game.batch.draw(bullet.image, bullet.x, bullet.y, bullet.width, bullet.height);
        }
        game.batch.end();

        // process user input
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            ship.x -= 400 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            ship.x += 400 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.UP))
            ship.y += 400 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.DOWN))
            ship.y -= 400 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyJustPressed(Keys.E))
            rotating = 1;
        if (Gdx.input.isKeyJustPressed(Keys.R))
            rotating = -1;
        if (Gdx.input.isKeyPressed(Keys.SPACE))
            if (topHead != null) {
                topHead.shoot(this);
            }

        // make sure the ship stays within the screen bounds
        if (ship.x < 30)
            ship.x = 30;
        if (ship.x > 500 - 60)
            ship.x = 500 - 60;
        if (ship.y < 30)
            ship.y = 30;

        // check if we need to create a new enemy
        if (TimeUtils.nanoTime() - lastDropTime > 200000000)
            spawnEnemy();

        // move the enemies, remove any that are beneath the bottom edge of
        // the screen or that hit the john. In the later case we play back
        // a sound effect as well.
        Iterator<Actor> iter = enemies.iterator();
        while (iter.hasNext()) {
            Actor enemy = iter.next();
            enemy.y -= 800 * Gdx.graphics.getDeltaTime();
            for (Head head : heads) {
                if (enemy.overlaps(head)) {
                    head.damage();
                    enemy.y = -100;
                }
            }
            for (Rectangle bullet : bullets) {
                if (enemy.overlaps(bullet)) {
                    enemy.damage(this);
                    bullet.y = -100;
                }
            }
            if (enemy.y + 64 < 0)
                iter.remove();
        }
        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            bullet.update(Gdx.graphics.getDeltaTime());

            if (bullet.y < 0) {
                bulletIter.remove();
            }
            else if (bullet.y > 1000) {
                bulletIter.remove();
            }
        }

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
//        rainMusic.play();
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
        enemyImage.dispose();
        johnImage.dispose();
        paulImage.dispose();
        georgeImage.dispose();
        ringoImage.dispose();
    }

}