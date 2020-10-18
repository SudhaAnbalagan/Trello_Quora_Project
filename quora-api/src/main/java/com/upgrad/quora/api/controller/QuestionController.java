package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;


@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionBusinessService questionBusinessService;

    @Autowired
    private UserBusinessService userBusinessService;



    @RequestMapping(method = RequestMethod.POST, path = "/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") final String authorization, QuestionRequest questionRequest) throws AuthorizationFailedException {
        //String[] bearerToken = authorization.split("Bearer");


        final QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());

        final QuestionEntity createdQuestionEntity = questionBusinessService.createQuestion(authorization,questionEntity);

        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException {

        //String[] bearerToken =accessToken.split("Bearer");

        final List<QuestionEntity> allQuestions = questionBusinessService.getAllQuestion(accessToken);

        List<QuestionDetailsResponse> questionDetailsList = new LinkedList<>();

        for (QuestionEntity question : allQuestions) {
            QuestionDetailsResponse questionDetails = new QuestionDetailsResponse();
            questionDetails.setId(question.getUuid());
            questionDetails.setContent(question.getContent());
            questionDetailsList.add(questionDetails);
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsList, HttpStatus.OK) ;
    }


    @RequestMapping(method = RequestMethod.PUT, path = "/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("questionId") final String questionId,
            final QuestionEditRequest questionEditRequest
    ) throws InvalidQuestionException, AuthorizationFailedException {

       // String[] bearerToken =accessToken.split("Bearer");

        questionBusinessService.editQuestion(questionId, questionEditRequest.getContent(), accessToken);
        QuestionEditResponse response = new QuestionEditResponse().id(questionId).status("QUESTION EDITED");

        return new ResponseEntity<QuestionEditResponse>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("questionId") final String questionId
    ) throws InvalidQuestionException, AuthorizationFailedException {

        String[] bearerToken =accessToken.split("Bearer");

        questionBusinessService.deleteQuestion(questionId,accessToken);
        QuestionDeleteResponse response = new QuestionDeleteResponse().id(questionId).status("QUESTION DELETED");

        return new ResponseEntity<QuestionDeleteResponse>(response, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/all/{userId}")
    public ResponseEntity<List<QuestionDetailsResponse>> getQuestionsByUser(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("userId") final String userId
    ) throws AuthorizationFailedException, UserNotFoundException {

        UsersEntity userEntity = userBusinessService.getUserById(userId);
        userBusinessService.getUserByToken(accessToken);

        List<QuestionEntity> allQuestions = questionBusinessService.getAllQuestionsByUser(userEntity);

        List<QuestionDetailsResponse> questionDetailsList = new LinkedList<>();

        for (QuestionEntity question : allQuestions) {
            QuestionDetailsResponse questionDetails = new QuestionDetailsResponse();
            questionDetails.setId(question.getUuid());
            questionDetails.setContent(question.getContent());
            questionDetailsList.add(questionDetails);
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsList, HttpStatus.OK);

    }


}