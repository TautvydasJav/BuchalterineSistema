package AccountingSystem.webControllers;

        import AccountingSystem.Model.Categories.TopCategory;
        import AccountingSystem.Model.Users.Employee;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Component;
        import org.springframework.stereotype.Repository;

@Repository
public interface TopCategoryRepository extends CrudRepository<TopCategory, Integer> {
    TopCategory findById(int id);
}