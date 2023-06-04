package com.FileUpload.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "CDR")
public @Data @NoArgsConstructor @AllArgsConstructor class CDR {

    @Id
    @Column(name = "callId")
    private Long callId;

    @Column(name = "recordingURL")
    private String recordingURL;

    @Column(name = "caller")
    private String caller;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "timestamp")
    private Timestamp timestamp;
}
