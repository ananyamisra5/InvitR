package com.halfnet.myyearbook.webservice.repo;

import com.halfnet.myyearbook.webservice.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends CrudRepository<Token, Long>{
    
    public boolean existsByToken(String token);
    public Token getByToken(String token);

}
