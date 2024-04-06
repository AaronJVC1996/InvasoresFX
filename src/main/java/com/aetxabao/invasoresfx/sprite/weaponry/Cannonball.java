package com.aetxabao.invasoresfx.sprite.weaponry;

import javafx.scene.image.Image;

/**
 * Disparo descendente sin animación
 */
public class Cannonball extends AShot {
    // Esta clase son las balas del enemigo, cuando creas una tienes que poner el ball sprite(su imagen)
    // Nuestra nave dispara Laserbeam y los enemigos dispara Cannonball

    // region attributes
    static int ROWS = 1;
    static int COLS = 1;
    static final int MAX_SPEED = 10;
    //endregion

    public Cannonball(Image img) {
        super(img,ROWS,COLS);
        xSpeed = 0;
        ySpeed = MAX_SPEED;
    }

    public void update() {
        x = x + xSpeed;
        y = y + ySpeed;
    }

}
