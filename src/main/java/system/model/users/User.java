package system.model.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import system.model.AccountingOS;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    protected int id;

    @JsonManagedReference
    @ManyToOne
    protected AccountingOS accounting;

    protected String name;
    protected String psw;
    protected String loginName;
    protected int accessLevel;
    protected SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    protected Date dateCreated = null;

    public String getUserString() {
        return null;
    }

    public String getAccessLevelString() {
        if(accessLevel == 1)
            return "Low";
        if(accessLevel == 2)
            return "Medium";
        if(accessLevel == 3)
            return "Admin";
        return "0";
    }
    public String getType() {
        return "User";
    }

    public String getDateCreated() {
        return dateCreated.toString();
    }
}