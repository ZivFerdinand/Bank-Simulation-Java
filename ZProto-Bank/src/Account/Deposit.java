package Account;

import Interface.IAccountBehaviour;
import Utilities.SystemRelated;
import Utilities.Template;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Deposit implements IAccountBehaviour {
    private String name;

    private final int monthPeriod;

    private boolean disbursed;

    private final float interest;
    private float totalValue;
    private float finalValue;

    private final Date transactionDate;
    private Date lastLoggedIn;

    private ArrayList<Transaction> transactions;

    public Date getInitialCreationDate() {
        return transactionDate;
    }

    public Deposit(Date transactionDate) {
        this.disbursed = false;
        this.lastLoggedIn = transactionDate;
        this.transactionDate = transactionDate;
        this.totalValue = 0;

        setName();
        System.out.print("Amount to Deposit? [Minimum Rp. 1.000.000,00] -> ");
        float initialValue = new SystemRelated().chooseNumberFloat(1000000f, 2000000000f);

        System.out.print("Length to Deposit? [3 / 6 / 12 Months] -> ");
        int monthPeriod = new SystemRelated().chooseMonth();

        this.monthPeriod = monthPeriod;

        switch (monthPeriod) {
            case 6 -> this.interest = 0.0325f;
            case 12 -> this.interest = 0.035f;
            default -> this.interest = 0.03f;
        }

        this.transactions = new ArrayList<>();
        addTransactions(transactionDate, initialValue, "Initial Deposit");

        System.out.println(new Template().ANSI_GREEN + "Successfully Created!" + new Template().ANSI_RESET);
        new Scanner(System.in).nextLine();
    }

    public String getName() {
        return name;
    }

    public int getMonth() {
        return monthPeriod;
    }

    public Date getLastLoginDate() {
        return lastLoggedIn;
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
        System.out.println(new Template().ANSI_GREEN + "Deposit Final Value Withdraw: " + indonesianC.format(Math.abs(finalValue)) + new Template().ANSI_RESET);
    }

    public boolean getDisbursementStatus() {
        return disbursed;
    }

    public boolean checkDisburseAbility(Date transactionDate) {
        LocalDate dateStart = this.transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateEnd = transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int period = (Period.between(dateStart, dateEnd).getMonths());

        return period >= monthPeriod || Period.between(dateStart, dateEnd).getYears() != 0;
    }

    public void cashMoney(Date transactionDate) {
        this.lastLoggedIn = transactionDate;
        LocalDate dateStart = this.transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateEnd = transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int period = (Period.between(dateStart, dateEnd).getMonths());

        if (period < monthPeriod && Period.between(dateStart, dateEnd).getYears() == 0) {
            System.out.println(new Template().ANSI_RED + "Disbursement Before Final Period Detected!" + new Template().ANSI_RESET);
            addTransactions(transactionDate, 0f, "");
            addTransactions(transactionDate, -1f * 0.01f * this.totalValue, "1% Accumulated Depreciation For Early Disbursement");
            addTransactions(transactionDate, -50000f, "Early Disbursement Fine Fee");
        } else {
            addTransactions(transactionDate, 0);
        }

        float lastValue = this.totalValue;
        addTransactions(transactionDate, -1 * this.totalValue, "Disbursement");
        this.finalValue = lastValue;
        this.disbursed = true;
    }

    public void addTransactions(Date transactionDate, float transactionValue) {
        if (disbursed)
            return;
        lastLoggedIn = transactionDate;
        TransactionType transactionType = (transactionValue < 0) ? TransactionType.Debit : TransactionType.Credit;
        Transaction transaction = new Transaction(transactionDate, transactionType, transactionValue, "Interest Addition");

        if (getTransactionsSize() != 0) {
            LocalDate dateCurrent = transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            LocalDate dateInitialDeposit = this.transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            LocalDate dateStart = this.transactions.get(getTransactionsSize() - 1).getTransactionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate initNextMonth = LocalDate.of(dateStart.getYear()+ dateStart.getMonthValue() / 12, dateStart.getMonthValue() % 12 + 1, 1);

            int period = (Period.between(dateInitialDeposit, initNextMonth).getMonths() + Period.between(dateInitialDeposit, initNextMonth).getYears() * 12);
            while ((initNextMonth.isBefore(dateCurrent) || initNextMonth.equals(dateCurrent)) && period < getMonth()) {
                Date currDate = new DateCustom().localDateToDate(initNextMonth);
                this.transactions.add(new Transaction(currDate, TransactionType.Credit, currInterestValue(), "Interest Addition"));
                this.totalValue += currInterestValue();
                initNextMonth = initNextMonth.plusMonths(1);

                period = (Period.between(dateInitialDeposit, initNextMonth).getMonths() + Period.between(dateInitialDeposit, initNextMonth).getYears() * 12);
            }
        }

        if (transactionValue != 0) {
            this.transactions.add(transaction);
            this.totalValue += transactionValue;
        }
    }

    public void addTransactions(Date transactionDate, float transactionValue, String tmpDesc) {
        if (disbursed)
            return;
        lastLoggedIn = transactionDate;
        TransactionType transactionType = (transactionValue < 0) ? TransactionType.Debit : TransactionType.Credit;
        Transaction transaction = new Transaction(transactionDate, transactionType, transactionValue, tmpDesc);

        if (getTransactionsSize() != 0) {
            LocalDate dateCurrent = transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            LocalDate dateInitialDeposit = this.transactionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            LocalDate dateStart = this.transactions.get(getTransactionsSize() - 1).getTransactionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate initNextMonth = LocalDate.of(dateStart.getYear()+ dateStart.getMonthValue() / 12, dateStart.getMonthValue() % 12 + 1, 1);

            int period = (Period.between(dateInitialDeposit, initNextMonth).getMonths() + Period.between(dateInitialDeposit, initNextMonth).getYears() * 12);
            while ((initNextMonth.isBefore(dateCurrent) || initNextMonth.equals(dateCurrent)) && period < getMonth()) {
                Date currDate = new DateCustom().localDateToDate(initNextMonth);
                this.transactions.add(new Transaction(currDate, TransactionType.Credit, currInterestValue(), "Interest Addition"));
                this.totalValue += currInterestValue();
                initNextMonth = initNextMonth.plusMonths(1);
                period = (Period.between(dateInitialDeposit, initNextMonth).getMonths() + Period.between(dateInitialDeposit, initNextMonth).getYears() * 12);
            }
        }

        if (transactionValue != 0) {
            this.transactions.add(transaction);
            this.totalValue += transactionValue;
        }
    }

    private void setName() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Your Deposit Name? [At Least 3 Characters] -> ");
        String tmpName = scanner.nextLine();
        if (!Pattern.matches("[A-Z][a-z]{2,}", tmpName)) {
            System.out.println(new Template().ANSI_RED + "Invalid!" + new Template().ANSI_RESET);
            setName();
            return;
        }

        this.name = tmpName;
    }

    public float currInterestValue() {
        return (totalValue * (interest / (float) 12));
    }
}
