package com.parker.quizapp.service;

import com.parker.quizapp.dao.QuestionDao;
import com.parker.quizapp.dao.QuizDao;
import com.parker.quizapp.model.Question;
import com.parker.quizapp.model.QuestionWrapper;
import com.parker.quizapp.model.Quiz;
import com.parker.quizapp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    // create different set of quiz tables
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        // we need to get the list of questions first, to create the quiz
        // also we cannot get all the questions, we need questions based on the arguments passed
        // so, we will create this method manually in the QuestionDao class.
        List<Question> questionList = questionDao.getQuestionsByCategory(category, numQ);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questionList);
        // id is auto generated, so we are not setting it. And we have set the title and questions.
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }


    // gives us the quiz tables based on the passed id
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        // here we have only returned the quiz_questions table (from pgAdmin) with the given id
        // and, now we have to convert these questions into a QuestionWrapper.
        // so we will get the quiz_questions list with passed id, then we are fetching
        // all the questions in that list and converting each of the questions to QuestionWrapper.

        List<Question> questionsFromDB = quiz.get().getQuestions();  // since we have used Optional to get questions
        // we have to use that .get()

        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        // we created this list for adding all the question wrappers
        // we will run a loop on all the questionsFromDB and convert them into wrapper using the
        // constructor of the QuestionWrapper class.
        for (Question q: questionsFromDB) {
            QuestionWrapper wrappedQuestions = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(),
                                                            q.getOption2(), q.getOption3(), q.getOption4());
            questionsForUser.add(wrappedQuestions);
        }

        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }


    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        // get the quiz table with the given id
        Optional<Quiz> quiz = quizDao.findById(id);
        // from the quiz table we fetched, get the list of questions
        List<Question> questionsFromDB = quiz.get().getQuestions();
        // now that we have all the questions and their correct answers in the questionsFromDB
        // we just need to compare the questionsFromDB values with the "responses" value.
        // and along with that we will count the total number of correct answers and return it.
        int count = 0;
        int i = 0;
        for (Response response: responses) {
            if (response.getResponse().equals(questionsFromDB.get(i).getRightAnswer())) {
                count++;
            }
            i++;
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}


/*
We already have a Question table, now to create a quiz based on different categories or
difficulty levels, we need another table - Quiz table. And this 2 table should be
mapped with each other.

Now when we have 2 tables mapped with each other, we can have either one-to-one relationship,
one-to-many relationship or many-to-many relationship.

in one-to-one every quiz will have one question only - we don't want that.
in one-to-many every quiz will have multiple questions, but same questions cannot be in other quiz -
        we don't want that as well
in many-to-many, every quiz will have multiple questions, also other quiz can have the same questions --
this is what we are going for here.
 */