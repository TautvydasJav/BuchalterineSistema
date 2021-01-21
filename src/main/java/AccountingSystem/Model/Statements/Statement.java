package AccountingSystem.Model.Statements;


import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
public class Statement implements Serializable {

    private double amount;
    private String name;
    private static SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    private Date dateCreated = null;

    public Statement(){}
    public Statement(String name, double amount){
        this.name = name;
        this.amount = amount;
        dateCreated = new Date(System.currentTimeMillis());
    }

    public double getAmount(){
        return amount;
    }
    public String getName(){
        return name;
    }
    public Date getDate(){
        return dateCreated;
    }
    public void setAmount(double newAmount){
        this.amount = newAmount;
    }
    public void setName(String newName){
        this.name = newName;
    }
}