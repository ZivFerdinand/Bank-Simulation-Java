package Account;

import java.util.Date;

public class Transaction {
    private final Date transactionDate;
    private final TransactionType transactionType;
    private final float value;
    private final String description;

    public Transaction(Date transactionDate, TransactionType transactionType, float value, String desc) {
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.value = value;
        this.description = desc;
    }

    public String getDesc() {
        return description;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionType() {
        return (transactionType == TransactionType.Credit) ? "CR" : "DR";
    }

    public float getValue() {
        return Math.abs(value);
    }
}
