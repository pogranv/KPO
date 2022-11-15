import java.util.Scanner;

public class Main {

    public static void printHelp() {
        System.out.println("Команды:");
        System.out.println("h - описание команд в программе");
        System.out.println("e - выход из программы");
        System.out.println("l - вывести список всех студентов группы");
        System.out.println("r - выбор случайного студента");
        System.out.println("a - вывести список отвечавших с оценками");
    }

    public static void main(String[] args) {

        printHelp();

        Scanner in = new Scanner(System.in);
        StudentsGroup group = new StudentsGroup();

        while (true) {
            System.out.print(">");
            String command = in.nextLine();
            switch (command) {
                case "r":
                    group.chooseRandomStudent();
                    break;
                case "a":
                    group.printAskedStudents();
                    break;
                case "l":
                    group.printStudentsList();
                    break;
                case "h":
                    printHelp();
                    break;
                case "e":
                    return;
                default:
                    System.out.println("Некорректная команда, повторите попытку!");
            }
        }
    }
}