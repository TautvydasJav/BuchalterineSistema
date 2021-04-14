package system.web.Repositories;

        import system.model.statements.Expense;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Integer> {
    Expense findById(int id);
    Expense deleteExpenseById(int id);
}

