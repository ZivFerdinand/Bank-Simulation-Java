package Menu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import Account.Deposit;
import Account.Transaction;
import Utilities.SystemRelated;
import Utilities.Template;

public class DepositMenu {
    ArrayList<Deposit> depositArrayList;

    public DepositMenu(ArrayList<Deposit> currDeposits, String name) {
        this.depositArrayList = currDeposits;
        mainMenu(name);
    }

    private void viewDepositList() {
        for (int i = 0; i < depositArrayList.size(); i++) {
            System.out.printf("[%d.] %s - %d Months\n", i + 1, depositArrayList.get(i).getName(), depositArrayList.get(i).getMonth());
        }
    }

    private void mainMenu(String name) {
        new SystemRelated().SystemCls();

        System.out.println(new Template().ANSI_PURPLE + "[Hi! " + name + "]" + new Template().ANSI_RESET);
        System.out.println(new Template().ANSI_PURPLE + "[DEPOSIT LIST]" + new Template().ANSI_RESET);

        int depositSize = depositArrayList.size();
        viewDepositList();
        System.out.println((depositSize + 1) + ". Create New Account\n" + new Template().ANSI_RED + "[0. BACK]" + new Template().ANSI_RESET);

        int choice = (new SystemRelated().chooseMenuInput(0, depositSize + 1));
        if (choice == 0)
            return;
        else if (choice == depositSize + 1) {
            depositArrayList.add(createNewAccount());
        } else
            currDepositMainMenu(depositArrayList.get(choice - 1));

        mainMenu(name);
    }

    private void currDepositMainMenu(Deposit currDeposit) {
        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_PURPLE + "[" + currDeposit.getName() + " DEPOSIT]" + new Template().ANSI_RESET);
        System.out.println("1. Disburse Deposit\n2. View Transaction Details\n3. Delete This Account\n" + new Template().ANSI_RED + "[0. BACK]" + new Template().ANSI_RESET);
        switch (new SystemRelated().chooseMenuInput(0, 4)) {
            case 1 -> disburseDeposit(currDeposit);
            case 2 -> viewDetails(currDeposit);
            case 3 -> deleteDeposit(currDeposit);
        }
    }

    private void deleteDeposit(Deposit currDeposit) {
        new SystemRelated().SystemCls();
        depositArrayList.remove(currDeposit);
        System.out.println(new Template().ANSI_GREEN + "Successfully Deleted!\nBalance Discarded!" + new Template().ANSI_RESET);

        new Scanner(System.in).nextLine();
    }

    private void disburseDeposit(Deposit currDeposit) {
        new SystemRelated().SystemCls();
        if (currDeposit.getDisbursementStatus()) {
            System.out.println(new Template().ANSI_GREEN + "You've Disbursed This Account!\nCheck Transaction Details!" + new Template().ANSI_RESET);
            new Scanner(System.in).nextLine();
            return;
        }
        Date getDate = inputDate(currDeposit);
        new SystemRelated().SystemCls();

        System.out.println(new Template().ANSI_PURPLE + "[DISBURSE DEPOSIT]" + new Template().ANSI_RESET);

        currDeposit.cashMoney(getDate);
        currDeposit.printBalance();
        System.out.println(new Template().ANSI_GREEN + "Successfully Disbursed!" + new Template().ANSI_RESET);

        new Scanner(System.in).nextLine();
    }

    private void disburseDeposit(Deposit currDeposit, Date getDate) {
        currDeposit.cashMoney(getDate);
        currDeposit.printBalance();
        System.out.println(new Template().ANSI_GREEN + "Successfully Disbursed!" + new Template().ANSI_RESET);
    }

    private void viewDetails(Deposit currDeposit) {
        new SystemRelated().SystemCls();

        if (!currDeposit.getDisbursementStatus()) {
            Date getDate = inputDate(currDeposit);
            new SystemRelated().SystemCls();

            if (currDeposit.checkDisburseAbility(getDate)) {
                disburseDeposit(currDeposit, getDate);
                System.out.println(new Template().ANSI_GREEN + "Auto-Disbursement is Automatically Performed!" + new Template().ANSI_RESET);
                new Scanner(System.in).nextLine();

                viewDetails(currDeposit);
                return;
            }
            currDeposit.addTransactions(getDate, 0);
        }

        System.out.println(new Template().ANSI_PURPLE + "[TRANSACTION SUMMARY]" + new Template().ANSI_RESET);

        for (int i = 0; i < currDeposit.getTransactionsSize(); i++) {
            Transaction currTransactionAt = currDeposit.getTransactionAt(i);
            System.out.println("[" + DateFormat.getDateInstance().format(currTransactionAt.getTransactionDate()) + "]");
            System.out.printf("%s || %s (%s)\n\n", currTransactionAt.getTransactionType(), new SystemRelated().printRp(currTransactionAt.getValue()), currTransactionAt.getDesc());
        }
        currDeposit.printBalance();

        new Scanner(System.in).nextLine();
    }

    private Deposit createNewAccount() {
        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_PURPLE + "[CREATE NEW DEPOSIT ACCOUNT]" + new Template().ANSI_RESET);
        Date getDate = inputDate();
        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_PURPLE + "[CREATE NEW DEPOSIT ACCOUNT]" + new Template().ANSI_RESET);

        return new Deposit(getDate);
    }

    private Date inputDate(Deposit currDeposit) {
        Scanner scanner = new Scanner(System.in);
        try {
            if (currDeposit.getLastLoginDate() != null) {
                System.out.println("Account Created at " + DateFormat.getDateInstance().format(currDeposit.getInitialCreationDate()));

                System.out.println("Last Logged In at " + DateFormat.getDateInstance().format(currDeposit.getLastLoginDate()));
            }
            System.out.print("Current Date? [dd/MM/yyyy] -> ");
            Date getDate = new SimpleDateFormat("dd/MM/yyyy").parse(scanner.nextLine());

            if (getDate.before(currDeposit.getLastLoginDate())) {
                throw new Exception("Can't Log In to Previous Date");
            }
            return getDate;
        } catch (Exception e) {
            System.out.println(new Template().ANSI_RED + e.getMessage() + new Template().ANSI_RESET);
            return inputDate(currDeposit);
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
