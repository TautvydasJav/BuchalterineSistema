package AccountingSystem.Model.Statements;

import AccountingSystem.Model.Categories.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.File;

@Entity
public class Expense extends Statement {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    File documents;

    @JsonBackReference
    @ManyToOne
    Category category;

    public Expense(String name, double amount) {
        super(name, amount);
    }
    public Expense() {}

    public Category getCategory() {return category;}
    public int getId() {
        return id;
    }
    public File getDocuments() {return documents;}

    public void setCategory(Category category) {this.category = category;}
    public void setId(int id) {
        this.id = id;
    }
    public void setDocuments(File documents) {this.documents = documents;}

}
