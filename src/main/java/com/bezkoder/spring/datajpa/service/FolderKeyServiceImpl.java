package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.common.ResponseCode;
import com.bezkoder.spring.datajpa.exception.BusinessException;
import com.bezkoder.spring.datajpa.model.FolderKey;
import com.bezkoder.spring.datajpa.repository.FolderKeyRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FolderKeyServiceImpl implements IFolderKeyService {
    @Autowired
    private FolderKeyRepository folderKeyRepository;

    @Override
    public FolderKey addFolderKey(FolderKey folderKey) {
        if (folderKey == null) {
            throw new BusinessException(ResponseCode.INVALID_INPUT, "FolderKey must not be null");
        }

        boolean exists = folderKeyRepository.existsByFolderKeyName(folderKey.getFolderKeyName());
        if (exists) {
            throw new BusinessException(ResponseCode.ALREADY_EXISTS, "FolderKey name already exists");
        }

        return folderKeyRepository.save(folderKey);
    }


    @Override
    public FolderKey updateFolderKey(String id, FolderKey folderKey) {
        FolderKey existing = folderKeyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.ERROR, "FOLDER_NOT_FOUND"));

        existing.setFolderKeyName(folderKey.getFolderKeyName());
        existing.setFavorite(folderKey.isFavorite());

        return folderKeyRepository.save(existing);
    }

    @Override
    public boolean deleteFolderKey(String id) {
        if (Strings.isNotEmpty(id)) {
            FolderKey folderKey = folderKeyRepository.getReferenceById(id);
            folderKeyRepository.delete(folderKey);
            return true;
        }
        return false;
    }

    @Override
    public List<FolderKey> getAllFolderKey() {
        return folderKeyRepository.findAll();
    }

    public FolderKey getFolderKeyById(String id) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(ResponseCode.INVALID_INPUT, "ID must not be empty");
        }

        try {
            UUID.fromString(id); // xác thực định dạng UUID
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ResponseCode.INVALID_INPUT, "Invalid UUID format");
        }

        return folderKeyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.ID_NOT_EXISTS, "FolderKey not found"));
    }
}
