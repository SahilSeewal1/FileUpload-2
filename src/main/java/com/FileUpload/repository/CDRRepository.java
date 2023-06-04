package com.FileUpload.repository;

import com.FileUpload.models.CDR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CDRRepository extends JpaRepository<CDR, Long> {
}
