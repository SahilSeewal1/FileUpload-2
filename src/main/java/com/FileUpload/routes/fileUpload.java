package com.FileUpload.routes;

import com.FileUpload.helpers.CSVHelper;
import com.FileUpload.models.Customer;
import com.FileUpload.models.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/csv")
public class fileUpload {

    @Autowired
    CSVHelper csvHelper;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (csvHelper.hasCSVFormat(file)) {
            try {
                List<Customer> customerList =  csvHelper.csvToCustomers(file.getInputStream());
                System.out.println(customerList);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                System.out.println(e);
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

}
