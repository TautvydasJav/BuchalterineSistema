package AccountingSystem.webControllers.Forms;

public class LogInForm {
    String loginName;
    String psw;

    public LogInForm(String loginName, String psw) {
        this.loginName = loginName;
        this.psw = psw;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
}
