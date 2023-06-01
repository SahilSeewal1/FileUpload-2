package com.FileUpload.helpers;

import com.FileUpload.constants.Details;
import com.FileUpload.models.Customer;
import com.FileUpload.repository.CustomerRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CSVHelper {
    public String TYPE = "text/csv";

    @Autowired
    private AES aes256Helper;
    @Autowired
    private CustomerRepository customerRepository;

    public boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public List<Customer> csvToCustomers(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Customer> customersList = new ArrayList<Customer>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Integer customerId =  Integer.parseInt(csvRecord.get("id"));
                String customerName = csvRecord.get("name");
                String contactNumbers = csvRecord.get("contactNumbers");
                String contactAddress = csvRecord.get("contactAddress");
                Optional<Customer> tempCustomer = customerRepository.findById(Long.valueOf(customerId.longValue()));
                String encryptedContactNumber = "";
                Integer previousContactCount = 1;
                try {
                    if (tempCustomer.isPresent()==false) {
                        System.out.println("New");
                        encryptedContactNumber = aes256Helper.encrypt(contactNumbers, Details.SECRET_KEY.getValue());
                        Customer customer = new Customer(
                                customerId,
                                customerName,
                                encryptedContactNumber,
                                contactAddress,
                                previousContactCount
                        );
                        customerRepository.save(customer);
                    } else {
                        String previousContactNumbers = tempCustomer.get().getCustomerContact();
                        previousContactCount = tempCustomer.get().getDistinctNumbers();
                        previousContactNumbers = aes256Helper.decrypt(previousContactNumbers,Details.SECRET_KEY.getValue());

                        encryptedContactNumber = aes256Helper.encrypt(
                                contactNumbers.concat(",").concat(previousContactNumbers)
                                ,Details.SECRET_KEY.getValue());

                        Customer customer = new Customer(
                                customerId,
                                customerName,
                                encryptedContactNumber,
                                contactAddress,
                                previousContactCount + 1
                        );

                        customerRepository.deleteById(Long.valueOf(customerId.longValue()));
                        customerRepository.save(customer);

                    }
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return customersList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}
