package com.FileUpload.helpers;

import com.FileUpload.constants.Details;
import com.FileUpload.models.Customer;
import com.FileUpload.repository.CustomerRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExcelHelper {
    @Autowired
    private AES aes256Helper;
    @Autowired
    private CustomerRepository customerRepository;

    private DataFormatter dataFormatter = new DataFormatter();
    private String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public List<Customer> excelToCustomers(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            int contactCount = 0;

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<Customer> customers = new ArrayList<Customer>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Customer customer = new Customer();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            customer.setCustomerId((int)currentCell.getNumericCellValue());
                            break;

                        case 1:

                            customer.setCustomerName(currentCell.getStringCellValue());
                            break;

                        case 2: {
                            String contacts = dataFormatter.formatCellValue(currentCell).replaceAll("\\s", "");
                            String encryptedString = "";
                            Set<String> contactsSet
                                    = Stream.of(contacts.split(","))
                                    .collect(Collectors.toSet());
                            contactCount = contactsSet.size();
                            for(String contact: contactsSet){
                                encryptedString += aes256Helper.encrypt(contact, Details.SECRET_KEY.getValue()) + " ";
                            }

                            customer.setCustomerContact(encryptedString.substring(0, encryptedString.length()-1));
                            break;
                        }

                        case 3:
                            customer.setCustomerAddress(currentCell.getStringCellValue());
                            break;

                        default:
                            break;
                    }

                    cellIdx++;
                }

                customer.setDistinctNumbers(contactCount);
                customerRepository.save(customer);
                customers.add(customer);
            }

            workbook.close();
            return customers;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse Excel file: " + e.getMessage());
        }
    }
}
