package Game;

public enum Input {

    //keyboard flags (google bitwise flags in enums)
    UP(1),
    DOWN(1 << 1),
    LEFT(1 << 2),
    RIGHT(1 << 3),
    Z(1 << 4),
    X(1 << 5),
    C(1 << 6),
    MOUSE_LEFT(1 << 7),
    A(1 << 8),
    D(1 << 9),
    PLUS(1 << 10),
    MINUS(1 << 11),
    EQUALS(1 << 12),
    R(1 << 13),
    W(1 << 14),
    S(1 << 15),
    Space(1 << 16),
    Escape(1 << 17);


    public final int bit;

    Input(int bit) {
        this.bit = bit;
    }

    //checks whether input is set or not
    public static boolean isSet(int state, Input flag) {
        return (state & flag.bit) != 0;
    }

    //sets input to true
    public static int set(int state, Input flag) {
        return state | flag.bit;
    }

    //sets input to false
    public static int clear(int state, Input flag) {
        return state & ~flag.bit;
    }

    //toggles input
    public static int toggle(int state, Input flag) {
        return state ^ flag.bit;
    }

}
