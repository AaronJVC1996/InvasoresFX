package com.aetxabao.invasoresfx.game;

import com.aetxabao.invasoresfx.game.enums.EEnemyShot;
import com.aetxabao.invasoresfx.game.enums.EEnemyType;
import com.aetxabao.invasoresfx.sprite.*;
import com.aetxabao.invasoresfx.sprite.weaponry.Gun;
import com.aetxabao.invasoresfx.util.Rect;
import javafx.scene.image.Image;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.aetxabao.invasoresfx.game.AppConsts.*;
import static com.aetxabao.invasoresfx.game.enums.EEnemyShot.*;
import static com.aetxabao.invasoresfx.game.enums.EEnemyType.*;

public class EnemySpawner {
    private static Logger log1 = Logger.getLogger(EnemyShip.class);
    private static Logger log2 = Logger.getLogger(EnemyShipDiagonal.class);
    private static Logger log3 = Logger.getLogger(EnemyShipGroup.class);
    private static Logger log4 = Logger.getLogger(EnemigoNuevo.class);

    //region attributes
    public static int n = 8;
    public static int m = 16;
    public static int vx = 5;
    public static int vy = 3;
    public static int ticks = 100;
    //endregion

    /**
     * Transforma una coordenada en una posición
     *
     * @param i coordenada de 0 a n eje horizontal
     * @return posicion x
     */
    private static int getX(Rect gameRect, int i) {
        return gameRect.left + i * gameRect.width() / n;
    }

    /**
     * Transforma una coordenada en una posición
     *
     * @param j coordenada de 0 a m eje vertical
     * @return posicion y
     */
    private static int getY(Rect gameRect, int j) {
        return gameRect.top + j * gameRect.height() / m;
    }

    public static List<AEnemy> createEnemies(Rect gameRect, int level) {
        List<AEnemy> enemies = new ArrayList<>();
        level = level % LEVELS;

        switch (level) {
            case 1:
                enemies = Prueba(gameRect);
                //enemies = crearEnemigosNivelDonut(gameRect);
                break;
            case 2:
                enemies = crearEnemigosNivelPaquito(gameRect);
                break;
            case 3:
                enemies = crearEnemigosNivelPulpo(gameRect);
                break;
            case 4:
            default:
                enemies = crearEnemigosNivelAaron(gameRect);
                break;
        }
        return enemies;
    }

    /**
     * Crea un enemigo en una coordenada (i,j) con una velocidad (vx,vy)
     *
     * @param i  coordenada horizontal
     * @param j  coordenada vertical
     * @param vx velocidad eje x
     * @param vy velocidad eje y
     * @return una instancia del enemigo
     */
    public static EnemyShip createEnemyShip(EEnemyType type, Image enemyImage, Rect gameRect, int i, int j, int vx, int vy, EEnemyShot shot) {
        EnemyShip e;
        switch (type) {
            case E_DIAGONAL:
                e = new EnemyShipDiagonal(gameRect, enemyImage, TICKSxFRAME);
                break;
            case E_NORMAL:
            default:
                e = new EnemyShip(gameRect, enemyImage, TICKSxFRAME);
                break;
        }
        if (shot == E_SHOT_GUN) {
            e.setWeapon(new Gun());
        }
        e.setPos(getX(gameRect, i), getY(gameRect, j));
        e.setXSpeed(vx);
        e.setYSpeed(vy);
        return e;
    }

