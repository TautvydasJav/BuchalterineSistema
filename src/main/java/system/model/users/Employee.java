package system.model.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import system.model.AccountingOS;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee extends User implements Serializable {

    private String lastName;
    private String firstName;

    public Employee(String firstName, String lastName, String psw, String loginName, int accessLevel, AccountingOS accounting) {
        this.name = firstName + " " + lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.psw = psw;
        this.loginName = loginName;
        this.accessLevel = accessLevel;
        dateCreated = new Date(System.currentTimeMillis());
        this.accounting = accounting;
    }

    @Override
    public String getUserString() {
        return firstName + " " + lastName;
    }

    @Override
    public String getType() {
        return "Employee";
    }
}
