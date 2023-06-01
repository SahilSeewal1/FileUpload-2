package com.FileUpload.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Customer")
public @Data @NoArgsConstructor @AllArgsConstructor class Customer {

    @Id
    @Column(name = "id")
    public Integer customerId;

    @Column(name = "name")
    public String customerName;

    @Column(name = "contactNumbers")
    public String customerContact;

    @Column(name = "contactAddress")
    public String customerAddress;

    @Column(name= "distinctNumbers" )
    public Integer distinctNumbers;
}
