package com.bezkoder.spring.datajpa.repository;

import com.bezkoder.spring.datajpa.entity.FolderKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderKeyRepository extends JpaRepository<FolderKey, String> {

    boolean existsByName(@NotBlank(message = "Folder name must not be empty") @Size(max = 255, message = "Folder name must be under 255 characters") String name);
}
