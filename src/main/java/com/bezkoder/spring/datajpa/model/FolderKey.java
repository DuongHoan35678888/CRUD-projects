package com.bezkoder.spring.datajpa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "folder_key")
public class FolderKey {
    @Id
    private String id;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Column(name = "folderKeyName", nullable = false)
    private String folderKeyName;

    @Column(name = "isFavorite")
    private boolean isFavorite;

}
