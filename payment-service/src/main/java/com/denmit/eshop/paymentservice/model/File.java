package com.denmit.eshop.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class File {

    @Id
    @SequenceGenerator(name = "filesIdSeq", sequenceName = "file_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "filesIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "path_file")
    private String pathFile;

    @OneToOne
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;
}
