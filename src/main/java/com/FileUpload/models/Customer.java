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
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumbers")
    private String contactNumbers;

    @Column(name = "contactAddress")
    private String contactAddress;
}
