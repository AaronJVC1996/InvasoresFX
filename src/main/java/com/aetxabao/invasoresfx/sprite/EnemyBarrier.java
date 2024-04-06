package com.aetxabao.invasoresfx.sprite;

import com.aetxabao.invasoresfx.util.Rect;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

import static com.aetxabao.invasoresfx.game.AppConsts.*;
import static com.aetxabao.invasoresfx.game.AppConsts.ENEMYSHIP_ALFA;

public class EnemyBarrier extends AEnemy implements IHaveShield, ICanSpawn {
    private int escudoEspacial;
    int N;//ticks para cambio de frame
    int n;
    Rect gameRect;

    public EnemyBarrier(Rect gameRect, Image img, int N) {
        super(img, ENEMYSHIP_ROWS, 1);
        this.gameRect = gameRect;
        xSpeed = ENEMYSHIP_MAX_SPEED;
        x = gameRect.left + width / 2;
        this.N = N;
        this.n = 0;
        this.escudoEspacial = 21;
    }

    public void updateFrame() {
        if (++n == N) {
            n = 0;
            currentFrame = ++currentFrame % cols;
        }
    }

    @Override
    public Rect getRect() {
        return new Rect(x, y, (int) (x + ENEMYSHIP_ALFA * width), (int) (y + ENEMYSHIP_ALFA * height));
    }

    @Override
    public void update() {
        if (x > gameRect.right - width - xSpeed || x + xSpeed < gameRect.left) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        y = y + ySpeed;
        updateFrame();
    }

    @Override
    public void draw(GraphicsContext gc) {
        int srcX = currentFrame * width;
        int srcY = 0;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, (int) (x + ENEMYSHIP_ALFA * width), (int) (y + ENEMYSHIP_ALFA * height));
        gc.drawImage(img, src.left, src.top, src.width(), src.height(),
                dst.left, dst.top, dst.width(), dst.height());
    }


    @Override
    public boolean impact() {
        if (escudoEspacial > 0) {
            escudoEspacial--; // Restar 1 a la resistencia del escudo
            return false; // El enemigo no muere por que tiene escudo
        } else {
            // El escudo se ha terminado, es 0
            return true; // El enemigo muere por que ya no tiene escudo
        }
    }

    @Override
    // todo Aqui me quedo para cuano despierte seguir, de momento se crean los enemigos cada 5 ataques pero
    // se crean con la misma imagen que el boss y no con la imagen de el asteroide, tengo que investigar un poco
    public List<AEnemy> spawn() {
            List<AEnemy> enemies = new ArrayList<>();
            if (escudoEspacial % 5 == 0) {
                for (int i = 0; i < 3; i++) {
                              Obstaculo obstaculo = new Obstaculo(gameRect, img, N);
                    obstaculo.setPos(i +2 , 0);
                    enemies.add(obstaculo); //
                }
            }
            return enemies;

    }
}
