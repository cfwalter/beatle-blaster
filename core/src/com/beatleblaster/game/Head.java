package com.beatleblaster.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Head extends Rectangle {
    int health;
    int angle;

    Texture image;
    Color color;

    long lastShotTime;
    Gun gun;

    public Head(Texture setImage, int setAngle, Color setColor, Gun setGun) {
        image = setImage;
        angle = setAngle;
        health = 15;
        height = 40;
        width = 40;
        color = setColor;
        gun = setGun;
        lastShotTime = 0;
    }

    public void damage() {
        if(--health < 0){
            health = 0;
        };
    }

    public void heal() {
        health++;
    }

    public void shoot(GameScreen screen) {
        gun.fire(screen, this);
    }
}
