package com.FileUpload.models;

import lombok.Builder;
@Builder
public class CustomerResponse {
    public Integer customerId;

    public String customerName;

    public String[] customerContact;

    public String customerAddress;

    public Integer distinctNumbers;

}
