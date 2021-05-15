package com.demoim_backend.demoim_backend.repository;

import com.demoim_backend.demoim_backend.model.CheckNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CheckNumRepository extends JpaRepository<CheckNum,Long> {

    CheckNum findByUsernameAndCertNumber(String username, String certNumber);

    CheckNum findByUsernameAndIsChecked(String username,String isChecked);

    @Transactional
    void deleteAllByUsername(String username);
}
