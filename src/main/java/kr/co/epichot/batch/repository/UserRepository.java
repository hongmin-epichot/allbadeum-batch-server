package kr.co.epichot.batch.repository;

import kr.co.epichot.batch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