    public static List<AEnemy> crearEnemigosNivelDonut(Rect gameRect) {
        List<AEnemy> enemies = new ArrayList<>();
        enemies.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 0, 0, vx, 0, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 7, 1, -vx, 0, E_SHOT_GUN));
        List<EnemyShip> el1 = new ArrayList<>();
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 2, 3, 0, 0, E_SHOT_NOTHING));
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 3, 2, 0, 0, E_SHOT_NOTHING));
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 4, 2, 0, 0, E_SHOT_NOTHING));
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 5, 3, 0, 0, E_SHOT_NOTHING));
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 2, 4, 0, 0, E_SHOT_NOTHING));
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 3, 5, 0, 0, E_SHOT_NOTHING));
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 4, 5, 0, 0, E_SHOT_NOTHING));
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 5, 4, 0, 0, E_SHOT_NOTHING));
        EnemyShipGroup eg1 = new EnemyShipGroup(gameRect, el1);
        eg1.setXSpeed(vx);
        enemies.add(eg1);
        contarEnemigos(enemies);
        return enemies;
    }

    public static List<AEnemy> crearEnemigosNivelPaquito(Rect gameRect) {
        List<AEnemy> enemies = new ArrayList<>();
        enemies.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 0, 0, vx, 0, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 7, 1, -vx, 0, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 3, 2, vx, 0, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 4, 3, -vx, 0, E_SHOT_GUN));
        List<EnemyShip> el1 = new ArrayList<>();
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_1, gameRect, 2, 4, vx, 0, E_SHOT_NOTHING));
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_1, gameRect, 3, 4, vx, 0, E_SHOT_NOTHING));
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_1, gameRect, 4, 4, vx, 0, E_SHOT_NOTHING));
        el1.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_1, gameRect, 5, 4, vx, 0, E_SHOT_NOTHING));
        EnemyShipGroup eg1 = new EnemyShipGroup(gameRect, el1);
        eg1.setXSpeed(vx);
        enemies.add(eg1);
        contarEnemigos(enemies);
        return enemies;
    }

    public static List<AEnemy> crearEnemigosNivelPulpo(Rect gameRect) {
        List<AEnemy> enemies = new ArrayList<>();
        enemies.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 0, 0, vx, 0, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 7, 1, -vx, 0, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_DIAGONAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 0, 0, vx, vy, E_SHOT_NOTHING));
        enemies.add(createEnemyShip(E_DIAGONAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 7, 0, -vx, vy, E_SHOT_NOTHING));
        enemies.add(createEnemyShip(E_DIAGONAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 3, 0, -vx, vy, E_SHOT_NOTHING));
        enemies.add(createEnemyShip(E_DIAGONAL, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 4, 0, vx, vy, E_SHOT_NOTHING));
        contarEnemigos(enemies);
        return enemies;
    }

    public static List<AEnemy> crearEnemigosNivelAaron(Rect gameRect) {
        List<AEnemy> enemies = new ArrayList<>();
        enemies.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_1, gameRect, 0, 0, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_NORMAL, ENEMYSHIP_SPRITE_IMAGE_1, gameRect, 0, 1, -vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_ONEWAY, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 0, 2, -vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_ONEWAY, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 1, 0, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_DIAGONAL, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 1, 1, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_DIAGONAL, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 1, 2, -vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_SINU, ENEMYSHIP_SPRITE_IMAGE_1, gameRect, 2, 0, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_SINU, ENEMYSHIP_SPRITE_IMAGE_1, gameRect, 2, 1, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_ROCKET, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 2, 2, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_ROCKET, ENEMYSHIP_SPRITE_IMAGE_2, gameRect, 3, 0, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_BARRIER, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 3, 1, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_BARRIER, ENEMYSHIP_SPRITE_IMAGE_3, gameRect, 3, 2, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_BARRIERDOWN, ENEMYBARRIER4_SPRITE_IMAGE, gameRect, 4, 0, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_BARRIERDOWN, ENEMYBARRIER4_SPRITE_IMAGE, gameRect, 4, 1, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_TOWER, ENEMYTOWER3_SPRITE_IMAGE, gameRect, 4, 2, vx, vy, E_SHOT_GUN));
        enemies.add(createEnemyShip(E_TOWER, ENEMYTOWER3_SPRITE_IMAGE, gameRect, 5, 3, vx, vy, E_SHOT_GUN));
        contarEnemigos(enemies);
        return enemies;
    }

    public static List<AEnemy> Prueba(Rect gameRect) {
        List<AEnemy> enemies = new ArrayList<>();
        enemies.add(createEnemyShip(E_BARRIERDOWN, ENEMYBARRIER4_SPRITE_IMAGE, gameRect, 0, 0, 10, 0, E_SHOT_GUN));
        contarEnemigos(enemies);
        return enemies;

    }

    private static void contarEnemigos(List<AEnemy> enemies) {
        // He creado este metodo para que el codigo este mas limpio en los niveles que es donde lo llamaremos
        // asi solo llamo este metodo y le inserto la lista de enemigos.

        int normales = 0;
        for (AEnemy enemy : enemies) {
            if (enemy instanceof EnemyShip) { // Suponiendo que DiagonalEnemy es la clase para enemigos diagonales
                normales++;
            }
        }

        int diagonales = 0;
        for (AEnemy enemy : enemies) {
            if (enemy instanceof EnemyShipDiagonal) { // Suponiendo que DiagonalEnemy es la clase para enemigos diagonales
                diagonales++;
            }
        }

        int grupos = 0;
        for (AEnemy enemy : enemies) {
            if (enemy instanceof EnemyShipGroup) { // Suponiendo que DiagonalEnemy es la clase para enemigos diagonales
                grupos++;
            }
        }

        int nuevos = 0;
        for (AEnemy enemy : enemies) {
            if (enemy instanceof EnemigoNuevo) { // Suponiendo que DiagonalEnemy es la clase para enemigos diagonales
                nuevos++;
            }
        }

        if (normales > 0) {log1.debug("Cantidad de EnemyShip N= " + normales);}
        if (diagonales > 0) {log2.debug("Cantidad de EnemyShipDiagonal N= " + diagonales);}
        if (grupos > 0) {log3.debug("Cantidad de EnemyShipGroup (Grupo de enemigos) N= " + grupos);}
        if (nuevos > 0) {log4.debug("Cantidad de EnemigoNuevo N= " + nuevos);}
    }
}