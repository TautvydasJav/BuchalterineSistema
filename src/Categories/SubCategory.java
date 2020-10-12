package Categories;

import Users.*;

public class SubCategory extends Category {

    Category parentCategory;

    public SubCategory(int id, String name, String desc, User author) {
        super(id, name, desc, author);
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parent) {
        this.parentCategory = parent;
    }

    @Override
    public void printThisCategory() {
        System.out.println("You selected: ");
        System.out.println("-------------------------------");
        System.out.println("ID: " + getID());
        System.out.println("Name: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("The parent category is: ID[" + parentCategory.getID() + "], Name: " + parentCategory.getName());
        System.out.println("Total Statements.Income: " + getAllIncome(0.0));
        System.out.println("Total Statements.Expense: " + getAllExpense(0.0));
        System.out.println("Date then added: " + formatter.format(dateCreated));
        if (dateUpdated != null)
            System.out.println("Date then updated: " + formatter.format(dateUpdated));
        else
            System.out.println("Date updated: has not been updated");
    }

}
