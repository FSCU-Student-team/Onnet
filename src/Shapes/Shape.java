package Shapes;

import com.jogamp.opengl.GL2;

public interface Shape {

     void setCenter(Point center);

     Point getCenter();

     void setWidth(double width);
     double getWidth();

     void setHeight(double height);
     double getHeight();

     void Scale(double Factor);

     void Rotate(double Angle);

     void Move(double x, double y);

     void Draw(GL2 gl);

     Shape Copy();

     Shape Copy(Shape ref);
}
