package system.model.categories;

import system.model.AccountingOS;
import system.model.users.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class TopCategory extends Category implements Serializable {

    @JsonManagedReference
    @ManyToOne
    private AccountingOS accounting;

    public TopCategory(String name, String desc, User author) {
        super(name, desc, author);
    }
    public TopCategory() {}

    public void setAccounting(AccountingOS accounting) {this.accounting = accounting;}

    @Override
    public void editName(String ID, String newName) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {  // if ID matches name is changed else recursion continues
            TopCategory editedTopCategory = this;
            editedTopCategory.name = newName;
            editedTopCategory.setDateUpdated();
            categoriesHibernateCtrl.editTopCategory(editedTopCategory);
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.editName(ID, newName);
            }
        }
    }
    @Override
    public void editDescription(String ID, String newDescription) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {// if ID matches description is changed else recursion continues
            TopCategory editedTopCategory = this;
            editedTopCategory.description = newDescription;
            editedTopCategory.setDateUpdated();
            categoriesHibernateCtrl.editTopCategory(editedTopCategory);
        } else {
            for (SubCategory cat : subCategories) { // else continuing recursion
                cat.editDescription(ID, newDescription);
            }
        }
    }

    @Override
    public String takeThisCategoryInfo() {
        String text;
        text = "You selected: \n" +
                "-------------------------------" + "\n" +
                "ID: " + getId() + "\n" +
                "Name: " + getName() + "\n" +
                "Description: " + getDescription() + "\n" +
                "Parent: This is top category"  + "\n" +
                "Total income: " + getAllIncome(0.0) + "\n" +
                "Total expense: " + getAllExpense(0.0) + "\n" +
                "Date then added: " + formatter.format(dateCreated) + "\n";

        if (dateUpdated != null)
            text += "Date then updated: " + formatter.format(dateUpdated) + "\n";
        else
            text += "Date updated: has not been updated" + "\n";

        return text;
    }
}
