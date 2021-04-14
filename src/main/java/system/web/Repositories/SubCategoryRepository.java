package system.web.Repositories;

        import system.model.categories.SubCategory;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends CrudRepository<SubCategory, Integer> {
    SubCategory findById(int id);
}