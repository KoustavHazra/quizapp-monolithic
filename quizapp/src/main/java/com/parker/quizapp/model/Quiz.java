package com.parker.quizapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // this will make the id auto-generated.
    private Integer id;
    private String title;

    @ManyToMany
    private List<Question> questions;

}
