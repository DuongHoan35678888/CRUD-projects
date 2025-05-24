package com.bezkoder.spring.datajpa.dto;

import com.bezkoder.spring.datajpa.model.FolderKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderKeyListResponse {
    private List<FolderKey> folders;
}

