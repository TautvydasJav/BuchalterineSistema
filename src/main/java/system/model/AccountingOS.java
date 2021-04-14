package system.model;

import system.hiberante.AccountingHibernateCtrl;
import system.hiberante.CategoryHibernateCtrl;
import system.hiberante.UsersHibernateCtrl;
import system.model.categories.Category;
import system.model.categories.SubCategory;
import system.model.categories.TopCategory;
import system.model.statements.Expense;
import system.model.statements.Income;
import system.model.users.Employee;
import system.model.users.LegalPerson;
import system.model.users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AccountingOS implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private static final long serialVersionUID = 1L;

    String Company;
    String systemVersion;

    @OneToOne
    User loggedInUser;

    @JsonBackReference
    @OneToMany(mappedBy = "accounting", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> users = new ArrayList<>();


    @JsonBackReference
    @OneToMany(mappedBy = "accounting", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<TopCategory> topCategories = new ArrayList<>();

    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("AccountingHibernate");
    static AccountingHibernateCtrl accountingHibernateCtrl = new AccountingHibernateCtrl(factory);
    static UsersHibernateCtrl usersHibernateCtrl = new UsersHibernateCtrl(factory);
    static CategoryHibernateCtrl categoriesHibernateCtrl = new CategoryHibernateCtrl(factory);

    public AccountingOS() {}
    public AccountingOS(String company, String systemVersion) throws Exception {
        Company = company;
        this.systemVersion = systemVersion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void setTopCategories(ArrayList<TopCategory> topCategories) {
        this.topCategories = topCategories;
    }

    public List<TopCategory> getTopCategories() {
        return topCategories;
    }

    public void setUsersFromDB() {
       users = usersHibernateCtrl.findUsersEntities();
    }

    public void setCategoriesFromDB() {
        topCategories = categoriesHibernateCtrl.findTopCategoryEntities();
    }

    public void setCurrentUser(User user) { loggedInUser = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void removeTopCategory(Category category) {
        this.getTopCategories().remove(category);
    }

    public User getUser(String loginName) {
        User user = new User();
        for (User u : users) {
            if (loginName.equals(u.getLoginName())){
                user = u;
                return user;}
        }

        return user;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public Category getCategory(String id) {
        Category category = new Category();
        for (TopCategory cat : topCategories) {
            if (id.equals(String.valueOf(cat.getId()))) {    //first checks if top category's ID matches with entered
                category = cat;
                break;
            } else {
                category = cat.getCategory(id, category); //else enters recursion to check sub categories
            }
        }
        return category;
    }

    public List<Expense> getExpenseListById(String id) {
        List<Expense> expenses = new ArrayList<Expense>();
        for (TopCategory cat : topCategories) {
            if (id.equals(String.valueOf(cat.getId()))) {    //first checks if top category's ID matches with entered
                expenses = cat.getExpense();
                break;
            } else {
                expenses = cat.getExpenseListById(id, expenses); //else enters recursion to check sub categories
            }
        }
        return expenses;
    }

    public List<Income> getIncomeListById(String id) {
        List<Income> income = new ArrayList<Income>();
        for (TopCategory cat : topCategories) {
            if (id.equals(String.valueOf(cat.getId()))) {    //first checks if top category's ID matches with entered
                income = cat.getIncome();
                break;
            } else {
                income = cat.getIncomeListById(id, income); //else enters recursion to check sub categories
            }
        }
        return income;
    }

    public boolean checkForAccess(int accessLevel) {
        if (loggedInUser.getAccessLevel() >= accessLevel)
            return true;
        else {
            return false;
        }
    }

    public boolean checkIfUserNameExists(String userName) {
        for (User user : users) {
            if (userName.equals(user.getLoginName()))
                return true;
        }
        return false;
    }

    public void addTopCategory(String name, String description) throws Exception {

        TopCategory newCategory = new TopCategory(name, description, loggedInUser);
        newCategory.setAccounting(this);
        categoriesHibernateCtrl.createTopCategory(newCategory);
        topCategories.add(newCategory);
    }

    public void addSubCategory(String id, String name, String description) throws Exception {

        SubCategory newCategory = new SubCategory(name, description, loggedInUser);
        for (TopCategory cat : topCategories) {
            cat.addCategory(id, newCategory, cat);
        }
    }

    public void addExpense(String id, String name, String amountStr) throws Exception {

        double amount = Double.parseDouble(amountStr);
        Expense newExpense = new Expense(name, amount);

        for (TopCategory cat : topCategories) {
            cat.addExpenseStatement(id, newExpense);
        }
        System.out.println("Expense statement added successfully");
    }

    public void addIncome(String id, String name, String amountStr) throws Exception {

        double amount = Double.parseDouble(amountStr);
        Income newIncome = new Income(name, amount);

        for (TopCategory cat : topCategories) {
            cat.addIncomeStatement(id, newIncome);
        }
        System.out.println("Income statement added successfully");
    }

    public void addUserEmp(Employee newUser) throws Exception {
        usersHibernateCtrl.create(newUser);
        System.out.println("User created successfully");
        setUsersFromDB();
    }
    public void addUserLegalPerson(LegalPerson newUser) throws Exception {
        usersHibernateCtrl.create(newUser);
        System.out.println("User created successfully");
        setUsersFromDB();
    }

    public void editNameOfCategory(String ID, String newName) throws Exception {
          for (TopCategory cat : topCategories) {
            cat.editName(ID, newName);
        }
    }

    public void editDescriptionOfCategory(String ID, String newDesc) throws Exception {
         for (TopCategory cat : topCategories) {
            cat.editDescription(ID, newDesc);
        }
    }

    public void editExpense(String catId, String expId, String newName, String newAmount) throws Exception {
           for (TopCategory cat : topCategories) {
            cat.editExpense(catId, expId, newName, newAmount);
        }
    }

    public void editIncome(String catId, String incId, String newName, String newAmount) throws Exception {
        for (TopCategory cat : topCategories) {
            cat.editIncome(catId, incId, newName, newAmount);
        }
    }

    public void deleteCategory(String ID) throws Exception {
        for (TopCategory cat : topCategories) {
            if (ID.equals(String.valueOf(cat.getId()))) {
                accountingHibernateCtrl.removeTopCategory(this,cat);
                break;
            } else {
                cat.deleteCategory(ID);
            }
        }
        setCategoriesFromDB();
    }

    public void deleteIncome(String categoryID, String incomeID) throws Exception {
        for (TopCategory cat : topCategories) {
            cat.deleteIncome(categoryID, incomeID);
        }
    }

    public void deleteExpense(String categoryID, String expenseID) throws Exception {
        for (TopCategory cat : topCategories) {
            cat.deleteExpense(categoryID, expenseID);
        }
    }

    public void deleteUser(String userName) throws Exception {
        for (User user : users) {
            if (userName.equals(user.getLoginName())) {
                usersHibernateCtrl.destroy(user.getId());
                System.out.println("User removed successfully");
                break;
            }
        }
        setUsersFromDB();
    }

    public boolean logIn(String userName, String password) {
        users = usersHibernateCtrl.findUsersEntities();
        for (User user : users) {
            if (userName.equals(user.getLoginName()) && password.equals(user.getPsw())) {
                setCurrentUser(user);
                return true;
            }
        }
        return false;
    }

    public User AppLogIn(String userName, String password) {
        users = usersHibernateCtrl.findUsersEntities();
        for (User user : users) {
            if (userName.equals(user.getLoginName()) && password.equals(user.getPsw())) {
                return user;
            }
        }
        return null;
    }

}
