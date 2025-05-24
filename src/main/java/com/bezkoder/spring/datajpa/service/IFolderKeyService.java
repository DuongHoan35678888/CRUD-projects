package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.model.FolderKey;

import java.util.List;

public interface IFolderKeyService {

    FolderKey addFolderKey(FolderKey folderKey);

    FolderKey updateFolderKey(String id, FolderKey employee);

    boolean deleteFolderKey(String id);

    List<FolderKey> getAllFolderKey();

    FolderKey getFolderKeyById(String id);
}
