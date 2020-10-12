import java.io.*;
import java.io.File;
import java.io.IOException;
/** Šiame laboratoriniame sukuriau buchalterine sistema kurioj
 *  skaičiavimams atlikti.
 *  Naudodamasis dvimačius ir vienmačius masyvus, skaičiavau vidurkius, rikiavau duomenis ir atlikau paprastas operacijas
 */

public class Start {

    public static AccountingOS readData(String fileName) {

        File file = new File(fileName);
        AccountingOS AccountingOS = new AccountingOS();

        try {
            if (file.createNewFile()) {
                System.out.println("New empty data file has been created.");
                return AccountingOS; // if new data file is created returns an empty object
            } else {
                System.out.println("The data file found successfully"); // if not continues to read data from file
            }
        } catch (IOException i) {
            System.out.println("Error while creating the file");
            i.printStackTrace();
        }

        if (file.length() == 0) {
            System.out.println("The file is empty");
            return AccountingOS;
        }

        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            AccountingOS = (AccountingOS) in.readObject(); //Deserialization done here
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Categories.Category class not found");
            c.printStackTrace();
        }
        System.out.println("Data was successfully scanned");
        return AccountingOS;

    }

    public static void saveData(AccountingOS AccountingOS, String fileName) {
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(AccountingOS);

            out.close();
            file.close();

            System.out.println("Data has been saved");

        } catch (IOException ex) {
            System.out.println("IOException is caught");
        }
    }

    public static void main(String[] args) {

        String fileName = "src\\data.ser";
        AccountingOS AccountingOS = new AccountingOS("PRif1803", "1.00");
        AccountingOS = readData(fileName);
        AccountingOS.logIn();
        AccountingOS.menu();
        saveData(AccountingOS, fileName);
        System.out.println("Program ended");
    }
}
