package Utilities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SystemRelated {
    public void SystemCls() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    public int chooseMonth() {
        Scanner scanner = new Scanner(System.in);
        try {
            int choice = scanner.nextInt();
            if (choice != 3 && choice != 6 && choice != 12) {
                throw new IllegalArgumentException("No Choice Exists!");
            }

            return choice;
        } catch (InputMismatchException | IllegalArgumentException e) {
            if (e.getMessage() == null) {
                System.out.println(new Template().ANSI_RED + "Invalid Format!" + new Template().ANSI_RESET);
            } else
                System.out.println(new Template().ANSI_RED + e.getMessage() + new Template().ANSI_RESET);

            scanner.nextLine();
            return chooseMonth();
        }
    }

    public float chooseNumberFloat(float min, float max) {
        Scanner scanner = new Scanner(System.in);
        try {
            float choice = scanner.nextFloat();
            if (choice < min || choice > max) {
                throw new IllegalArgumentException("No Choice Exists!");
            }

            return choice;
        } catch (InputMismatchException | IllegalArgumentException e) {
            if (e.getMessage() == null) {
                System.out.println(new Template().ANSI_RED + "Invalid Format!" + new Template().ANSI_RESET);
            } else
                System.out.println(new Template().ANSI_RED + e.getMessage() + new Template().ANSI_RESET);

            scanner.nextLine();
            return chooseNumberFloat(min, max);
        }
    }

    public String printRp(float value) {
        DecimalFormat indonesianC = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setGroupingSeparator('.');

        indonesianC.setDecimalFormatSymbols(formatRp);

        return indonesianC.format(value);

    }

    public int chooseMenuInput(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.printf("Choose Menu Number? [%d-%d] -> ", min, max);
            int choice = scanner.nextInt();
            if (choice < min || choice > max) {
                throw new IllegalArgumentException("No Choice Exists!");
            }

            return choice;
        } catch (InputMismatchException | IllegalArgumentException e) {
            if (e.getMessage() == null) {
                System.out.println(new Template().ANSI_RED + "Invalid Format!" + new Template().ANSI_RESET);
            } else
                System.out.println(new Template().ANSI_RED + e.getMessage() + new Template().ANSI_RESET);

            scanner.nextLine();
            return chooseMenuInput(min, max);
        }
    }
}
