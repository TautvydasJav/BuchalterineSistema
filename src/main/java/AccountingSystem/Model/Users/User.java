package AccountingSystem.Model.Users;

import AccountingSystem.Model.AccountingOS;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    protected static SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    protected Date dateCreated = null;

    public User() {
    }

    public String getUserString() {
        return null;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPsw() {
        return psw;
    }
    public String getLoginName() {
        return loginName;
    }
    public int getAccessLevel() {
        return accessLevel;
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
    public int getId() {
        return id;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public void setPsw(String psw) {
        this.psw = psw;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
    public AccountingOS getAccounting() {return accounting;}
    public void setAccounting(AccountingOS accounting) {this.accounting = accounting;}
}