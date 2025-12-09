package Shapes;

import Physics.Collision.Collider;
import com.jogamp.opengl.GL2;

public interface Shape {

     void setOrigin(Point origin);

     Point getCenter();

     double getWidth();

     double getHeight();

     void scale(double Factor);

     void rotate(double Angle);

    Collider getCollider();

    double getRestitution(); //basically bounce but restitution sounds fancy

    void move(Vector2 delta);

     void draw(GL2 gl);

     <T> T copy();

     Color getColor();
}
