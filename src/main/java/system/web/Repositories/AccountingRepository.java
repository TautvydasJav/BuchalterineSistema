package system.web.Repositories;

import system.model.AccountingOS;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountingRepository extends CrudRepository<AccountingOS, Integer> {
    AccountingOS findById(int id);
}

