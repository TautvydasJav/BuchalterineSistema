package system.web.Repositories;

        import system.model.statements.Income;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends CrudRepository<Income, Integer> {
    Income findById(int id);
}


