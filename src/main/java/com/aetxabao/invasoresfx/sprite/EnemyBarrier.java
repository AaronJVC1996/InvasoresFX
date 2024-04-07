/**
 * @author Aaron Vasquez
 */
package com.aetxabao.invasoresfx.sprite;

import com.aetxabao.invasoresfx.Main;
import com.aetxabao.invasoresfx.util.Rect;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import static com.aetxabao.invasoresfx.game.AppConsts.*;
import static com.aetxabao.invasoresfx.game.AppConsts.ENEMYSHIP_ALFA;

public class EnemyBarrier extends AEnemy implements IHaveShield, ICanSpawn {
    private int escudoEspacial;
    private boolean ejecutado;
    int N;//ticks para cambio de frame
    int n;
    Rect gameRect;
    private static final Image ENEMYSHIP_OBSTACULO = new Image(Main.class.getResource("sprite/Obstaculo.png").toString());
    public EnemyBarrier(Rect gameRect, Image img, int N) {
        super(img, ENEMYSHIP_ROWS, 1);
        this.gameRect = gameRect;
        xSpeed = ENEMYSHIP_MAX_SPEED;
        x = gameRect.left + width / 2;
        this.N = N;
        this.n = 0;
        this.escudoEspacial = 33;
        this.ejecutado = false;
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
    // He añadido el metodo ICanSpawn bien implementado, he tenido que crear una nueva variable ejecutado debido a que
    // si no creaba esta variable al llegar el escudoEspacial a 5, ejecutaba obstaculos sin parar hasta pegarle
    // con otro disparo, con la variable boolean ejecutado ya no ocurre esto y esta perfecto
    public List<AEnemy> spawn() { // El boss tiene 33 vidas asi que lanzara 6 veces su habilidad spawn
        List<AEnemy> enemies = new ArrayList<>();
        if (escudoEspacial % 5 == 0 && !ejecutado) { // Ejecutado comienza como falso, es boolean
            for (int i = 0; i <= 4; i++) { // invoca 5 obstaculos
                // Cambio de imagen a enemyship_obstaculo para que no sea la misma que enemybarrier
                Obstaculo obstaculo = new Obstaculo(gameRect, ENEMYSHIP_OBSTACULO, N);
                obstaculo.setPos(i * 110, 0);// Posiciones horizontales variadas, vertical 0(arriba del mapa)
                obstaculo.setYSpeed(3); // Velocidad en vertical para que solo vaya hacia abajo
                obstaculo.setXSpeed(0); // Velocidad en horizontal 0 para que solo vaya recto en vertical
                enemies.add(obstaculo);
            }
            // Ejecutado true para decirle que ya se ejecuto una vez y no se ejecute mas hasta el siguiente if
            ejecutado = true;
        }
        // Este if vuelve a hacer a ejecutado false por lo tanto podra volver a crear los obstaculos
        if (escudoEspacial % 5 != 0) {
            ejecutado = false;
        }
        return enemies;
    }
}
