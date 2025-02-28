package com.denmit.eshop.commentservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Comment {

    @Id
    @SequenceGenerator(name = "commentsIdSeq", sequenceName = "comment_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentsIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @Column(name = "text")
    private String text;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_id")
    private Long orderId;
}
