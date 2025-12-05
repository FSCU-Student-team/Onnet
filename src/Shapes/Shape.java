package Shapes;

import Physics.Collision.Collider;
import com.jogamp.opengl.GL2;

public interface Shape {

     void setOrigin(Point origin);

     Point getCenter();

     double getWidth();

     double getHeight();

     void Scale(double Factor);

     void Rotate(double Angle);

    Collider getCollider();

    void Move(Vector2 delta);

     void draw(GL2 gl);

     <T> T Copy();
}
