package com.parker.quizapp.controller;

import com.parker.quizapp.model.QuestionWrapper;
import com.parker.quizapp.model.Response;
import com.parker.quizapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    QuizService service;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String category,
                                             @RequestParam int numQ, @RequestParam String title) {
        return service.createQuiz(category, numQ, title);
    }

    @GetMapping("getQuiz/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id) {
        // a quiz will contain of multiple questions, and each question will have 4 options
        // and the right answer. But if we fetch all the data from Question class, it is
        // risky as user might be able to know what the right answer is.
        // so we will create a QuestionWrapper class which have all the Question fields, but
        // the right answer, difficulty level and category ( as we don't need these last 2 ).

        return service.getQuizQuestions(id);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Integer id, @RequestBody List<Response> responses) {
        return service.calculateResult(id, responses);
    }
}
