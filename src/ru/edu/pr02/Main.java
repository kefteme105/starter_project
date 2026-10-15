package ru.edu.pr02;

public class Main {
    public static void main(String[] args) {
        String line = args.length == 0 ? "8;9;7;10;8" : args[0];
        try {
            System.out.println(TimesheetAnalyzer.report(line));
        } catch (IllegalArgumentException exception) {
            System.out.println("Ошибка: " + exception.getMessage());
        }
    }
}
