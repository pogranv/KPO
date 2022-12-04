/**
 * Класс, позволяющий хранить пару результатов двух игроков.
 */
public class ScorePair {
    /**
     * Конструктор, формирующий пару результатов двух игроков.
     * @param firstPlayerScore количество очков первого игрока.
     * @param secondPlayerScore количество очков второго игрока.
     */
    public ScorePair(int firstPlayerScore, int secondPlayerScore) {
        this.firstPlayerScore = firstPlayerScore;
        this.secondPlayerScore = secondPlayerScore;
    }

    /**
     * Возвращает количество очков первого игрока.
     * @return количество очков первого игрока.
     */
    public int getFirstPlayerScore() {
        return firstPlayerScore;
    }

    /**
     * Возвращает количество очков первого игрока.
     * @return количество очков первого игрока.
     */
    public int getSecondPlayerScore() {
        return secondPlayerScore;
    }
    private int firstPlayerScore;
    private int secondPlayerScore;
}
