package Statements;

import java.io.File;

public class Expense extends Statement {
    File documents;

    public Expense() {
    }

    public Expense(String name, double amount, int incomeID) {
        super(name, amount, incomeID);
    }
}
