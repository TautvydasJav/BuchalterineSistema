package Categories;

import Users.*;

import java.io.*;

public class TopCategory extends Category implements Serializable {

    public TopCategory(int id, String name, String desc, User author) {
        super(id, name, desc, author);
    }

    @Override
    public void printThisCategory() {
        System.out.println("You selected: ");
        System.out.println("-------------------------------");
        System.out.println("ID: " + getID());
        System.out.println("Name: " + getName());
        System.out.println("Description: " + getDescription());
        System.out.println("Parent: This is top category ");
        System.out.println("All income of this category and it's subcategories: " + getAllIncome(0.0));
        System.out.println("All expense of this category and it's subcategories: " + getAllExpense(0.0));
        System.out.println("Date then added: " + formatter.format(dateCreated));
        if (dateUpdated != null)
            System.out.println("Date updated: " + formatter.format(dateUpdated));
        else
            System.out.println("Date updated: has not been updated");
    }
}
