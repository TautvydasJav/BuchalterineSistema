package AccountingSystem.webControllers;

import AccountingSystem.Model.Categories.Category;
import AccountingSystem.Model.Users.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Category findById(int id);
}

