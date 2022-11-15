import java.util.Scanner;

public class StudentsGroup {
    private Student[] studentsList;
    private int countStudents;
    private int countAskedStudents;

    public StudentsGroup() {
        studentsList = new Student[] {
                new Student("Ivanov", "Ivan", "Ivanovich"),
                new Student ("Andreev", "Andrei", "Andreevich"),
                new Student("Petrov", "Petr", "Petrovich"),
                new Student("Igorev", "Igor", "Igorevich"),
                new Student("Fedorov", "Fedor", "Fedorovich"),
                new Student("Rafov", "Rafael", "Rafaelevich"),
                new Student("Sidorov", "Sidor", "Sidorovich"),
                new Student("Alexandrov", "Alexandr", "Alexandrovich"),
                new Student("Petrov", "Petr", "Petrovich"),
                new Student("Nikitov", "Nikita", "Nikitovich")
        };
        this.countStudents = studentsList.length;
        this.countAskedStudents = 0;
    }

    public void printStudentsList() {
        for (var student : studentsList) {
            System.out.println(student);
        }
    }

    public void printAskedStudents() {
        for (var student : studentsList) {
            if (student.getIsAttend()) {
                System.out.println(student + " | Оценка: " + student.getMark());
            }
        }
    }

    public void chooseRandomStudent() {
        if (countStudents == countAskedStudents) {
            System.out.println("Все студенты уже ответили!");
            return;
        }
        ++countAskedStudents;

        int studentNumber = getRamdomNumber();

        System.out.println("Отвечает " + studentsList[studentNumber]);
        System.out.print("Присутствует ли на паре? (y/n)\n>");
        Scanner in = new Scanner(System.in);
        while (true) {
            String command = in.nextLine();
            if (command.equals("y")) {
                studentsList[studentNumber].setWasAsked(true);
                studentsList[studentNumber].setIsAttend(true);
                getMark(studentsList[studentNumber]);
                break;
            } else if (command.equals("n")) {
                studentsList[studentNumber].setMark(0);
                studentsList[studentNumber].setWasAsked(true);
                break;
            } else {
                System.out.println("Неверная команда, повторите ввод!");
            }
        }
    }

    private int getRamdomNumber() {
        while (true) {
            int studentNumber = (int) (Math.random() * countStudents);
            if (!studentsList[studentNumber].getWasAsked()) {
                return studentNumber;
            }
        }
    }

    private void getMark(Student student) {
        while (true) {
            System.out.print("Введите оценку студента: [0;10] \n>");
            Scanner in = new Scanner(System.in);
            int mark = in.nextInt();
            try {
                student.setMark(mark);
                return;
            } catch (NumberFormatException ex) {
                System.out.println("Некорректная оценка, повторите попытку!");
            }
        }
    }
}
