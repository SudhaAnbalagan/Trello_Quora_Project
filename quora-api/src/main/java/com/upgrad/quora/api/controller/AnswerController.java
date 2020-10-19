package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/")






public class AnswerController {

    @Autowired
    private  AnswerService answerService;

    @Autowired
    private UserBusinessService userBusinessService;


    @RequestMapping(method = RequestMethod.POST,path = "/question/{questionId}/answer/create",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer (AnswerRequest   answerRequest, @RequestHeader("authorization") final String authorization,@PathVariable("questionId") final String questionId) throws InvalidQuestionException, AuthorizationFailedException {
        final AnswerEntity answerEntity=answerService.createAnswer(questionId,authorization,answerRequest.getAnswer());
        AnswerResponse answerResponse=new AnswerResponse();
        answerResponse.id(answerEntity.getUuid()).status("ANSWER CREATED");


        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.OK);

    }
    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("answerId") final String answerId,
            final AnswerEditRequest answerEditRequest
    ) throws InvalidQuestionException, AuthorizationFailedException {

        // String[] bearerToken =accessToken.split("Bearer");

        answerService.editAnswer(answerId, answerEditRequest.getContent(), accessToken);
        AnswerEditResponse response = new AnswerEditResponse().id(answerId).status("ANSWER EDITED");

        return new ResponseEntity<AnswerEditResponse>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("answerId") final String answerId
    ) throws InvalidQuestionException, AuthorizationFailedException {

        String[] bearerToken =accessToken.split("Bearer");

        answerService.deleteAnswer(answerId,accessToken);
        AnswerDeleteResponse response = new AnswerDeleteResponse().id(answerId).status("QUESTION DELETED");

        return new ResponseEntity<AnswerDeleteResponse>(response, HttpStatus.OK);
    }
//    @RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestions(@RequestHeader("authorization") final String accessToken,@PathVariable("questionId") final String questionId) throws AuthorizationFailedException {
//
//
//        final List<AnswerEntity> allAnswers = answerService.getAllAnswersByUser(accessToken);
//
//        List<AnswerDetailsResponse> answerDetailsList = new LinkedList<>();
//
//        for (AnswerEntity answer : allAnswers) {
//            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse();
//            answerDetailsResponse.setId(answer.getUuid());
//            answerDetailsResponse.setAnswerContent(answer.getAnswer());
//            answerDetailsList.add(answerDetailsResponse);
//        }
//
//        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsList, HttpStatus.OK) ;
//    }
@RequestMapping(method = RequestMethod.GET, path = "answer/all/{questionId}",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion (@RequestHeader("authorization") final String authorizationToken,
                                                                            @PathVariable("questionId") final String questionId) throws AuthorizationFailedException,
        InvalidQuestionException {
    List<AnswerDetailsResponse> answerDetailsResponseList = new ArrayList<AnswerDetailsResponse>();
    List<AnswerEntity> answerEntityList = answerService.performGetAllAnswersToQuestion(authorizationToken, questionId);
    if (answerEntityList != null && !answerEntityList.isEmpty()) {
        for (AnswerEntity answerEntity : answerEntityList) {
            answerDetailsResponseList.add(new AnswerDetailsResponse().id(answerEntity.getUuid())
                    .answerContent(answerEntity.getAnswer()).questionContent(answerEntity.getQuestion().getContent()));
        }
    }
    return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponseList, HttpStatus.OK);
}

}
