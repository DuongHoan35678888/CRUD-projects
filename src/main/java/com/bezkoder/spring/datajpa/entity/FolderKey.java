package com.bezkoder.spring.datajpa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @NotBlank(message = "Folder name must not be empty")
    @Size(max = 255, message = "Folder name must be under 255 characters")
    @JsonProperty("name")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @JsonProperty("is_favorite")
    @Column(name = "favorite")
    private Boolean favorite;

}
