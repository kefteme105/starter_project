package ru.edu.pr02;

public class SelfCheck {
    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        total++; passed += check("типовое значение", () -> {
            int[] hours = TimesheetAnalyzer.parseHours("8;8;8;8;8");
            return TimesheetAnalyzer.total(hours) == 40
                    && TimesheetAnalyzer.average(hours) == 8.0
                    && TimesheetAnalyzer.weeklyOvertime(hours) == 5;
        });

        total++; passed += check("дни с переработкой", () ->
                TimesheetAnalyzer.overtimeDays(
                        TimesheetAnalyzer.parseHours("9;8;7;10;8")) == 2);

        total++; passed += check("нижняя граница", () ->
                TimesheetAnalyzer.total(
                        TimesheetAnalyzer.parseHours("0;0;0;0;0")) == 0);

        total++; passed += check("верхняя граница", () ->
                TimesheetAnalyzer.total(
                        TimesheetAnalyzer.parseHours("16;16;16;16;16")) == 80);

        total++; passed += expectError("значение 17", "позиции 3", () ->
                TimesheetAnalyzer.parseHours("8;8;17;8;8"));

        total++; passed += expectError("неверный формат", "позиции 2", () ->
                TimesheetAnalyzer.parseHours("8;abc;8;8;8"));

        total++; passed += expectError("четыре значения", "ровно 5", () ->
                TimesheetAnalyzer.parseHours("8;8;8;8"));

        total++; passed += expectError("пустая строка", "не должна быть пустой", () ->
                TimesheetAnalyzer.parseHours(""));

        total++; passed += expectError("пустой элемент", "позиции 2", () ->
                TimesheetAnalyzer.parseHours("8;;8;8;8"));

        System.out.printf("Итог: %d/%d проверок пройдено%n", passed, total);
        if (passed != total) {
            System.exit(1);
        }
    }

    private static int check(String name, Check action) {
        try {
            boolean ok = action.run();
            System.out.println((ok ? "PASS " : "FAIL ") + name);
            return ok ? 1 : 0;
        } catch (Exception exception) {
            System.out.println("FAIL " + name + ": " + exception.getMessage());
            return 0;
        }
    }

    private static int expectError(String name, String messagePart, Action action) {
        try {
            action.run();
            System.out.println("FAIL " + name + ": исключение не возникло");
            return 0;
        } catch (IllegalArgumentException exception) {
            boolean ok = exception.getMessage().contains(messagePart);
            System.out.println((ok ? "PASS " : "FAIL ") + name + ": " + exception.getMessage());
            return ok ? 1 : 0;
        }
    }

    @FunctionalInterface
    private interface Check {
        boolean run();
    }

    @FunctionalInterface
    private interface Action {
        void run();
    }
}
