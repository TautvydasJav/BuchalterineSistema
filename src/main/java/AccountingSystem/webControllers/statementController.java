package AccountingSystem.webControllers;

import AccountingSystem.HibernateControl.CategoryHibernateCtrl;
import AccountingSystem.HibernateControl.ExpenseHibernateCtrl;
import AccountingSystem.HibernateControl.IncomeHibernateCtrl;
import AccountingSystem.HibernateControl.UsersHibernateCtrl;
import AccountingSystem.Model.Categories.Category;
import AccountingSystem.Model.Categories.SubCategory;
import AccountingSystem.Model.Categories.TopCategory;
import AccountingSystem.Model.Statements.Expense;
import AccountingSystem.Model.Statements.Income;
import AccountingSystem.webControllers.Forms.StatementForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Controller
@RequestMapping(path = "/statement")
public class statementController {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("AccountingHibernate");
    ExpenseHibernateCtrl expenseHibernateCtrl = new ExpenseHibernateCtrl(factory);
    IncomeHibernateCtrl incomeHibernateCtrl = new IncomeHibernateCtrl(factory);
    CategoryHibernateCtrl categoryHibernateCtrl = new CategoryHibernateCtrl(factory);

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;


    @GetMapping(path = "/get_expense_by_id")
    public @ResponseBody
    Expense getExpenseByID(@RequestParam int id) {
        Expense expense = expenseRepository.findById(id);
        return expense;
    }

    @GetMapping(path = "/get_income_by_id")
    public @ResponseBody
    Income getIncomeByID(@RequestParam int id) {
        Income income = incomeRepository.findById(id);
        return income;
    }

    @PostMapping(path = "/update")
    public @ResponseBody
    String updateStatement(@RequestBody StatementForm form) throws Exception {
        Expense expense = expenseRepository.findById(Integer.parseInt(form.getId()));
        Income income = new Income();
        if(expense != null) {
            if(!form.getName().isEmpty())
                expense.setName(form.getName());
            if(!form.getAmount().isEmpty())
                expense.setAmount(Double.parseDouble(form.getAmount()));
            expenseHibernateCtrl.edit(expense);
        }
        else{
            income = incomeRepository.findById(Integer.parseInt(form.getId()));
            if(!form.getName().isEmpty())
                income.setName(form.getName());
            if(!form.getAmount().isEmpty())
                income.setAmount(Double.parseDouble(form.getAmount()));
            incomeHibernateCtrl.edit(income);
        }
        return "updated";
    }

    @PostMapping(path = "/delete")
    public @ResponseBody
    String deleteStatement(@RequestBody StatementForm form) throws Exception {
        Category category = new Category();
        Expense expense = new Expense();
        category = categoryHibernateCtrl.findCategory(Integer.parseInt(form.getCatId()));
        expense = expenseHibernateCtrl.findExpense(Integer.parseInt(form.getId()));
        categoryHibernateCtrl.removeExpenseFromCat(category, expense);
        //expenseHibernateCtrl.destroy(Integer.parseInt(form.getId()));
        return "deleted";
    }

    @GetMapping(path="/allExpense")
    public @ResponseBody Iterable<Expense> getAllExpense() {
        return expenseRepository.findAll();
    }

    @GetMapping(path="/allIncome")
    public @ResponseBody Iterable<Income> getAllIncome() {
        return incomeRepository.findAll();
    }

}