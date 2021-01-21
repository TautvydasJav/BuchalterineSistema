package AccountingSystem.webControllers;

import AccountingSystem.HibernateControl.UsersHibernateCtrl;
import AccountingSystem.Model.AccountingOS;
import AccountingSystem.Model.Categories.TopCategory;
import AccountingSystem.Model.Users.Employee;
import AccountingSystem.Model.Users.LegalPerson;
import AccountingSystem.Model.Users.User;
import AccountingSystem.webControllers.Forms.LogInForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("AccountingHibernate");
    UsersHibernateCtrl usersHibernateCtrl = new UsersHibernateCtrl(factory);

    AccountingOS accountingOS = new AccountingOS();

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/get_by_id")
    public @ResponseBody
    User getUserByID(@RequestParam int id) {
        User user = userRepository.findById(id);
        return user;
    }

    @PostMapping(path = "/login")
    public @ResponseBody
    User getLogInUser(@RequestBody LogInForm form) {
        User user = userRepository.findByLoginNameAndPsw(form.getLoginName(),form.getPsw());
        return user;
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path = "/update")
    public @ResponseBody
    String updateTopCategory(@RequestParam User user) throws Exception {
        usersHibernateCtrl.edit(user);
        return "updated";
    }

    @DeleteMapping(path="/delete")
    public @ResponseBody String deleteEmpById(@RequestParam int id) throws Exception {
        usersHibernateCtrl.destroy(id);
        return "deleted";
    }
}
