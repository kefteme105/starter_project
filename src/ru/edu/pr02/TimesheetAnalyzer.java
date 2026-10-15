package ru.edu.pr02;

public class TimesheetAnalyzer {
    public static final int WORK_DAYS = 5;
    public static final int MIN_HOURS = 0;
    public static final int MAX_HOURS = 16;
    public static final int DAILY_NORM = 8;
    // Вариант 6: недельная норма изменена с 40 до 35 часов.
    public static final int WEEKLY_NORM = 35;

    private TimesheetAnalyzer() {
    }

    public static int[] parseHours(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Строка табеля не должна быть пустой");
        }

        String[] values = line.split(";", -1);
        if (values.length != WORK_DAYS) {
            throw new IllegalArgumentException(
                    "Ожидалось ровно " + WORK_DAYS + " значений, получено: " + values.length);
        }

        int[] hours = new int[WORK_DAYS];
        for (int i = 0; i < values.length; i++) {
            String value = values[i].trim();
            if (value.isEmpty()) {
                throw new IllegalArgumentException("Пустое значение в позиции " + (i + 1));
            }

            try {
                hours[i] = Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException(
                        "Значение в позиции " + (i + 1) + " не является целым числом: " + value,
                        exception);
            }

            if (hours[i] < MIN_HOURS || hours[i] > MAX_HOURS) {
                throw new IllegalArgumentException(
                        "Значение в позиции " + (i + 1) + " должно быть от "
                                + MIN_HOURS + " до " + MAX_HOURS + ": " + hours[i]);
            }
        }
        return hours;
    }

    public static int total(int[] hours) {
        requireHours(hours);
        int sum = 0;
        for (int hour : hours) {
            sum += hour;
        }
        return sum;
    }

    public static double average(int[] hours) {
        return (double) total(hours) / hours.length;
    }

    public static int overtimeDays(int[] hours) {
        requireHours(hours);
        int count = 0;
        for (int hour : hours) {
            if (hour > DAILY_NORM) {
                count++;
            }
        }
        return count;
    }

    public static int weeklyOvertime(int[] hours) {
        return Math.max(0, total(hours) - WEEKLY_NORM);
    }

    public static String report(String line) {
        int[] hours = parseHours(line);
        return String.format(
                "Сумма: %d ч; среднее: %.1f ч; дней с переработкой: %d; недельная переработка: %d ч",
                total(hours), average(hours), overtimeDays(hours), weeklyOvertime(hours));
    }

    private static void requireHours(int[] hours) {
        if (hours == null || hours.length != WORK_DAYS) {
            throw new IllegalArgumentException("Массив должен содержать ровно " + WORK_DAYS + " значений");
        }
        for (int i = 0; i < hours.length; i++) {
            if (hours[i] < MIN_HOURS || hours[i] > MAX_HOURS) {
                throw new IllegalArgumentException(
                        "Значение в позиции " + (i + 1) + " должно быть от "
                                + MIN_HOURS + " до " + MAX_HOURS + ": " + hours[i]);
            }
        }
    }
}
