package system.model.categories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import system.hiberante.CategoryHibernateCtrl;
import system.hiberante.ExpenseHibernateCtrl;
import system.hiberante.IncomeHibernateCtrl;
import system.model.users.*;
import system.model.statements.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected int id;

    protected String name;
    protected String description;
    protected static  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    protected Date dateCreated = null;
    protected Date dateUpdated = null;

    @OneToOne
    protected User author;

    @OneToMany(mappedBy = "parentCategory", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    protected List<SubCategory> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    protected List<Income> income = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade= CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    protected List<Expense> expense = new ArrayList<>();

    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("AccountingHibernate");
    static CategoryHibernateCtrl categoriesHibernateCtrl = new CategoryHibernateCtrl(factory);
    static IncomeHibernateCtrl incomeHibernateCtrl = new IncomeHibernateCtrl(factory);
    static ExpenseHibernateCtrl expenseHibernateCtrl = new ExpenseHibernateCtrl(factory);


    public Category(String name, String desc, User author) {
        this.name = name;
        this.description = desc;
        this.author = author;
        dateCreated = new Date(System.currentTimeMillis());
    }

    public void setSubCategoriesFromDB() {
        subCategories = categoriesHibernateCtrl.findSubCategoryEntities();
    }
    public void setIncomeListFromDB() {
        income = incomeHibernateCtrl.findIncomeEntities();
    }
    public void setExpenseListFromDB() {
        expense = expenseHibernateCtrl.findExpenseEntities();
    }

    public void setDateUpdated() {
        this.dateUpdated = new Date(System.currentTimeMillis());
    }

    public String takeThisCategoryInfo() {
        return "";
    }

    public void removeExpense(Expense expense) {
        this.getExpense().remove(expense);
    }
    public void removeIncome(Income income) {
        this.getIncome().remove(income);
    }
    public void removeSubCategory(Category category) {
        this.getSubCategories().remove(category);
    }

    public void addCategory(String ID, SubCategory newSubCategory, Category parent) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {
            newSubCategory.setParentCategory(parent); // set parent category
            categoriesHibernateCtrl.createSubCategory(newSubCategory);
            subCategories.add(newSubCategory);
            System.out.println("New Category added successfully");
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.addCategory(ID, newSubCategory, cat);
            }
        }
    }

    public void addExpenseStatement(String ID, Expense newExpense) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {
            newExpense.setCategory(this);
            expense.add(newExpense);
            expenseHibernateCtrl.create(newExpense);
            System.out.println("New expense statement added successfully");
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.addExpenseStatement(ID, newExpense);
            }
        }
    }

    public void addIncomeStatement(String ID, Income newIncome) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {
            newIncome.setCategory(this);
            income.add(newIncome);
            incomeHibernateCtrl.create(newIncome);
            System.out.println("New income statement added successfully");
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.addIncomeStatement(ID, newIncome);
            }
        }
    }

    public List<Expense> getExpenseListById(String ID, List<Expense> expense) {
        for (SubCategory cat : subCategories) { // else continuing recursion
            if(ID.equals(String.valueOf(cat.getId()))) {
                setExpenseListFromDB();
                expense = cat.expense;
                return expense;
            }
            else
                expense = cat.getExpenseListById(ID, expense);
        }
        return expense;
    }

    public List<Income> getIncomeListById(String ID, List<Income> income) {
        for (SubCategory cat : subCategories) { // else continuing recursion
            if(ID.equals(String.valueOf(cat.getId()))) {
                setIncomeListFromDB();
                income = cat.income;
                return income;
            }
            else
                income = cat.getIncomeListById(ID, income);
        }
        return income;
    }

    public double getAllExpense(double amount) {
        if (!expense.isEmpty()) {
            for (Expense statement : expense) {
                amount += statement.getAmount();
            }
        }
        for (SubCategory cat : subCategories) {
            amount += cat.getAllExpense(0.0);
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
            double temp = 0;
            temp += cat.getAllIncome(0.0);
            amount += temp;
        }
        return amount;
    }

    public Category getCategory(String ID, Category category) {
        for (SubCategory cat : subCategories) { // else continuing recursion
            if(ID.equals(String.valueOf(cat.getId()))) {
                category = cat;
                return category;
            }
            else
                category = cat.getCategory(ID, category);
        }
        return category;
    }

    public void editName(String ID, String newName) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {  // if ID matches name is changed else recursion continues
            this.name = newName;
            setDateUpdated();
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.editName(ID, newName);
            }
        }
    }

    public void editDescription(String ID, String newDescription) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {// if ID matches description is changed else recursion continues
            this.description = newDescription;
            setDateUpdated();
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.editDescription(ID, newDescription);
            }
        }
    }

    public void deleteCategory(String ID) throws Exception {
        for (SubCategory cat : subCategories) {
            if (ID.equals(String.valueOf(cat.getId()))) {
                categoriesHibernateCtrl.removeSubCategory(this, cat);
                break;
            } else {
                cat.deleteCategory(ID);
            }
        }
    }

    public void deleteExpense(String catID, String expenseID) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(catID)) {
            for (Expense statement : expense) {
                tempId = String.valueOf(statement.getId());
                if (tempId.equals(expenseID)) {
                    categoriesHibernateCtrl.removeExpenseFromCat(this, statement);
                    System.out.println("Expense statement deleted successfully");
                    setExpenseListFromDB();
                    break;
                }
            }
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.deleteExpense(catID, expenseID);
            }
        }
    }

    public void deleteIncome(String catID, String incomeID) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(catID)) {
            for (Income statement : income) { // else continuing recursion
                tempId = String.valueOf(statement.getId());
                if (tempId.equals(incomeID)) {
                    categoriesHibernateCtrl.removeIncomeFromCat(this, statement);
                    System.out.println("Income statement deleted successfully");
                    setExpenseListFromDB();
                    break;
                }
            }
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.deleteIncome(catID, incomeID);
            }
        }
    }

    public void editExpense(String catID, String expenseID, String newName, String newAmount) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(catID)) {
            for (Expense statement : expense) {
                tempId = String.valueOf(statement.getId());
                if (tempId.equals(expenseID)) {
                    Expense editedExpense = statement;
                    if(!newName.isEmpty())
                        editedExpense.setName(newName);
                    if(!newAmount.isEmpty()){
                        double amount = Double.parseDouble(newAmount);
                        editedExpense.setAmount(amount);
                    }
                    expenseHibernateCtrl.edit(editedExpense);
                    break;
                }
            }
        } else {
            for (SubCategory cat : subCategories)
                cat.editExpense(catID, expenseID, newName, newAmount);
        }
    }

    public void editIncome(String catID, String incomeID, String newName, String newAmount) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(catID)) {
            for (Income statement : income) {
                tempId = String.valueOf(statement.getId());
                if (tempId.equals(incomeID)) {
                    Income editedIncome = statement;
                    if(!newName.isEmpty())
                        editedIncome.setName(newName);
                    if(!newAmount.isEmpty()){
                        double amount = Double.parseDouble(newAmount);
                        editedIncome.setAmount(amount);
                    }
                    incomeHibernateCtrl.edit(editedIncome);
                    break;
                }
            }
        } else {
            for (SubCategory cat : subCategories)
                cat.editIncome(catID, incomeID, newName, newAmount);
        }
    }
}
