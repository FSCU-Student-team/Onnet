package Shapes;

import com.jogamp.opengl.GL2;

public record Color(double r, double g, double b, double a) {

    //Pre-defined colors
    public static final Color WHITE = new Color(1, 1, 1, 1);
    public static final Color BLACK = new Color(0, 0, 0, 1);
    public static final Color RED = new Color(1, 0, 0, 1);
    public static final Color GREEN = new Color(0, 1, 0, 1);
    public static final Color BLUE = new Color(0, 0, 1, 1);
    public static final Color YELLOW = new Color(1, 1, 0, 1);
    public static final Color MAGENTA = new Color(1, 0, 1, 1);
    public static final Color CYAN = new Color(0, 1, 1, 1);
    public static final Color GRAY = new Color(0.5, 0.5, 0.5, 1);
    public static final Color LIGHT_GRAY = new Color(0.7, 0.7, 0.7, 1);
    public static final Color DARK_GRAY = new Color(0.2, 0.2, 0.2, 1);
    public static final Color DARK_BROWN = new Color(139.0 / 255, 69.0 / 255, 19.0 / 255, 1);
    public static final Color SKY = new Color(95 / 255.0, 187 / 255.0, 227 / 255.0);
    public static final Color ORANGE = new Color("#fc7905aa");
    public static final Color PURPLE = new Color("#ae05fcaa");
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    //constructors
    public Color {
        r = clamp(r);
        g = clamp(g);
        b = clamp(b);
        a = clamp(a);
    }

    public Color(double gray) {
        this(gray, gray, gray);
    }


    public Color(double r, double g, double b) {
        this(r, g, b, 1);
    }

    public Color(String hex) {
        if (!hex.matches("^#[A-Fa-f0-9]{6}([A-Fa-f0-9]{2})?$")) {
            throw new IllegalArgumentException("Invalid hex color: " + hex);
        }

        this(
                parseHex(hex, 0),
                parseHex(hex, 2),
                parseHex(hex, 4),
                hex.length() == 9 ? parseHex(hex, 6) : 1.0
        );
    }

    //helper method, takes a hex string and returns a double between 0 and 1
    private static double parseHex(String hex, int start) {
        return Integer.parseInt(hex.substring(1 + start, 3 + start), 16) / 255.0;
    }

    // blends a color over another color returning a new color
    public Color blendOver(Color backdrop) {
        double outA = a + backdrop.a * (1 - a);

        if (outA < 1e-6) return TRANSPARENT;

        double outR = (r * a + backdrop.r * backdrop.a * (1 - a)) / outA;
        double outG = (g * a + backdrop.g * backdrop.a * (1 - a)) / outA;
        double outB = (b * a + backdrop.b * backdrop.a * (1 - a)) / outA;

        return new Color(outR, outG, outB, outA);
    }

    //clamps a double between 0 and 1, filtering out negatives and values beyond 1
    private static double clamp(double v) {
        return Math.max(0.0, Math.min(1.0, v));
    }

    //Clones the color (creates a new object)
    public Color copy() {
        return new Color(r, g, b, a);
    }

    //self-explanatory
    public double[] toArray() {
        return new double[]{r, g, b, a};
    }

    //Makes GL calls to set the color
    public void useColorGl(GL2 gl) {
        gl.glColor4d(r, g, b, a);
    }

    //Makes GL calls to set the clear color with current
    public void clearColorGl(GL2 gl) {
        gl.glClearColor((float) r, (float) g, (float) b, (float) a);
    }

    //returns a hex string (RGB) representation of the color
    public String toHexString() {
        return String.format("#%02X%02X%02X", (int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

    //returns a hex string (RGBA) representation of the color
    public String toHexStringAlpha() {
        return String.format("#%02X%02X%02X%02X", (int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255));
    }

    //self-explanatory
    @Override
    public String toString() {
        return String.format("Color(%.2f, %.2f, %.2f, %.2f)", r, g, b, a);
    }
}
