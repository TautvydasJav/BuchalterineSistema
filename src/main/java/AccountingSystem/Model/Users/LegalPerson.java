package AccountingSystem.Model.Users;

import AccountingSystem.Model.AccountingOS;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Entity
public class LegalPerson extends User implements Serializable {

    public LegalPerson() {}
    public LegalPerson(String companyName, String psw, String loginName, int accessLevel) {
        this.name = companyName;
        this.psw = psw;
        this.loginName = loginName;
        this.accessLevel = accessLevel;
        dateCreated = new Date(System.currentTimeMillis());
    }

    @Override
    public String getUserString() {
        return name;
    }

    @Override
    public String getType() {
        return "Legal person";
    }

}
