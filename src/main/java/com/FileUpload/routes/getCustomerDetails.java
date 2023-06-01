package com.FileUpload.routes;

import com.FileUpload.helpers.AES;
import com.FileUpload.models.Customer;
import com.FileUpload.models.CustomerResponse;
import com.FileUpload.models.ResponseMessage;
import com.FileUpload.repository.CustomerRepository;
import com.FileUpload.utilities.CustomerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.FileUpload.constants.Details;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class getCustomerDetails {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AES aesHelper;

    @Autowired
    CustomerUtility customerUtility;

    @GetMapping(value="/getCustomerDetails")
    public ResponseEntity getCustomerDetailsById(@RequestParam("Id") Integer customerId) {

        Optional<Customer> customerData = customerRepository.findById(Long.valueOf(customerId.longValue()));

        if(!customerData.isPresent()){
            //No customer
            String message = "User Not Found";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
       }
        String[] Contacts = customerUtility.handleCustomerContacts(aesHelper, customerData);

        return ResponseEntity.status(HttpStatus.OK).body(
                CustomerResponse.builder()
                .customerId(customerData.get().customerId)
                .customerName(customerData.get().customerName)
                .customerContact(Contacts)
                .customerAddress(customerData.get().customerAddress)
                .distinctNumbers(customerData.get().distinctNumbers)
                .build());
    }

    @GetMapping(value="/getAllCustomerDetails")
    public ResponseEntity getAllCustomerDetails() {

        List<Customer> customerDataList = customerRepository.findAll();

        if(customerDataList.isEmpty()){
            //No customer
            return new ResponseEntity<List<Customer>>(new ArrayList<>(), HttpStatus.OK);
        }
        List<CustomerResponse> customerContactList = new ArrayList<>();

        for(Customer customerData: customerDataList) {
            String[] Contacts = customerUtility.handleCustomerContacts(aesHelper, Optional.of(customerData));

            customerContactList.add(CustomerResponse.builder()
                    .customerId(customerData.customerId)
                    .customerName(customerData.customerName)
                    .customerContact(Contacts)
                    .customerAddress(customerData.customerAddress)
                    .distinctNumbers(customerData.distinctNumbers)
                    .build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(customerContactList);
    }

    @GetMapping(value="/getCustomerContacts")
    public ResponseEntity getContactsById(@RequestParam("Id") Integer customerId) throws IllegalAccessException {
        String[] Contacts;
        String message = "";
        Optional<Customer> customerData = customerRepository.findById(Long.valueOf(customerId.longValue()));

        if(!customerData.isPresent()){
            //No customer
            message = "User Not Found";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
        String encryptedContacts = aesHelper.decrypt(customerData.get().customerContact,Details.SECRET_KEY.getValue());
        Contacts = encryptedContacts.split("[,]",0);

        for (int i =0;i<Contacts.length;i++) {
            Contacts[i] = aesHelper.encrypt(Contacts[i],Details.SECRET_KEY.getValue());
        }
        return ResponseEntity.status(HttpStatus.OK).body(Contacts);
    }
}
