package system.model.statements;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Statement implements Serializable {

    private double amount;
    private String name;
    private static SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    private Date dateCreated = null;

    public Statement(String name, double amount){
        this.name = name;
        this.amount = amount;
        dateCreated = new Date(System.currentTimeMillis());
    }
}