package core;

import processing.core.PVector;

public class BoxCollider {
    public PVector topLeft;
    public PVector bottomRight;

    public BoxCollider(PVector topLeft, PVector bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public boolean inBox(PVector point) {
        return topLeft.x <= point.x &&
                point.x <= bottomRight.x &&
                topLeft.y <= point.y &&
                point.y <= bottomRight.y;
    }
}
