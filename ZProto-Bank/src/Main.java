import Menu.DepositMenu;
import Menu.SavingsMenu;
import User.Customer;
import Utilities.SystemRelated;
import Utilities.Template;

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        new Main().checkAccountMenu(database);
    }

    private void checkAccountMenu(Database database) {
        printTitle();

        System.out.println("1. Log In\n2. Sign Up");
        System.out.println(new Template().ANSI_RED + "[0. EXIT]" + new Template().ANSI_RESET);

        Customer currActiveCust = null;
        switch (new SystemRelated().chooseMenuInput(0, 2)) {
            case 1 -> {
                currActiveCust = new Customer(true).logIn(database.customerArrayList);
                if(currActiveCust == null)
                {
                    checkAccountMenu(database);
                    return;
                }
            }
            case 2 -> {
                currActiveCust = new Customer(false);
                database.customerArrayList.add(currActiveCust);
            }
            default -> exitProgram();
        }
        mainMenu(currActiveCust);

        checkAccountMenu(database);
    }

    private void mainMenu(Customer customer) {
        new SystemRelated().SystemCls();

        System.out.println(new Template().ANSI_PURPLE + "[Hi! " + customer.getName() + "]" + new Template().ANSI_RESET);

        System.out.println("1. Savings Menu\n2. Deposit Menu");
        System.out.println(new Template().ANSI_RED + "[0. EXIT]" + new Template().ANSI_RESET);
        switch (new SystemRelated().chooseMenuInput(0, 2)) {
            case 1 -> new SavingsMenu(customer.getSavingsAccounts(), customer.getName());
            case 2 -> new DepositMenu(customer.getDepositAccounts(), customer.getName());
            default -> {
                return;
            }
        }

        mainMenu(customer);
    }

    private void printTitle() {
        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_PURPLE + "▄▀█ █▄▄ █▀▀   █▄▄ ▄▀█ █▄░█ █▄▀\n█▀█ █▄█ █▄▄   █▄█ █▀█ █░▀█ █░█" + new Template().ANSI_RESET);
        System.out.println(new Template().ANSI_PURPLE + "[ABC BANK]" + new Template().ANSI_RESET);
    }

    private void exitProgram() {
        new SystemRelated().SystemCls();
        System.out.println(new Template().ANSI_GREEN + "System Logged Out!\nThank You for Using Our Service" + new Template().ANSI_RESET);
        System.exit(0);
    }
}