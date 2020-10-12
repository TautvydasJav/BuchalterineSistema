package Users;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class LegalPerson extends User implements Serializable {

    public LegalPerson() {
    }

    public LegalPerson(String companyName, String psw, String loginName, int accessLevel) {
        this.name = companyName;
        this.psw = psw;
        this.loginName = loginName;
        this.accessLevel = accessLevel;
        dateCreated = new Date(System.currentTimeMillis());
    }

    @Override
    public void printUser() {
        System.out.println("Company's name: " + name + ", Access level: " + accessLevel + " | created " + dateCreated);
        System.out.println("------------------------------");
    }

    @Override
    public void printUserForAdmin() {
        System.out.println("Company's name: " + name + ", Access level: " + accessLevel + " | created " + dateCreated);
        System.out.println(" LoginName: " + loginName + ", password: " + psw);
        System.out.println("------------------------------");
    }
}
