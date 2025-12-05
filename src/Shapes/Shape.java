package Shapes;

import com.jogamp.opengl.GL2;

public interface Shape {

     void setOrigin(Point origin);

     Point getCenter();

     double getWidth();

     double getHeight();

     void Scale(double Factor);

     void Rotate(double Angle);

     void Move(Vector2 delta);

     void Draw(GL2 gl);

     <T> T Copy();
}
