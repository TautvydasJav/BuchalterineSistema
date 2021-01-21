package AccountingSystem.webControllers.Forms;

public class StatementForm {
    private String id;
    private String name;
    private String amount;
    private String catId;

    public StatementForm(String id, String name, String amount, String catId) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.catId = catId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }
}
