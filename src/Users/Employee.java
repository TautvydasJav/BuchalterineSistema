package Users;

import java.io.Serializable;
import java.util.Date;


public class Employee extends User implements Serializable {

    private String lastName;
    private String firstName;

    public Employee(String firstName, String lastName, String psw, String loginName, int accessLevel) {
        this.name = firstName + " " + lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.psw = psw;
        this.loginName = loginName;
        this.accessLevel = accessLevel;
        dateCreated = new Date(System.currentTimeMillis());
    }

    @Override
    public void printUser() {
        System.out.println("Name of employee: " + name + ", Access level: " + accessLevel + " | created " + dateCreated);
        System.out.println("------------------------------");
    }

    @Override
    public void printUserForAdmin() {
        System.out.println("Name of employee: " + firstName + " " + lastName + ", Access level: " + accessLevel + " | created " + dateCreated);
        System.out.println(" LoginName: " + loginName + ", password: " + psw);
        System.out.println("------------------------------");
    }
}
