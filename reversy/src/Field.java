import java.util.ArrayList;

/**
 * Класс, реализующий логику игрового поля игры "Реверси".
 */
public class Field {
    /**
     * Конструктор для создания игрового поля.
     * @param fieldSize размер игрового поля.
     */
    public Field(int fieldSize) {
        this.fieldSize = fieldSize;
        field = new int[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; ++i) {
            for (int j = 0; j < fieldSize; ++j) {
                field[i][j] = 0;
            }
        }
        field[fieldSize / 2 - 1][fieldSize / 2 - 1] = 1;
        field[fieldSize / 2][fieldSize / 2] = 1;
        field[fieldSize / 2 - 1][fieldSize / 2] = 2;
        field[fieldSize / 2][fieldSize / 2 - 1] = 2;
        currentPlayer = 2;
    }

    /**
     * Осуществляет проверку пустых клеток на поле.
     * @return true, если на поле есть свободные клетки и false в противном случае.
     */
    public boolean hasEmpyCells() {
        for (int i = 0; i < fieldSize; ++i) {
            for (int j = 0; j < fieldSize; ++j) {
                if (field[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Передает право хода другому игроку.
     */
    public void changeCurrentPlayer() {
        currentPlayer = currentPlayer % 2 + 1;
    }

    /**
     * Проверяет возможность совершения хода текущим игроком.
     * @return true, если текущий игрок имеет возможность совершить ход и false в противном случае.
     */
    public boolean isEnableNextMove() {
        if (!hasEmpyCells()) {
            return false;
        }
        for (int i = 0; i < fieldSize; ++i) {
            for (int j = 0; j < fieldSize; ++j) {
                if (isCorrectMove(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Получает номер текущего игрока.
     * @return номер текущего игрока (1 или 2).
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Совершает ход текущим игроком по координатам (x,y).
     * При этом все захваченные фишки меняют цвет.
     * @param x координата хода по x.
     * @param y координата хода по y.
     */
    public void doMove(int x, int y) {
        if (!isCorrectMove(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        for (int stepX = -1; stepX <= 1; ++stepX) {
            for (int stepY = -1; stepY <= 1; ++stepY) {
                if (stepX == 0 && stepY == 0) {
                    continue;
                }
                if (checkMoveByDirection(x,y, stepX, stepY)) {
                    int currentX = x;
                    int currentY = y;
                    while (isCorrectCoordinates(currentX + stepX, currentY + stepY)) {
                        if (field[currentX + stepX][currentY + stepY] == currentPlayer) {
                            break;
                        }
                        field[currentX + stepX][currentY + stepY] = currentPlayer;
                        currentX += stepX;
                        currentY += stepY;
                    }
                }
            }
        }
        field[x][y] = currentPlayer;
        changeCurrentPlayer();
    }

    /**
     * Оценочная функция для определения ценности хода текущего игрока.
     * @param x координата хода по x.
     * @param y координата хода по y.
     * @return ценность хода в типе double.
     */
    public double estimateFunction(int x, int y) {
        if (!isCorrectMove(x, y)) {
            return 0;
        }
        double estimate = 0;
        var occupationCells = getOccupationCells(x, y);
        for (var coordinate: occupationCells) {
            estimate += 1;
            if (coordinate.getX() == 0 || coordinate.getY() == 0) {
                estimate += 1;
            }
        }
        if (x == 0) {
            estimate += 0.4;
        }
        if (y == 0) {
            estimate += 0.4;
        }
        return estimate;
    }

    /**
     * Формирует список координат доступных ходов для текущего игрока.
     * @return список координат доступных ходов для текущего игрока.
     */
    public ArrayList<Coordinates> getAvailableMoves() {
        ArrayList<Coordinates> availableMoves = new ArrayList<Coordinates>();
        for (int i = 0; i < fieldSize; ++i) {
            for (int j = 0; j < fieldSize; ++j) {
                if (isCorrectMove(i, j)) {
                    availableMoves.add(new Coordinates(i, j));
                }
            }
        }
        return availableMoves;
    }

    /**
     * Печатает в консоль текущее состояние игрового поля.
     * @param showAvailableMoves требуется ли показывать на поле доступные ходы для текущего игрока.
     */
    public void print(boolean showAvailableMoves) {
        for (int i = 0; i < fieldSize; ++i) {
            System.out.print((fieldSize - i) + " || ");
            for (int j = 0; j < fieldSize; ++j) {
                String cell;
                if (field[i][j] == 0) {
                    if (showAvailableMoves && isCorrectMove(i, j)) {
                        cell = "?";
                    } else {
                        cell = " ";
                    }
                } else if (field[i][j] == 1) {
                    cell = "●";
                } else {
                    cell = "○";
                }

                System.out.print(cell + " | ");
            }
            if (i != fieldSize - 1) {
                System.out.println("\n   ---------------------------------");
            }
        }
        System.out.print("\n    ===============================\n     ");
        for (int i = 0; i < fieldSize; ++i) {
            System.out.print((char)('a' + i) + "   ");
        }
        System.out.println();
    }

    /**
     * Формирует пару результатов двух игроков по текущему состоянию игрового поля.
     * @return пара результатов двух игроков по текущему состоянию игрового поля.
     */
    public ScorePair getScore() {
        int firstPlayerScore = 0;
        int secondPlayerScore = 0;
        for (int i = 0; i < fieldSize; ++i) {
            for (int j = 0; j < fieldSize; ++j) {
                if (field[i][j] == 1) {
                    firstPlayerScore++;
                } else {
                    secondPlayerScore++;
                }
            }
        }
        return new ScorePair(firstPlayerScore, secondPlayerScore);
    }


    private boolean isCorrectMove(int x, int y) {
        boolean isCorrectMoveFlag = false;
        for (int stepX = -1; stepX <= 1; ++stepX) {
            for (int stepY = -1; stepY <= 1; ++stepY) {
                if (stepX == 0 && stepY == 0) {
                    continue;
                }
                isCorrectMoveFlag = isCorrectMoveFlag || checkMoveByDirection(x,y, stepX, stepY);
            }
        }
        return isCorrectMoveFlag;
    }

    private ArrayList<Coordinates> getOccupationCells(int x, int y) {
        ArrayList<Coordinates> occupationCells = new ArrayList<Coordinates>();
        for (int stepX = -1; stepX <= 1; ++stepX) {
            for (int stepY = -1; stepY <= 1; ++stepY) {
                if (stepX == 0 && stepY == 0) {
                    continue;
                }
                if (checkMoveByDirection(x,y, stepX, stepY)) {
                    int currentX = x;
                    int currentY = y;
                    while (isCorrectCoordinates(currentX + stepX, currentY + stepY)) {
                        if (field[currentX + stepX][currentY + stepY] == currentPlayer) {
                            break;
                        }
                        occupationCells.add(new Coordinates(currentX + stepX, currentY + stepY));
                        currentX += stepX;
                        currentY += stepY;
                    }
                }
            }
        }
        return occupationCells;
    }
    private boolean checkMoveByDirection(int x, int y, int stepX, int stepY) {
        if (!isCorrectCoordinates(x + stepX, y + stepY) || field[x][y] != 0 || field[x + stepX][y + stepY] == currentPlayer) {
            return false;
        }
        while (isCorrectCoordinates(x + stepX, y + stepY)) {
            if (field[x + stepX][y + stepY] == currentPlayer) {
                return true;
            }
            if (field[x + stepX][y + stepY] == 0) {
                return false;
            }
            x += stepX;
            y += stepY;
        }
        return false;
    }
    private boolean isCorrectCoordinates(int x, int y) {
        return 0 <= x && x < fieldSize && 0 <= y && y < fieldSize;
    }
    private int[][] field;
    private int fieldSize;
    private int currentPlayer;
}
