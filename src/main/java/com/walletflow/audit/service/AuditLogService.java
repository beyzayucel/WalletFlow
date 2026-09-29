package com.walletflow.audit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {

    Page<AuditLogResponse> getAuditLogs(AuditLogScope scope, String search, Pageable pageable);



    void createAuditLogForSelf(
            AuditActionType action,
            User user
    );

    void createAuditLogForAdmin(AuditActionType action, User targetUser);
}