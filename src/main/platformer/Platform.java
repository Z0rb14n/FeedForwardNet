package platformer;

import processing.core.PVector;

import java.awt.*;

// Represents a platform that can be jumped on
class Platform {
    private static final Color PLATFORM_COLOR = new Color(0, 100, 0);
    private static final int HEIGHT = 50;
    static final int MIN_WIDTH = 100;
    private int width;
    private int xpos;
    private int ypos;

    // EFFECTS: initializes the platform with given width, x position and y position
    Platform(int width, int xpos, int ypos) {
        if (width < MIN_WIDTH) this.width = MIN_WIDTH;
        else this.width = width;
        this.xpos = xpos;
        this.ypos = ypos;
    }

    // MODIFIES: g
    // EFFECTS: draws this platform onto the graphics object
    void draw(Graphics2D g) {
        g.setColor(PLATFORM_COLOR);
        g.fill(getBoundingBox());
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1));
        g.draw(getBoundingBox());
    }

    // EFFECTS: returns the bounding box of this rectangle
    private Rectangle getBoundingBox() {
        return new Rectangle(xpos, ypos, width, HEIGHT);
    }

    // EFFECTS: returns the x position of this rectangle
    int getXPosition() {
        return xpos;
    }

    // EFFECTS: returns the y position of this rectangle
    int getYPosition() {
        return ypos;
    }

    // EFFECTS: returns the width of this rectangle
    int getWidth() {
        return width;
    }

    // EFFECTS: determines if the platformerAI is above this platform
    boolean isPlayerAbove(PlatformerAI ai) {
        PVector pos = ai.getPosition();
        boolean yBoundsMet = ai.getPlayerBottomVerticalPos() <= ypos;
        if (!yBoundsMet) return false;
        boolean bool2 = pos.x + PlatformerAI.PLAYER_WIDTH <= xpos + width && pos.x + PlatformerAI.PLAYER_WIDTH >= xpos;
        if (bool2) return true;
        else return pos.x <= xpos + width && pos.x >= xpos;
    }

    // EFFECTS: determines if the ai is on this platform
    boolean playerOnPlatform(PlatformerAI ai) {
        PVector pos = ai.getPosition();
        boolean yBoundsMet = Math.round(ai.getPlayerBottomVerticalPos()) == ypos;
        if (!yBoundsMet) return false;
        boolean bool2 = pos.x + PlatformerAI.PLAYER_WIDTH <= xpos + width && pos.x + PlatformerAI.PLAYER_WIDTH >= xpos;
        if (bool2) return true;
        else return pos.x <= xpos + width && pos.x >= xpos;
    }

    // MODIFIES: ai
    // EFFECTS: adjusts the platformerAI's position if they are inside this platform
    boolean adjustPlayerPosition(PlatformerAI ai) {
        if (isPlayerInside(ai)) {
            ai.forceCanJump(this.ypos);
            return true;
        }
        return false;
    }

    // EFFECTS: determines if the platformerAI is inside this platform
    boolean isPlayerInside(PlatformerAI ai) {
        return ai.getBoundingBox().intersects(getBoundingBox());
    }

    // EFFECTS: returns a deep copy of this platform
    Platform copy() {
        return new Platform(width, xpos, ypos);
    }

    @Override
    // EFFECTS: determines if this platform is equal to another object
    public boolean equals(Object o) {
        if (!(o instanceof Platform)) return false;
        Platform p = (Platform) o;
        return p.width == width && p.xpos == xpos && p.ypos == ypos;
    }

    @Override
    // EFFECTS: returns the hashCode of this object
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + width;
        hash = 31 * hash + xpos;
        hash = 31 * hash + ypos;
        return hash;
    }
}
