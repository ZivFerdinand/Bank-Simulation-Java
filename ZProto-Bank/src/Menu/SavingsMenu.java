package Menu;

import Account.Savings;
import Account.Transaction;
import Utilities.SystemRelated;
import Utilities.Template;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class SavingsMenu {
    ArrayList<Savings> savingsArrayList;

    public SavingsMenu(ArrayList<Savings> currSavings, String name) {
        this.savingsArrayList = currSavings;
        mainMenu(name);
    }

    private void viewSavingsList() {
        for (int i = 0; i < savingsArrayList.size(); i++) {
            System.out.printf("[%d.] %s\n", i + 1, savingsArrayList.get(i).getName());
        }
    }

    private void mainMenu(String name) {
        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_PURPLE + "[Hi! " + name + "]" + new Template().ANSI_RESET);
        System.out.println(new Template().ANSI_PURPLE + "[SAVINGS LIST]" + new Template().ANSI_RESET);

        int savingsSize = savingsArrayList.size();
        viewSavingsList();
        System.out.println((savingsSize + 1) + ". Create New Account\n" + new Template().ANSI_RED + "[0. BACK]" + new Template().ANSI_RESET);

        int choice = (new SystemRelated().chooseMenuInput(0, savingsSize + 1));
        if (choice == 0)
            return;
        else if (choice == savingsSize + 1) {
            savingsArrayList.add(createNewAccount());
        } else
            currSavingsMainMenu(savingsArrayList.get(choice - 1));

        mainMenu(name);
    }

    private Savings createNewAccount() {
        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_PURPLE + "[CREATE NEW SAVINGS ACCOUNT]" + new Template().ANSI_RESET);
        Date getDate = inputDate();
        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_PURPLE + "[CREATE NEW SAVINGS ACCOUNT]" + new Template().ANSI_RESET);

        return new Savings(getDate);
    }

    private void currSavingsMainMenu(Savings currSavings) {
        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_PURPLE + "[" + currSavings.getName() + " SAVINGS]" + new Template().ANSI_RESET);
        System.out.println("1. Add Money\n2. Cash Money\n3. View Transaction Details\n4. Delete This Account\n" + new Template().ANSI_RED + "[0. BACK]" + new Template().ANSI_RESET);
        switch (new SystemRelated().chooseMenuInput(0, 4)) {
            case 1 -> addMoney(currSavings);
            case 2 -> cashMoney(currSavings);
            case 3 -> viewDetails(currSavings);
            case 4 -> deleteSavings(currSavings);
        }
    }

    private void deleteSavings(Savings currSavings) {
        new SystemRelated().SystemCls();
        savingsArrayList.remove(currSavings);
        System.out.println(new Template().ANSI_GREEN + "Successfully Deleted!\nBalance Discarded!" + new Template().ANSI_RESET);

        new Scanner(System.in).nextLine();
    }

    private void viewDetails(Savings currSavings) {
        new SystemRelated().SystemCls();
        Date getDate = inputDate(currSavings);
        new SystemRelated().SystemCls();
        currSavings.addTransactions(getDate, 0);

        System.out.println(new Template().ANSI_PURPLE + "[TRANSACTION SUMMARY]" + new Template().ANSI_RESET);

        for (int i = 0; i < currSavings.getTransactionsSize(); i++) {
            Transaction currTransactionAt = currSavings.getTransactionAt(i);
            System.out.println("[" + DateFormat.getDateInstance().format(currTransactionAt.getTransactionDate()) + "]");
            System.out.printf("%s || %s (%s)\n\n", currTransactionAt.getTransactionType(), new SystemRelated().printRp(currTransactionAt.getValue()), currTransactionAt.getDesc());
        }
        currSavings.printBalance();

        new Scanner(System.in).nextLine();
    }

    private void addMoney(Savings currSavings) {
        new SystemRelated().SystemCls();
        Date getDate = inputDate(currSavings);

        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_PURPLE + "[ADD MONEY]" + new Template().ANSI_RESET);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Money To Add? [Not Zero | Max 2.000.000.000,00] -> ");
        float scannedValue = new SystemRelated().chooseNumberFloat(0, 2000000000);

        currSavings.addTransactions(getDate, scannedValue, "User Money Deposit");
        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_GREEN + "Successfully Added!" + new Template().ANSI_RESET);
        currSavings.printBalance();
        scanner.nextLine();
    }

    private void cashMoney(Savings currSavings) {
        new SystemRelated().SystemCls();
        Date getDate = inputDate(currSavings);

        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_PURPLE + "[CASH MONEY]" + new Template().ANSI_RESET);
        currSavings.addTransactions(getDate, 0);
        currSavings.printBalance();

        System.out.print("Money To Cash? [Not Zero | Max 2.000.000.000,00] -> ");
        float scannedValue = new SystemRelated().chooseNumberFloat(0, 2000000000);

        new SystemRelated().SystemCls();
        if (currSavings.cashMoney(getDate, scannedValue)) {
            System.out.println(new Template().ANSI_GREEN + "Successfully Cashed Out!" + new Template().ANSI_RESET);
        }
        currSavings.printBalance();
        new Scanner(System.in).nextLine();
    }

    private Date inputDate(Savings currSavings) {
        Scanner scanner = new Scanner(System.in);
        try {
            if (currSavings.getLastLoginDate() != null) {
                System.out.println("Last Logged In at " + DateFormat.getDateInstance().format(currSavings.getLastLoginDate()));
            }
            System.out.print("Current Date? [dd/MM/yyyy] -> ");
            Date getDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());

            if (getDate.before(currSavings.getLastLoginDate())) {
                throw new Exception("Can't Log In to Previous Date");
            }
            return getDate;
        } catch (Exception e) {
            System.out.println(new Template().ANSI_RED + e.getMessage() + new Template().ANSI_RESET);
            return inputDate(currSavings);
        }
    }

    private Date inputDate() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Current Date? [dd/MM/yyyy] -> ");

            return new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println(new Template().ANSI_RED + e.getMessage() + new Template().ANSI_RESET);
            return inputDate();
        }
    }
}
