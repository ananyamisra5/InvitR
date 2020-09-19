package com.halfnet.myyearbook.webservice.repo;

import com.halfnet.myyearbook.webservice.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User,Long>{
    User findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
