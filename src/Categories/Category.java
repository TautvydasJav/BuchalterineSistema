package Categories;

import Users.*;
import Statements.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Date;

public class Category implements Serializable {

    protected String name;
    protected String description;
    protected int id;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    protected Date dateCreated = null;
    protected Date dateUpdated = null;
    protected User author;
    protected ArrayList<SubCategory> subCategories = new ArrayList<>();
    protected ArrayList<Income> income = new ArrayList<>();
    protected ArrayList<Expense> expense = new ArrayList<>();

    public Category() {
    }

    public Category(int id, String name, String desc, User author) {
        this.id = id;
        this.name = name;
        this.description = desc;
        this.author = author;
        dateCreated = new Date(System.currentTimeMillis());
    }

    public void printSubCategories(String indent, boolean last) {
        System.out.println(indent + "+- " + "|ID:" + this.id + "|" + this.name);
        indent += last ? "   " : "|  ";

        for (SubCategory cat : subCategories) {
            cat.printSubCategories(indent, last);
        }
    }

    public void printThisCategory() {
    }

    public void findTheCategoryToPrint(String ID) {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID))
            printThisCategory();
        else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.findTheCategoryToPrint(ID);
            }
        }
    }

    public boolean printAllStatements(String ID, boolean isEmpty) {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {
            System.out.println("------------------------------");
            if (!expense.isEmpty()) {
                System.out.println("Expenses:");
                for (Expense statement : expense)
                    System.out.println("ID: [" + statement.getID() + "] " + statement.getName() + ": -" +
                            statement.getAmount() + " | created by: " + "UserName :" + author.getLoginName() +
                            author.getName() + " " + formatter.format(statement.getDate()));
            } else {
                System.out.println("Expenses:");
                System.out.println("---empty---:");
            }
            if (!income.isEmpty()) {
                System.out.println("------------------------------");
                System.out.println("Income:");
                for (Income statement : income)
                    System.out.println("ID: [" + statement.getID() + "] " + statement.getName() + ": +" +
                            statement.getAmount() + " | created by: " + "UserName :" + author.getLoginName() +
                            author.getName() + " " + formatter.format(statement.getDate()));
                System.out.println("------------------------------");
            } else {
                System.out.println("Income:");
                System.out.println("---empty---:");
            }
            return true;
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.printAllStatements(ID, isEmpty);
                if (isEmpty)
                    return true;
            }
        }
        return isEmpty;
    }

    public boolean findIfIdExists(String ID, boolean state) {
        if (state)
            return true;
        for (SubCategory cat : subCategories) {
            if (ID.equals(String.valueOf(cat.getID()))) { // else continuing recursion
                return true;
            } else {
                state = cat.findIfIdExists(ID, false);
                if (state)
                    return true;
            }
        }
        return false;
    }

    public void addCategory(String ID, SubCategory newSubCategory, Category parent) {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {
            newSubCategory.setParentCategory(parent); // set parent category
            subCategories.add(newSubCategory);
            System.out.println("New Category added successfully");
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.addCategory(ID, newSubCategory, cat);
            }
        }
    }

    public void addExpenseStatement(String ID, Expense newExpense) {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {
            expense.add(newExpense);
            System.out.println("New expense statement added successfully");
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.addExpenseStatement(ID, newExpense);
            }
        }
    }

    public void addIncomeStatement(String ID, Income newIncome) {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {
            income.add(newIncome);
            System.out.println("New income statement added successfully");
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.addIncomeStatement(ID, newIncome);
            }
        }
    }

    public double getAllExpense(double amount) {
        if (!expense.isEmpty()) {
            for (Expense statement : expense) {
                amount += statement.getAmount();
            }
        }
        for (SubCategory cat : subCategories) {
            amount += cat.getAllExpense(amount);
        }
        return amount;
    }

    public double getAllIncome(double amount) {

        if (!income.isEmpty()) {
            for (Income statement : income) {
                amount += statement.getAmount();
            }
        }
        for (SubCategory cat : subCategories) {
            amount += cat.getAllIncome(amount);
        }
        return amount;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getID() {
        return this.id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setName(String ID, String newName) {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {  // if ID matches name is changed else recursion continues
            this.name = newName;
            setDateUpdated();
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.setName(ID, newName);
            }
        }
    }

    public void setDescription(String ID, String newDescription) {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {// if ID matches description is changed else recursion continues
            this.description = newDescription;
            setDateUpdated();
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.setDescription(ID, newDescription);
            }
        }
    }

    public void setDateUpdated() {
        this.dateUpdated = new Date(System.currentTimeMillis());
    }

    public void deleteCategory(String ID) {
        Category toDelete = new Category();
        for (SubCategory cat : subCategories) {
            if (ID.equals(String.valueOf(cat.getID()))) {
                toDelete = cat;
                break;
            } else {
                cat.deleteCategory(ID);
            }
        }
        subCategories.remove(toDelete);
    }

    public void deleteExpense(String catID, String expenseID) {
        String tempId = String.valueOf(id);
        Expense toDelete;
        toDelete = null;
        if (tempId.equals(catID)) {
            for (Expense statement : expense) { // else continuing recursion
                tempId = String.valueOf(statement.getID());
                if (tempId.equals(expenseID))
                    toDelete = statement;
                break;
            }
            if (toDelete != null) {
                expense.remove(toDelete);
                System.out.println("Expense statement deleted successfully");
            } else
                System.out.println("ID does not exists");
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.deleteExpense(catID, expenseID);
            }
        }
    }

    public void deleteIncome(String catID, String incomeID) {
        String tempId = String.valueOf(id);
        Income toDelete = new Income();
        if (tempId.equals(catID)) {
            for (Income statement : income) { // else continuing recursion
                tempId = String.valueOf(statement.getID());
                if (tempId.equals(incomeID))
                    toDelete = statement;
                break;
            }
            if (toDelete != null) {
                income.remove(toDelete);
                System.out.println("Income statement deleted successfully");
            } else
                System.out.println("ID does not exists");
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.deleteIncome(catID, incomeID);
            }
        }
    }
}
