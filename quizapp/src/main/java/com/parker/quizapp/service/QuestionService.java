package com.parker.quizapp.service;

import com.parker.quizapp.dao.QuestionDao;
import com.parker.quizapp.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao dao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(dao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.getCause();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        try {
            return new ResponseEntity<>(dao.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.getCause();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            dao.save(question);
            return new ResponseEntity<>("Data stored in db successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            e.getCause();
        }
        return new ResponseEntity<>("Error occurred while storing data in db.", HttpStatus.BAD_REQUEST);
    }
}
