package AccountingSystem.webControllers;

        import AccountingSystem.Model.Statements.Income;
        import AccountingSystem.Model.Users.Employee;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Component;
        import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends CrudRepository<Income, Integer> {
    Income findById(int id);
}


