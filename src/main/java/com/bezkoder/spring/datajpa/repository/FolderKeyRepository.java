package com.bezkoder.spring.datajpa.repository;

import com.bezkoder.spring.datajpa.model.FolderKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderKeyRepository extends JpaRepository<FolderKey, String> {

}
