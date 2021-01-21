package AccountingSystem.webControllers;

import AccountingSystem.Model.AccountingOS;
import AccountingSystem.Model.Users.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountingRepository extends CrudRepository<AccountingOS, Integer> {
    AccountingOS findById(int id);
}

