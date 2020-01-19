package cn.com.clm.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import cn.com.clm.pojo.User;

public interface UserRepository extends JpaRepository<User, String> {
}
