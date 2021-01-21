package AccountingSystem.webControllers;

        import AccountingSystem.Model.Statements.Expense;
        import AccountingSystem.Model.Users.Employee;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Component;
        import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Integer> {
    Expense findById(int id);
    Expense deleteExpenseById(int id);
}

