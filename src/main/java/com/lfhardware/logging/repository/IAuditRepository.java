package com.lfhardware.logging.repository;

import com.lfhardware.logging.domain.Audit;
import com.lfhardware.shared.CrudRepository;

public interface IAuditRepository extends CrudRepository<Audit, Long> {
}
