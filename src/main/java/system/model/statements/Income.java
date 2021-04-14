package system.model.statements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import system.model.categories.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Income extends Statement {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JsonBackReference
    Category category;

    public Income(String name, double amount) {
        super(name, amount);
    }
}
