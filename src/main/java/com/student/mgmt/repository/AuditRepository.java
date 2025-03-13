package com.student.mgmt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.student.mgmt.entity.Audit;

@Repository
public interface AuditRepository extends CrudRepository<Audit, Long> {
}
