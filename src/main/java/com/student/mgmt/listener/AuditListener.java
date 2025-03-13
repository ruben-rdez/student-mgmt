package com.student.mgmt.listener;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.student.mgmt.entity.Audit;
import com.student.mgmt.entity.Student;
import com.student.mgmt.repository.AuditRepository;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class AuditListener {

    private final AuditRepository auditRepository;

    @PostPersist
    private void prePersis(Student student) {
        //String user = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Audit for Inserting Student : " 
            + student.getId() + " at " + LocalDateTime.now());

        Audit audit = new Audit();
        audit.setStudentid(student.getId());
        audit.setDate(LocalDateTime.now());
        audit.setAction("INSERT");
        audit.setUser("ADMIN");
        this.auditRepository.save(audit);
    }

    @PreUpdate
    private void preUpdate(Student student) {
        //String user = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Audit for Updating Student : " 
            + student.getId() + " at " + LocalDateTime.now());

        Audit audit = new Audit();
        audit.setStudentid(student.getId());
        audit.setDate(LocalDateTime.now());
        audit.setAction("UPDATE");
        audit.setUser("ADMIN");
        this.auditRepository.save(audit);
    }

    @PreRemove
    private void preRemove(Student student) {
        //String user = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Audit for Deleting Student : " 
            + student.getId() + " at " + LocalDateTime.now());

        Audit audit = new Audit();
        audit.setStudentid(student.getId());
        audit.setDate(LocalDateTime.now());
        audit.setAction("DELETE");
        audit.setUser("ADMIN");
        this.auditRepository.save(audit);
    }

}
