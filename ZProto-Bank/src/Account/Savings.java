package Account;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import Interface.IAccountBehaviour;
import Utilities.SystemRelated;
import Utilities.Template;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Savings implements IAccountBehaviour {
    private String name;

    private Date lastLoggedIn;

    private float transactionMade;
    private float totalValue;

    private ArrayList<Transaction> transactions;

    public Savings(Date transactionDate) {
        this.transactionMade = 0;
        this.lastLoggedIn = transactionDate;
        this.totalValue = 0;
        this.transactions = new ArrayList<>();

        setName();

        System.out.print("Initial Deposit? [Minimum Rp. 300.000,00] -> ");
        float initialValue = new SystemRelated().chooseNumberFloat(300000f, 2000000000f);

        addTransactions(transactionDate, initialValue, "Initial Deposit");

        System.out.println(new Template().ANSI_GREEN + "Successfully Created!" + new Template().ANSI_RESET);
        new Scanner(System.in).nextLine();
    }

    public String getName() {
        return name;
    }

    public void addTransactions(Date transactionDate, float transactionValue) {
        lastLoggedIn = transactionDate;
        TransactionType transactionType = (transactionValue < 0) ? TransactionType.Debit : TransactionType.Credit;
        Transaction transaction = new Transaction(transactionDate, transactionType, transactionValue, "");

        if (getTransactionsSize() != 0) {
            LocalDate dateCurrent = transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            LocalDate dateStart = this.transactions.get(getTransactionsSize() - 1).getTransactionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate initNextMonth = LocalDate.of(dateStart.getYear() + dateStart.getMonthValue() / 12, dateStart.getMonthValue() % 12 + 1, 1);

            while (initNextMonth.isBefore(dateCurrent) || initNextMonth.equals(dateCurrent)) {
                Date currDate = new DateCustom().localDateToDate(initNextMonth);
                this.transactions.add(new Transaction(currDate, TransactionType.Credit, currInterestValue(), "Interest Addition"));
                this.totalValue += currInterestValue();

                initNextMonth = initNextMonth.plusMonths(1);
            }
        }

        if (transactionValue != 0) {
            this.transactions.add(transaction);
            this.totalValue += transactionValue;
        }
    }

    public void addTransactions(Date transactionDate, float transactionValue, String tmpDesc) {
        lastLoggedIn = transactionDate;
        TransactionType transactionType = (transactionValue < 0) ? TransactionType.Debit : TransactionType.Credit;
        Transaction transaction = new Transaction(transactionDate, transactionType, transactionValue, tmpDesc);

        if (getTransactionsSize() != 0) {
            LocalDate dateCurrent = transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            LocalDate dateStart = this.transactions.get(getTransactionsSize() - 1).getTransactionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate initNextMonth = LocalDate.of(dateStart.getYear()+ dateStart.getMonthValue() / 12, dateStart.getMonthValue() % 12 + 1, 1);

            while (initNextMonth.isBefore(dateCurrent) || initNextMonth.equals(dateCurrent)) {
                Date currDate = new DateCustom().localDateToDate(initNextMonth);
                this.transactions.add(new Transaction(currDate, TransactionType.Credit, currInterestValue(), "Interest Addition"));
                this.totalValue += currInterestValue();

                initNextMonth = initNextMonth.plusMonths(1);
            }
        }

        if (transactionValue != 0) {
            this.transactions.add(transaction);
            this.totalValue += transactionValue;
        }
    }

    public boolean cashMoney(Date transactionDate, float transactionValue) {
        if (lastLoggedIn.equals(transactionDate)) {
            if (transactionMade + transactionValue > 10000000f) {
                System.out.println(new Template().ANSI_RED + "Limit Rp. 10.000.000,00 Reached!" + new Template().ANSI_RESET);
                return false;
            } else if (transactionValue > totalValue) {
                System.out.println(new Template().ANSI_RED + "Insufficient Balance!" + new Template().ANSI_RESET);
                return false;
            } else
                transactionMade += transactionValue;
        } else if (transactionValue <= 10000000f)
            transactionMade = transactionValue;
        else {
            System.out.println(new Template().ANSI_RED + "Limit Rp. 10.000.000,00 Reached!" + new Template().ANSI_RESET);
            return false;
        }
        lastLoggedIn = transactionDate;
        addTransactions(transactionDate, -1 * transactionValue, "Money Withdraw by User");


        return true;
    }

    public int getTransactionsSize() {
        return transactions.size();
    }

    public Transaction getTransactionAt(int index) {
        return transactions.get(index);
    }

    public void printBalance() {
        DecimalFormat indonesianC = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setGroupingSeparator('.');

        indonesianC.setDecimalFormatSymbols(formatRp);
        System.out.println(new Template().ANSI_GREEN + "Current Balance : " + indonesianC.format(Math.abs(totalValue)) + new Template().ANSI_RESET);
    }

    public Date getLastLoginDate() {
        return this.lastLoggedIn;
    }

    public float currInterestValue() {
        float interest = 0.025f;
        return (totalValue * (interest / (float) 12));
    }

    private void setName() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Your Savings Name? [At Least 3 Characters] -> ");
        String tmpName = scanner.nextLine();
        if (!Pattern.matches("[A-Z][a-z]{2,}", tmpName)) {
            System.out.println(new Template().ANSI_RED + "Invalid!" + new Template().ANSI_RESET);
            setName();
            return;
        }

        this.name = tmpName;
    }
}
