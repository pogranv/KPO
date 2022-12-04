/**
 * Класс, позволяющий хранить координаты (x, y).
 */
public class Coordinates {

    /**
     * Конструктор, формирующий пару координат (x, y).
     * @param x координата по X.
     * @param y координата по Y.
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает координату x.
     * @return координата X.
     */
    public int getX() {
        return x;
    }

    /**
     * Возвращает координату y.
     * @return координата Y.
     */
    public int getY() {
        return y;
    }

    private int x;
    private int y;
}
