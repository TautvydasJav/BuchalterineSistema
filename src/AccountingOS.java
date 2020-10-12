import Categories.Category;
import Categories.SubCategory;
import Categories.TopCategory;
import Statements.Expense;
import Statements.Income;
import Users.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountingOS implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int accessLevelAdmin = 3;
    private final int accessLevelMid = 2;
    private final int accessLevelLow = 1;
    private int categoryID = 1;
    private int expenseID = 1;
    private int incomeID = 1;
    String Company;
    String systemVersion;
    User loggedInUser;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<TopCategory> topCategories = new ArrayList<>();

    public AccountingOS() {
    }

    public AccountingOS(String company, String sysVer) {
        this.Company = company;
        this.systemVersion = sysVer;
    }

    public void printTopCategories() {
        if (topCategories.isEmpty()) {
            System.out.println("No categories saved");
            return;
        }
        System.out.println("\tID: " + "\tName: ");
        for (TopCategory cat : topCategories)
            System.out.println("| \t" + cat.getID() + "\t| " + cat.getName());
    }

    public void printAllCategories() {
        if (topCategories.isEmpty()) {
            System.out.println("No categories saved");
            return;
        }
        for (TopCategory cat : topCategories) {
            cat.printSubCategories("", true);
        }
    }

    public void printStatements(String ID) {
        if (topCategories.isEmpty()) {
            System.out.println("No statements saved");
            return;
        }
        boolean doesStatementsExists;
        for (TopCategory cat : topCategories) {
            doesStatementsExists = cat.printAllStatements(ID, false);
            if (doesStatementsExists)
                return;
        }
        System.out.println("No statements saved");
    }

    public void printUsers() {
        for (User user : users) {
            if (loggedInUser.getAccessLevel() <= 2)
                user.printUser();
            else if (loggedInUser.getAccessLevel() == 3)
                user.printUserForAdmin();
        }
    }
    public boolean checkIdExists(String ID) {
        boolean idExists = false;
        for (TopCategory cat : topCategories) {
            if (ID.equals(String.valueOf(cat.getID()))) {    //first checks if top category's ID matches with entered
                idExists = true;
                break;
            } else {
                idExists = cat.findIfIdExists(ID, false); //else enters recursion to check sub categories
                if (idExists)
                    break;
            }
        }
        return idExists;
    }

    public boolean checkForAccess(int accessLevel) {
        if (loggedInUser.getAccessLevel() >= accessLevel)
            return true;
        else {
            System.out.println("Logged in user does not have access");
            System.out.println("Press Any Key To Continue...");
            new java.util.Scanner(System.in).nextLine();
            return false;
        }
    }
    public boolean checkIfUserNameExists(String userName) {
        for(User user : users){
            if(userName.equals(user.getLoginName())){
                System.out.println("User name is already taken");
                return true;
            }
        }
        return false;
    }

    public void addTopCategory() {
        if (!checkForAccess(accessLevelMid))
            return;
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object

        System.out.println("Enter name of the new categor(y)");
        System.out.println("(");
        String name = myObj.nextLine();  // Read user input

        System.out.println("Enter description of the new category");
        String description = myObj.nextLine();

        TopCategory newCategory = new TopCategory(categoryID, name, description, loggedInUser);
        topCategories.add(newCategory);
        categoryID++;
    }

    public void addSubCategory() {
        if (!checkForAccess(accessLevelMid))
            return;
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter the ID of category to which you want to add a subcategory");
        String id = myObj.nextLine();

        if (!checkIdExists(id)) {
            System.out.println("The ID you entered doesn't exist");
            return;
        }

        System.out.println("Enter name of the new category");
        String name = myObj.nextLine();  // Read user input

        System.out.println("Enter description of the new category");
        String description = myObj.nextLine();

        SubCategory newCategory = new SubCategory(categoryID, name, description, loggedInUser);
        categoryID++;
        for (TopCategory cat : topCategories) {
            cat.addCategory(id, newCategory, cat);
        }
    }

    public void addExpense(String id) {
        if (!checkForAccess(accessLevelMid))
            return;
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter the name of Statements.Expense statement");
        String name = myObj.nextLine();

        System.out.println("Enter the amount ");
        double amount = myObj.nextDouble();

        Expense newExpense = new Expense(name, amount, expenseID);

        for (TopCategory cat : topCategories) {
            cat.addExpenseStatement(id, newExpense);
        }
        System.out.println("Statements.Expense statement added successfully");
        expenseID++;
    }

    public void addIncome(String id) {
        if (!checkForAccess(accessLevelMid))
            return;
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter the name of Statements.Income statement");
        String name = myObj.nextLine();

        System.out.println("Enter the amount ");
        double amount = myObj.nextDouble();

        Income newIncome = new Income(name, amount, incomeID);

        for (TopCategory cat : topCategories) {
            cat.addIncomeStatement(id, newIncome);
        }
        System.out.println("Statements.Income statement added successfully");
        incomeID++;
    }

    public void addUser() {
        if (!checkForAccess(accessLevelAdmin)) {
            return;
        }
        Scanner myObj = new Scanner(System.in);
        User newUser = null;
        System.out.println("If it's employee type [1], if it's legal person type [2]");
        String selection = myObj.nextLine();
        if (selection.equals("1")) {
            System.out.println("Enter login name");
            String loginName = myObj.nextLine();
            if(checkIfUserNameExists(loginName))
                return;
            System.out.println("Enter password");
            String psw = myObj.nextLine();
            System.out.println("Enter first name");
            String name = myObj.nextLine();
            System.out.println("Enter last name");
            String nameLast = myObj.nextLine();
            System.out.println("Enter access level([1] low access, [2] mid level access, [3] admin)");
            int accessLevel = myObj.nextInt();
            newUser = new Employee(name, nameLast, psw, loginName, accessLevel);
        } else if (selection.equals("2")) {
            System.out.println("Enter login name");
            String loginName = myObj.nextLine();
            if(checkIfUserNameExists(loginName))
                return;
            System.out.println("Enter password");
            String psw = myObj.nextLine();
            System.out.println("Enter company's name: ");
            String name = myObj.nextLine();
            System.out.println("Enter access level([1] low access, [2] mid level access, [3] admin)");
            int accessLevel = myObj.nextInt();
            newUser = new LegalPerson(name, psw, loginName, accessLevel);
        }

        users.add(newUser);
        System.out.println("User created successfully");
    }

    public void editNameOfCategory(String ID) {
        if (!checkForAccess(accessLevelMid))
            return;
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter new name");
        String newName;  // Read user input
        newName = myObj.nextLine();
        for (TopCategory cat : topCategories) {
            cat.setName(ID, newName);
        }
    }

    public void editDescriptionOfCategory(String ID) {
        if (!checkForAccess(accessLevelMid))
            return;
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter new description");
        String newDescr;  // Read user input
        newDescr = myObj.nextLine();
        for (TopCategory cat : topCategories) {
            cat.setDescription(ID, newDescr);
        }
    }

    public void deleteCategory(String ID) {
        if (!checkForAccess(accessLevelMid))
            return;
        boolean deleteTopCategory = false;
        Category toDelete = null;
        for (TopCategory cat : topCategories) {
            if (ID.equals(String.valueOf(cat.getID()))) {
                toDelete = cat;
                deleteTopCategory = true;
                break;
            } else {
                cat.deleteCategory(ID);
            }
        }
        if (deleteTopCategory)
            topCategories.remove(toDelete);
    }

    public void deleteIncome(String categoryID) {
        if (!checkForAccess(accessLevelMid))
            return;
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the ID of income statement to delete it");
        String id;  // Read user input
        id = myObj.nextLine();
        for (TopCategory cat : topCategories) {
            cat.deleteIncome(categoryID, id);
        }
    }

    public void deleteExpense(String categoryID) {
        if (!checkForAccess(accessLevelMid))
            return;
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the ID of expense statement to delete it");
        String id;  // Read user input
        id = myObj.nextLine();
        for (TopCategory cat : topCategories) {
            cat.deleteExpense(categoryID, id);
        }
    }

    public void deleteUser() {
        if (!checkForAccess(accessLevelAdmin))
            return;
        Scanner myObj = new Scanner(System.in);
        User userToDelete = null;
        System.out.println("Enter the login user name of the user you want to delete");
        String enteredUserName;  // Read user input
        enteredUserName = myObj.nextLine();
        if (loggedInUser.getLoginName().equals(enteredUserName)) {
            System.out.println("Failed to delete you are logged in as this user");
            return;
        } else {
            for (User user : users) {
                if (user.getLoginName().equals(enteredUserName)) {
                    userToDelete = user;
                    System.out.println("User removed successfully");
                    break;
                }
            }
        }
        if (userToDelete != null)
            users.remove(userToDelete);
        else
            System.out.println("ID does not exists");
    }

    public void logIn() {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("------------------Welcome to accountingOS---------------");
        System.out.println("                                      version 1.00      ");
        System.out.println(" ");

        while (true) {
            System.out.println("Enter user name: ");
            String userName = myObj.nextLine();
            System.out.println("Enter password: ");
            String enteredPsw = myObj.nextLine();
            for (User user : users) {
                if (userName.equals(user.getLoginName()) && enteredPsw.equals(user.getPsw())) {
                    loggedInUser = user;
                    System.out.println("Logged in successfully!");
                    System.out.println("Press Any Key To Continue...");
                    new java.util.Scanner(System.in).nextLine();
                    return;
                } else if (userName.equals(user.getLoginName())) {
                    System.out.println("Password is incorrect");
                    System.out.println("Press Any Key To Continue...");
                    new java.util.Scanner(System.in).nextLine();
                    break;
                }
            }
            System.out.println("Try again...");
        }
    }

    public void menu() {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        String selection;  // Read user input
        boolean stop = false;
        System.out.println("------------------Welcome to accountingOS---------------");
        System.out.println("                                      version 1.00      ");
        System.out.println("Logged in as: " + loggedInUser.getName());

        while (!stop) {
            System.out.println("Enter the number to perform the desired operation");
            System.out.println("📕" + "MENU:" + "📕");
            System.out.println("(1) Print only top categories");
            System.out.println("(2) Prints all categories");
            System.out.println("(3) Adds top category");
            System.out.println("(4) Adds sub category");
            System.out.println("(5) Select category");
            System.out.println("(6) Prints all users");
            System.out.println("(7) Add new user");
            System.out.println("(8) Delete user");
            System.out.println("(0) To exit");
            selection = myObj.nextLine();  // Read user input

            switch (selection) {
                case "1":
                    printTopCategories();
                    break;
                case "2":
                    printAllCategories();
                    break;
                case "3":
                    addTopCategory();
                    break;
                case "4":
                    addSubCategory();
                    break;
                case "5":
                    menuOfSelectedCategory();
                    break;
                case "6":
                    printUsers();
                    break;
                case "7":
                    addUser();
                    break;
                case "8":
                    deleteUser();
                    break;
                case "0":
                    stop = true;
                    break;
                default:
                    System.out.println("Entered action does not exists");
            }
            System.out.println("Press Any Key To Continue...");
            new java.util.Scanner(System.in).nextLine();
        }
    }

    public void menuOfSelectedCategory() {
        boolean stop = false;
        System.out.println("\"Enter the ID of category you want to select\"");
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        String selection;
        String id;  // Read user input
        id = myObj.nextLine();

        if (!checkIdExists(id)) {
            System.out.println("The ID you entered doesn't exist");
            return;
        }
        System.out.println("-----------------------------------------");
        System.out.println("📕" + " THE CATEGORY MENU " + "📕");
        while (!stop) {
            for (TopCategory cat : topCategories) {
                cat.findTheCategoryToPrint(id);
            }
            System.out.println("-----------------------------------------");
            System.out.println("Enter the number to perform the desired operation");
            System.out.println("-----------------------------------------");
            System.out.println("(1) Edit name");
            System.out.println("(2) Edit description");
            System.out.println("(3) Go to Incomes and Expenses");
            System.out.println("(4) Delete this category");
            System.out.println("(0) Exit back to menu");
            System.out.println("-----------------------------------------");
            selection = myObj.nextLine();
            switch (selection) {

                case "1":
                    editNameOfCategory(id);
                    break;
                case "2":
                    editDescriptionOfCategory(id);
                    break;
                case "3":
                    menuOfSelectedStatements(id);
                    break;
                case "4":
                    deleteCategory(id);
                    System.out.println("Selected category is deleted");
                    stop = true;
                    break;
                case "0":
                    stop = true;
                    break;
                default:
                    System.out.println("Entered action does not exists");
            }
        }
    }

    public void menuOfSelectedStatements(String id) {
        boolean stop = false;
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        String selection;

        while (!stop) {
            System.out.println("-----------------------------------------");
            System.out.println("📕" + " INCOMES AND EXPENSES " + "📕");

            printStatements(id);
            System.out.println("-----------------------------------------");
            System.out.println("Enter the number to perform the desired operation");
            System.out.println("-----------------------------------------");
            System.out.println("(1) Add Expense");
            System.out.println("(2) Add Income");
            System.out.println("(3) Delete Expense");
            System.out.println("(4) Delete Income");
            System.out.println("(0) Go back to category");
            System.out.println("-----------------------------------------");
            selection = myObj.nextLine();
            switch (selection) {
                case "1":
                    addExpense(id);
                    break;
                case "2":
                    addIncome(id);
                    break;
                case "3":
                    deleteExpense(id);
                    break;
                case "4":
                    deleteIncome(id);
                    break;
                case "0":
                    stop = true;
                    break;
                default:
                    System.out.println("Entered action does not exists");
            }
        }
    }
}