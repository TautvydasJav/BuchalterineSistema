package AccountingSystem.Model.Statements;

import AccountingSystem.Model.Categories.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity

public class Income extends Statement {

    @Id

    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;


    @ManyToOne
    @JsonBackReference
    Category category;

    public Income(){}
    public Income(String name, double amount) {
        super(name, amount);
    }

    public Category getCategory() {return category;}
    public int getId() {
        return id;
    }

    public void setCategory(Category category) {this.category = category;}
    public void setId(int id) {
        this.id = id;
    }

}
