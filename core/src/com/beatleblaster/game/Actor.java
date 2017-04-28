package com.beatleblaster.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Actor extends Rectangle {
    int health;
    int dy;

    Texture image;

    public Actor(Texture setImage, int setSize) {
        image = setImage;
        health = 5;
        height = setSize;
        width = setSize;
        dy = MathUtils.random(600, 900);
    }

    public void damage(GameScreen screen) {
        health--;
        if (health <= 0){
            screen.score++;
            y = -100;
        }
    }
}
