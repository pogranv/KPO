public class Student {
    private String name;
    private String surname;
    private String patronymic;
    private boolean wasAsked;
    private boolean isAttend;
    private int mark;

    public Student(String surname, String name, String patronymic) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.wasAsked = false;
        this.isAttend = false;
    }

    public void setIsAttend(boolean isAttend) {
        this.isAttend = isAttend;
    }

    public boolean getIsAttend() {
        return this.isAttend;
    }

    public boolean getWasAsked() {
        return this.wasAsked;
    }

    public void setWasAsked(boolean wasAsked) {
        this.wasAsked = wasAsked;
    }

    public int getMark() {
        return this.mark;
    }

    public void setMark(int mark) {
        if (mark < 0 || mark > 10) {
            throw new NumberFormatException();
        }
        this.mark = mark;
    }

    @Override
    public String toString() {
        return surname + " " + name + " " + patronymic;
    }
}
