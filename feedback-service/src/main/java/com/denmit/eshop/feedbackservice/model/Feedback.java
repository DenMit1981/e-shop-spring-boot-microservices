package com.denmit.eshop.feedbackservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Feedback {

    @Id
    @SequenceGenerator(name = "feedbacksIdSeq", sequenceName = "feedback_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedbacksIdSeq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "rate")
    private String rate;

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
