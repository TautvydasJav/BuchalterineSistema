package system.web.controllers;

        import system.hiberante.AccountingHibernateCtrl;
        import system.model.AccountingOS;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.*;
        import system.web.Repositories.AccountingRepository;

        import javax.persistence.EntityManagerFactory;
        import javax.persistence.Persistence;


@Controller
@RequestMapping(path = "/accounting")
public class AccountingController {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("AccountingHibernate");
    AccountingHibernateCtrl accountingHibernateCtrl = new AccountingHibernateCtrl(factory);

    @Autowired
    private AccountingRepository accountingRepository;

    @GetMapping(path = "/get_by_id")
    public @ResponseBody
    AccountingOS getByID(@RequestParam int id) {
        AccountingOS accounting = accountingRepository.findById(id);
        return accounting;
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<AccountingOS> getAccountingSystems() {
        return accountingRepository.findAll();
    }

    @PostMapping(path = "/update")
    public @ResponseBody
    String update(@RequestParam AccountingOS accountingOS) throws Exception {
        accountingHibernateCtrl.edit(accountingOS);
        return "updated";
    }

    @DeleteMapping(path="/delete")
    public @ResponseBody String delete(@RequestParam int id) throws Exception {
        accountingHibernateCtrl.destroy(id);
        return "deleted";
    }

}

