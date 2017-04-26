package com.beatleblaster.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Actor extends Rectangle {
    int health;

    Texture image;

    public Actor(Texture setImage) {
        image = setImage;
        health = 5;
        height = 50;
        width = 50;
    }

    public void damage(GameScreen screen) {
        health--;
        if (health <= 0){
            screen.score++;
            y = -100;
        }
    }
}
