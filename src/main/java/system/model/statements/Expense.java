package system.model.statements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import system.model.categories.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.File;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
