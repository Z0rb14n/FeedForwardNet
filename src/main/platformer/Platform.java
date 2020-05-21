package platformer;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

class Platform {
    private static final int HEIGHT = 50;
    static final int MIN_WIDTH = 100;
    private static final int COLOR = PlatformerUI.PLATFORM_COLOR;
    private static final int BLACK = PlatformerUI.BLACK;
    private int width;
    private int xpos;
    private int ypos;

    Platform(int width, int xpos, int ypos) {
        if (width < MIN_WIDTH) this.width = MIN_WIDTH;
        else this.width = width;
        this.xpos = xpos;
        this.ypos = ypos;
    }

    void draw(PApplet surface) {
        surface.pushStyle();
        surface.fill(COLOR);
        surface.stroke(BLACK);
        surface.strokeWeight(1);
        surface.rect(xpos, ypos, width, HEIGHT);
        surface.popStyle();
    }

    private Rectangle getBoundingBox() {
        return new Rectangle(xpos, ypos, width, HEIGHT);
    }

    int getXPosition() {
        return xpos;
    }

    int getYPosition() {
        return ypos;
    }

    int getWidth() {
        return width;
    }

    boolean isPlayerAbove(PlatformerAI ai) {
        PVector pos = ai.getPosition();
        boolean yBoundsMet = ai.getPlayerBottomVerticalPos() <= ypos;
        if (!yBoundsMet) return false;
        boolean bool2 = pos.x + PlatformerAI.PLAYER_WIDTH <= xpos + width && pos.x + PlatformerAI.PLAYER_WIDTH >= xpos;
        if (bool2) return true;
        else return pos.x <= xpos + width && pos.x >= xpos;
    }

    boolean playerOnPlatform(PlatformerAI ai) {
        PVector pos = ai.getPosition();
        boolean yBoundsMet = Math.round(ai.getPlayerBottomVerticalPos()) == ypos;
        if (!yBoundsMet) return false;
        boolean bool2 = pos.x + PlatformerAI.PLAYER_WIDTH <= xpos + width && pos.x + PlatformerAI.PLAYER_WIDTH >= xpos;
        if (bool2) return true;
        else return pos.x <= xpos + width && pos.x >= xpos;
    }

    boolean adjustPlayerPosition(PlatformerAI ai) {
        if (isPlayerInside(ai)) {
            ai.forceCanJump(this.ypos);
            return true;
        }
        return false;
    }

    boolean isPlayerInside(PlatformerAI ai) {
        return ai.getBoundingBox().intersects(getBoundingBox());
    }

    Platform copy() {
        return new Platform(width, xpos, ypos);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Platform)) return false;
        Platform p = (Platform) o;
        return p.width == width && p.xpos == xpos && p.ypos == ypos;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + width;
        hash = 31 * hash + xpos;
        hash = 31 * hash + ypos;
        return hash;
    }
}
