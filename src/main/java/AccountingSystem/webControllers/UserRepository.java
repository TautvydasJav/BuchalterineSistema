package AccountingSystem.webControllers;

        import AccountingSystem.Model.Users.Employee;
        import AccountingSystem.Model.Users.User;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Component;
        import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
        User findById(int id);
        User findByLoginNameAndPsw(String loginName, String psw);
}
