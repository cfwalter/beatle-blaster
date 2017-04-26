package com.beatleblaster.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bullet extends Rectangle {
    double dx;
    double dy;

    Texture image;

    public Bullet(Texture setImage, double setDx, double setDy) {
        image = setImage;
        height = 5;
        width = 5;
        dx = setDx;
        dy = setDy;
    }

    public void update(float delta) {
        x += (int) (delta * dx);
        y += (int) (delta * dy);
    }
}
