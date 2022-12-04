import java.text.ParseException;
import java.util.Scanner;

/**
 * Класс, реализующий логику игры "Реверси".
 */
public class Game {

    /**
     * Метод запускает и реализовывает консольную игру "Реверси".
     */
    public void start() {
        while (true) {
            System.out.println("\nВы в главном меню.");
            System.out.println("Введите /n для начала новой игры.");
            System.out.println("        /b чтобы узнать лучший результат за сессию.");
            System.out.print("        /e чтобы завершить сессию.\n>");
            String command = in.nextLine();
            switch (command) {
                case Commands.newGame:
                    runGame();
                    break;
                case Commands.bestResult:
                    printBestResult();
                    break;
                case Commands.exit:
                    return;
                default:
                    System.out.println("Неверный формат, повторите попытку!");
            }
        }
    }

    private static class Commands {
        public static final String newGame = "/n";
        public static final String bestResult = "/b";
        public static final String exit = "/e";
        public static final String playerVsPlayer = "/p";
        public static final String computerForBlack = "cw";
        public static final String computerForWhite = "/cb";
    }
    private void handleConsole() {
        while (true) {
            System.out.print("Введите координаты, куда поставить фишку или команду /e, чтобы завершить игру\n>");
            String command = in.nextLine();
            if (command.isEmpty()) {
                System.out.println("Неверный формат, повторите попытку!");
                continue;
            }
            try {
                if (command.equals(Commands.exit)) {
                    toFinishGameFlag = true;
                } else {
                    tryDoMove(command);
                }
                return;
            } catch (ParseException ex) {
                System.out.println("Неверный формат, повторите попытку!");
            } catch (IndexOutOfBoundsException ex) {
                System.out.println("Невозможно сделать ход, повторите попытку!");
            }
        }
    }

    private void tryDoMove(String coordinates) throws ParseException {
        if (coordinates.length() != 2) {
            throw new ParseException("", 0);
        }
        char letter = coordinates.charAt(0);
        char number = coordinates.charAt(1);
        if ('a' > letter || letter > 'h' || number < '1' || number > '8') {
            throw new ParseException("", 0);
        }
        var x = fieldSize - (number - '1') - 1;
        var y = (letter - 'a');
        field.doMove(x, y);
    }

    private void setGameType() {
        while (true) {
            System.out.println("Введите /cw для начала игры с компьютером за ●");
            System.out.println("        /cb для начала игры с компьютером за ○");
            System.out.println("        /p для начала игры в режиме игрок против игрока");
            System.out.print(">");
            gameType = in.nextLine();
            if (gameType.equals(Commands.playerVsPlayer) || gameType.equals(Commands.computerForWhite) || gameType.equals(Commands.computerForBlack)) {
                return;
            }
            System.out.println("Неверный формат, повторите попытку!");
        }
    }

    private String coordinatesToString(Coordinates coordinates) {
        return String.valueOf((char)('a' + coordinates.getY())) + String.valueOf((char) ('0' + (fieldSize - coordinates.getX())));
    }
    private void makeComputerMove() {
        Coordinates move = new Coordinates(0, 0);
        double maxEstimate = 0;
        for (int i = 0; i < fieldSize; ++i) {
            for (int j = 0; j < fieldSize; ++j) {
                double currentEstimate = field.estimateFunction(i, j);
                if (currentEstimate > maxEstimate) {
                    maxEstimate = currentEstimate;
                    move = new Coordinates(i, j);
                }
            }
        }
        field.doMove(move.getX(), move.getY());
        System.out.println("Компьютер поставил фишку на " + coordinatesToString(move));
    }

    private void printWhoMove() {
        String playerColor = field.getCurrentPlayer() == 1 ? "(●)" : "(○)";
        System.out.println("Ходят " + playerColor);
    }
    private void printScore() {
        ScorePair scorePair = field.getScore();

        System.out.println("Больше невозможно совершать ходы, игра окончена!");
        System.out.println("Результаты игры:");
        System.out.println("Игрок за (●): " + scorePair.getFirstPlayerScore());
        System.out.println("Игрок за (○): " + scorePair.getSecondPlayerScore() + "\n");

        if (scorePair.getFirstPlayerScore() > scorePair.getSecondPlayerScore()) {
            System.out.println("(●) выиграли!");
        } else if (scorePair.getFirstPlayerScore() < scorePair.getSecondPlayerScore()) {
            System.out.println("(○) выиграли!");
        } else {
            System.out.println("Ничья!");
        }

        maxScore = Math.max(maxScore, Math.max(scorePair.getFirstPlayerScore(), scorePair.getSecondPlayerScore()));
    }

    private boolean isGameOver() {
        if (!field.hasEmpyCells()) {
            return true;
        }
        if (field.isEnableNextMove()) {
            return false;
        }
        field.changeCurrentPlayer();
        if (field.isEnableNextMove()) {
            field.changeCurrentPlayer();
            return false;
        }
        return true;
    }

    private boolean isComputerMove() {
        if (gameType.equals(Commands.playerVsPlayer)) {
            return false;
        }
        if (gameType.equals(Commands.computerForWhite)) {
            return field.getCurrentPlayer() == 1;
        } else {
            return field.getCurrentPlayer() == 2;
        }
    }

    private void printAvailableMoves() {
        System.out.println("Доступные ходы:");
        var availableMoves = field.getAvailableMoves();
        System.out.println("----------------------------------------------");
        for (var move : availableMoves) {
            System.out.print(coordinatesToString(move) + " ");
        }
        System.out.println("\n----------------------------------------------\n");
    }
    private void runGame() {
        field = new Field(fieldSize);
        setGameType();
        boolean showAvailableMoves = !isComputerMove();
        field.print(showAvailableMoves);
        while (true) {
            if (isGameOver()) {
                break;
            }
            printWhoMove();
            if (!field.isEnableNextMove()) {
                System.out.println("Невозможно сделать ход, право хода переходит следующему.");
                field.changeCurrentPlayer();
                continue;
            }

            if (isComputerMove()) {
                makeComputerMove();
                showAvailableMoves = true;
            } else {
                printAvailableMoves();
                handleConsole();
                if (toFinishGameFlag) {
                    break;
                }
                if (!gameType.equals(Commands.playerVsPlayer)) {
                    showAvailableMoves = false;
                }
            }
            field.print(showAvailableMoves);
        }
        if (!toFinishGameFlag) {
            printScore();
        }
        toFinishGameFlag = false;
    }
    private void printBestResult() {
        System.out.println("Наибольшее количество фишек за текующую сессию: " + maxScore + "\n");
    }
    private static Scanner in = new Scanner(System.in);
    private static final int fieldSize = 8;
    private static int maxScore = 0;
    private boolean toFinishGameFlag = false;
    private String gameType;
    private Field field;
}
