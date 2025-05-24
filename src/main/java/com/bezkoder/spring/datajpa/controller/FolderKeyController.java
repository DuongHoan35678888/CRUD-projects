package com.bezkoder.spring.datajpa.controller;

import com.bezkoder.spring.datajpa.common.ResponseCode;
import com.bezkoder.spring.datajpa.dto.ApiResponse;
import com.bezkoder.spring.datajpa.dto.BooleanResponse;
import com.bezkoder.spring.datajpa.dto.FolderKeyListResponse;
import com.bezkoder.spring.datajpa.model.FolderKey;
import com.bezkoder.spring.datajpa.service.IFolderKeyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/folder")
public class FolderKeyController {

    @Autowired
    private IFolderKeyService folderKeyService;

    @PostMapping
    public ResponseEntity<ApiResponse<FolderKey>> addFolderKey(@RequestBody @Valid FolderKey folderKey) {
        FolderKey addFolderKey = folderKeyService.addFolderKey(folderKey);
        return ResponseEntity.ok(ApiResponse.success(addFolderKey));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FolderKey>> updateFolderKey(@PathVariable String id,
                                                                  @RequestBody @Valid FolderKey folderKey) {
        FolderKey updateFolderKey = folderKeyService.updateFolderKey(id, folderKey);
        return ResponseEntity.ok(ApiResponse.success(updateFolderKey));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<BooleanResponse>> deleteFolderKey(@PathVariable String id) {
        boolean deleted = folderKeyService.deleteFolderKey(id);
        return ResponseEntity.ok(ApiResponse.success(new BooleanResponse(deleted)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<FolderKeyListResponse>> getAllFolderKey() {
        String requestId = UUID.randomUUID().toString();
        List<FolderKey> list = folderKeyService.getAllFolderKey();
        FolderKeyListResponse response = new FolderKeyListResponse(list);
        return ResponseEntity.ok(new ApiResponse<>(ResponseCode.SUCCESS, requestId, response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FolderKey>> getFolderKeyById(@PathVariable String id, HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();

        // Validate id không null, không rỗng
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(ResponseCode.INVALID_INPUT, requestId, new FolderKey()));
        }

        FolderKey folderKey = folderKeyService.getFolderKeyById(id);
        return ResponseEntity.ok(ApiResponse.success(folderKey));
    }
}
