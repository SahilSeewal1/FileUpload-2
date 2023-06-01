package com.FileUpload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.FileUpload.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
