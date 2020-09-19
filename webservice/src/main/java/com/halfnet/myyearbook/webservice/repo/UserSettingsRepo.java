package com.halfnet.myyearbook.webservice.repo;

import com.halfnet.myyearbook.webservice.entity.UserSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingsRepo extends CrudRepository<UserSettings, Long> {

}
