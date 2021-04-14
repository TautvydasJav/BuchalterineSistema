package system.web.Repositories;

        import system.model.categories.TopCategory;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface TopCategoryRepository extends CrudRepository<TopCategory, Integer> {
    TopCategory findById(int id);
}