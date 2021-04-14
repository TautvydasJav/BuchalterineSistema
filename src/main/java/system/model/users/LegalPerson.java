package system.model.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class LegalPerson extends User implements Serializable {

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
