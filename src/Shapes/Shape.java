package Shapes;

import com.jogamp.opengl.GL2;

public interface Shape {


     // ----------------- Shape methods -----------------
     void setCenter(Point origin);

     Point getCenter();

     double getWidth();

     double getHeight();

     void Scale(double Factor);

     void Rotate(double Angle);

     void Move(double x, double y);

     void Draw(GL2 gl);

     Shape Copy();

     Shape Copy(Shape ref);
}
