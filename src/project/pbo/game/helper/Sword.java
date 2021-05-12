package project.pbo.game.helper;

import project.pbo.game.Card;
import project.pbo.gfx.Assets;

import java.awt.*;
import java.util.Random;

public class Sword extends Card {
    private final int damage;
    public Sword() {
        super("Sword");
        damage = new Random().nextInt(8)+3;
    }

    @Override
    protected void cetak(Graphics g, int x, int y) {
        g.drawImage(Assets.card, (x*184)+40+this.x, (y*177)+80+this.y, 184, 177, null);
    }

    public int getDamage() {
        return damage;
    }

}
