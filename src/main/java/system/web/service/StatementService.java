package system.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import system.hiberante.CategoryHibernateCtrl;
import system.hiberante.ExpenseHibernateCtrl;
import system.hiberante.IncomeHibernateCtrl;
import system.model.categories.Category;
import system.model.statements.Expense;
import system.model.statements.Income;
import system.web.Repositories.ExpenseRepository;
import system.web.Repositories.IncomeRepository;
import system.web.forms.StatementForm;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Component
public class StatementService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("AccountingHibernate");
    ExpenseHibernateCtrl expenseHibernateCtrl = new ExpenseHibernateCtrl(factory);
    IncomeHibernateCtrl incomeHibernateCtrl = new IncomeHibernateCtrl(factory);
    CategoryHibernateCtrl categoryHibernateCtrl = new CategoryHibernateCtrl(factory);

    public void deleteStatement(StatementForm form) throws Exception {
        Category category = new Category();
        Expense expense = new Expense();
        category = categoryHibernateCtrl.findCategory(Integer.parseInt(form.getCatId()));
        expense = expenseHibernateCtrl.findExpense(Integer.parseInt(form.getId()));
        categoryHibernateCtrl.removeExpenseFromCat(category, expense);
    }

    public void updateStatement(StatementForm form) throws Exception {
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
    }
}
