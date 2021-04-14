package system.web.controllers;

import system.model.statements.Expense;
import system.model.statements.Income;
import system.web.Repositories.ExpenseRepository;
import system.web.Repositories.IncomeRepository;
import system.web.forms.StatementForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import system.web.service.StatementService;

@Controller
@RequestMapping(path = "/statement")
public class statementController {

    StatementService statementService;

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
        statementService.updateStatement(form);
        return "updated";
    }

    @PostMapping(path = "/delete")
    public @ResponseBody
    String deleteStatement(@RequestBody StatementForm form) throws Exception {
        statementService.deleteStatement(form);
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