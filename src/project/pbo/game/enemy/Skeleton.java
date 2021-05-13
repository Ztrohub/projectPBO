package project.pbo.game.enemy;

import project.pbo.gfx.Assets;

import java.awt.*;

public class Skeleton extends Enemy {
    private int ctrEnemy;
    private int timer;

    public Skeleton( int stage) {
        super("Skeleton", stage, 1, 5);
    }

    @Override
    protected void cetak(Graphics g, int x, int y) {
        g.drawImage(Assets.mummyEnemy[ctrEnemy], (x*184)+57+this.x, (y*177)+90+this.y, 140, 130, null);

        if(timer == 0) {
            ctrEnemy = (ctrEnemy == Assets.mummyEnemy.length - 1) ? 0 : ++ctrEnemy;
            this.timer++;
        } else {
            if(timer == 7) this.timer = 0;
            else this.timer++;
        }
    }
}
