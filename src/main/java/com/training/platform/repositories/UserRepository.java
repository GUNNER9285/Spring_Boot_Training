
package com.training.platform.repositories;

import com.training.platform.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface UserRepository extends JpaRepository<User,Integer> {
    public List<User> findByCityAndActive(String city, Integer active);

    public List<User> findByAgeIn(List<Integer> ages);

    @Query(value = "SELECT * FROM users u WHERE u.active = ?1 and u.city = ?2 ",
            nativeQuery = true)
    public List<User> findAllByParamsQuery(Integer active,  String city);

    @Query("SELECT u FROM User u WHERE u.active = 1 and u.city = 'Bangkok' ")
    public List<User> findAllByJpqlQuery();

}
