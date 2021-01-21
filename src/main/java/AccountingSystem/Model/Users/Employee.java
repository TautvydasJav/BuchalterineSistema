package AccountingSystem.Model.Users;

import AccountingSystem.Model.AccountingOS;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Entity
public class Employee extends User implements Serializable {

    private String lastName;
    private String firstName;

    public Employee() {}
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
