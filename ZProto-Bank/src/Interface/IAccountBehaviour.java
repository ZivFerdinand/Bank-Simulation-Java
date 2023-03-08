package Interface;

import Account.Transaction;

import java.util.Date;

public interface IAccountBehaviour {
    float currInterestValue();
    void addTransactions(Date transactionDate, float transactionValue, String tmpDesc);
    void addTransactions(Date transactionDate, float transactionValue);
    void printBalance();
    String getName();
    Date getLastLoginDate();
    int getTransactionsSize();
    Transaction getTransactionAt(int index);
}
