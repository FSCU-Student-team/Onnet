package Physics;

import Shapes.Color;
import Shapes.Point;
import Shapes.Vector2;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {
    public static double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    //makes a new point with bounds randomly
    public static Point randomPoint(double minX, double maxX, double minY, double maxY) {
        return new Point(randomDouble(minX, maxX), randomDouble(minY, maxY));
    }

    //makes a new point with bounds randomly with x and y having the same bounds
    public static Point randomPoint(double min, double max) {
        return new Point(randomDouble(min, max), randomDouble(min, max));
    }

    //makes a new vector with bounds randomly
    public static Vector2 randomVector(double min, double max) {
        return new Vector2(randomDouble(min, max), randomDouble(min, max));
    }

    //makes a new vector with bounds randomly with x and y having the same bounds
    public static Vector2 randomVector(double minX, double maxX, double minY, double maxY) {
        return new Vector2(randomDouble(minX, maxX), randomDouble(minY, maxY));
    }

    //makes a new normalized vector with a random direction
    public static Vector2 randomUnitVector() {
        double angle = randomAngleRad();
        return new Vector2(Math.cos(angle), Math.sin(angle));
    }

    public static double randomAngle() {
        return randomDouble(0, 360);
    }

    public static double randomAngle(double min, double max) {
        return randomDouble(min, max);
    }

    //generates a random angle in radians
    public static double randomAngleRad() {
        return Math.toRadians(randomDouble(0, 360));
    }

    //generates a random angle in radians with bounds
    public static double randomAngleRad(double min, double max) {
        return Math.toRadians(randomDouble(min, max));
    }

    //generates a random color (RGB)
    public static Color randomColor() {
        return new Color(randomDouble(0, 255), randomDouble(0, 255), randomDouble(0, 255));
    }

    //chooses a random element from a list
    public static <T> T randomChoice(List<T> list) {
        if (list.isEmpty()) throw new IllegalArgumentException("List is empty");
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}
