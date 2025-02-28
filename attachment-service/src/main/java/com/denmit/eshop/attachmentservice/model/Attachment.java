package com.denmit.eshop.attachmentservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attachments")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Attachment {

    @Id
    @SequenceGenerator(name = "attachmentsIdSeq", sequenceName = "attachment_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachmentsIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "order_id")
    private Long orderId;
}
