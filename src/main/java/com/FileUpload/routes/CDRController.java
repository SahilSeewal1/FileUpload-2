package com.FileUpload.routes;

import com.FileUpload.models.CDR;
import com.FileUpload.repository.CDRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cdr")
public class CDRController {

    @Autowired
    private CDRRepository cdrRepository;

    @GetMapping(value="/getAllCDRs")
    public List<CDR> getAllCDRs() {
        return cdrRepository.findAll();
    }

    @PostMapping(value = "/uploadCDR")
    public CDR addCDR(@RequestBody CDR cdr) {
        return cdrRepository.save(cdr);
    }

}
