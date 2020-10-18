package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class QuestionBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserBusinessService userBusinessService;

    public QuestionEntity getQuestionById(String id) throws InvalidQuestionException {
        QuestionEntity question = questionDao.getQuestionById(id);

        if(question == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        return question;
    }

    private Boolean isQuestionOwner(UserAuthEntity userAuthEntity, QuestionEntity questionEntity) throws AuthorizationFailedException {
        if(questionEntity.getUser().getUuid().equals(userAuthEntity.getUser().getUuid()))
            return true;
        else
            return false;
    }

    public List<QuestionEntity> getAllQuestionsByUser(final UsersEntity userId) {
        List<QuestionEntity> questionsList = questionDao.getAllQuestionsByUser(userId);

        return questionsList;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final String authorization,QuestionEntity questionEntity) throws AuthorizationFailedException {
        UserAuthEntity userAuthTokenEntity = userDao.getUserByAccessToken(authorization);

        if(userAuthTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        if(userAuthTokenEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }
        UsersEntity userEntity = userAuthTokenEntity.getUser();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUser(userEntity);
        return questionDao.createQuestion(questionEntity);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestion(final String authorization) throws AuthorizationFailedException {
        userBusinessService.getUserByToken(authorization);
        return questionDao.getAllQuestions();
    }

    @Transactional
    public String editQuestion(String uuid, String questionContent, String accessToken) throws InvalidQuestionException, AuthorizationFailedException {

        QuestionEntity question = getQuestionById(uuid);
        UserAuthEntity userAuthEntity = userBusinessService.getUserByToken(accessToken);

        if(isQuestionOwner(userAuthEntity,question)) {
            questionDao.editQuestion(uuid, questionContent);
            return uuid;
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }

    }

    @Transactional
    public String deleteQuestion(String uuid, String accessToken) throws InvalidQuestionException, AuthorizationFailedException {

        QuestionEntity question = getQuestionById(uuid);
        UserAuthEntity userAuthEntity = userBusinessService.getUserByToken(accessToken);

        String userRole = userAuthEntity.getUser().getRole();

        if(isQuestionOwner(userAuthEntity,question) || userRole.equals("admin")) {
            questionDao.deleteQuestion(uuid);
            return uuid;
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }

    }







}
