package User;

import Account.Deposit;
import Account.Savings;
import Utilities.SystemRelated;
import Utilities.Template;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Customer {
    private String name;
    private String password;
    private ArrayList<Savings> savingsAccounts;
    private ArrayList<Deposit> depositAccounts;

    private Scanner scanner;

    public Customer(boolean isLogin) {
        scanner = new Scanner(System.in);
        new SystemRelated().SystemCls();

        if(!isLogin) {
            setName();
            setPassword();

            this.savingsAccounts = new ArrayList<>();
            this.depositAccounts = new ArrayList<>();

            System.out.println(new Template().ANSI_GREEN + "Successfully Created Account!" + new Template().ANSI_RESET);
            System.out.println("Press Enter to Continue...");
            scanner.nextLine();
        }
    }

    public Customer logIn(ArrayList<Customer> customerArrayList) {
        System.out.print(new Template().ANSI_PURPLE + "[LOG IN]" + new Template().ANSI_RESET + "\nName? ");
        String name = scanner.nextLine();

        System.out.print("Password? ");
        String password = scanner.nextLine();

        Customer currActiveCust = logInForCust(name, password, customerArrayList);
        new SystemRelated().SystemCls();
        if (currActiveCust != null) {
            System.out.println(new Template().ANSI_GREEN + "Successfully Logged In!" + new Template().ANSI_RESET);
            System.out.println("Press Enter to Continue...");
            scanner.nextLine();

            return currActiveCust;
        } else {
            System.out.println(new Template().ANSI_RED + "No Such Account Found!" + new Template().ANSI_RESET);
            System.out.println("Press Enter to Continue...");
            scanner.nextLine();
        }

        return null;
    }

    private Customer logInForCust(String name, String password, ArrayList<Customer> customerArrayList) {
        for (Customer currCust : customerArrayList) {
            if (name.compareTo(currCust.getName()) == 0) {
                return (password.compareTo(currCust.getPassword()) == 0) ? currCust : null;
            }
        }
        return null;
    }

    public ArrayList<Deposit> getDepositAccounts() {
        return depositAccounts;
    }

    public ArrayList<Savings> getSavingsAccounts() {
        return savingsAccounts;
    }

    public void setName() {
        System.out.print("Your Full Name? [Pascal Case for first letter (Ziven Ziven)] [At Least 3 Characters] -> ");
        String tmpName = scanner.nextLine();
        if (!Pattern.matches("(\\b[A-Z]{1}[a-z]+)( )([A-Z]{1}[a-z]+\\b)", tmpName)) {
            System.out.println(new Template().ANSI_RED + "Invalid!" + new Template().ANSI_RESET);
            setName();
            return;
        }

        this.name = tmpName;
    }

    public String getName() {
        return name;
    }

    public void setPassword() {

        System.out.print("Your Password? [min. 8 Characters] -> ");
        String tmpPass = scanner.nextLine();
        if (!Pattern.matches(".{8,}", tmpPass)) {
            System.out.println(new Template().ANSI_RED + "Invalid!" + new Template().ANSI_RESET);
            setPassword();
            return;
        }

        this.password = tmpPass;
        new SystemRelated().SystemCls();
    }

    public String getPassword() {
        return password;
    }

}
