package AccountingSystem.Model.Categories;

import AccountingSystem.Model.Users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class SubCategory extends Category {

    @JsonBackReference
    @ManyToOne
    Category parentCategory;

    public SubCategory(String name, String desc, User author) {
        super(name, desc, author);
    }
    public SubCategory() {}

    public Category getParentCategory() {
        return parentCategory;
    }
    public void setParentCategory(Category parent) {
        this.parentCategory = parent;
    }

    @Override
    public void editName(String ID, String newName) throws Exception {
        String tempId = String.valueOf(id);
        if (tempId.equals(ID)) {  // if ID matches name is changed else recursion continues
            SubCategory editedSubCategory = this;
            editedSubCategory.name = newName;
            editedSubCategory.setDateUpdated();
            categoriesHibernateCtrl.editSubCategory(editedSubCategory);
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
            SubCategory editedSubCategory = this;
            editedSubCategory.description = newDescription;
            editedSubCategory.setDateUpdated();
            categoriesHibernateCtrl.editSubCategory(editedSubCategory);
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
                "The parent category is: ID[" + parentCategory.getId() + "], Name: " + parentCategory.getName() + "\n" +
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