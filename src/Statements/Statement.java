package Statements;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Statement implements Serializable {
    private int id;
    private double amount;
    private String name;
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    private Date dateCreated = null;

    public Statement(){}

    public Statement(String name, double amount, int id){
        this.id = id;
        this.name = name;
        this.amount = amount;
        dateCreated = new Date(System.currentTimeMillis());
    }
    public int getID(){
        return id;
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
