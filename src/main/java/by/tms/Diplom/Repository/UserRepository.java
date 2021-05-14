package by.tms.Diplom.Repository;

import by.tms.Diplom.Entity.User;
import by.tms.Diplom.Entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);
    boolean existsUserByLogin(String login);
    boolean existsUserById(long id);
    User findUserById(long id);
    List<User> findUsersByUserRole(UserRole userRole);
    boolean existsUserByUserRole(UserRole userRole);
}