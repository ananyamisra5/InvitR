package com.halfnet.myyearbook.webservice.repo;

import com.halfnet.myyearbook.webservice.entity.PasswordReset;
import com.halfnet.myyearbook.webservice.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetRepo extends CrudRepository<PasswordReset, Long> {

    public PasswordReset findByUser(User u);
}
