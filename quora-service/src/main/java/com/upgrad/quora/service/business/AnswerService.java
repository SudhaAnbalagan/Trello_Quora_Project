package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AnswerService {

@Autowired
private AnswerDao answerDao;

@Autowired
private QuestionDao questionDao;

@Autowired
private QuestionBusinessService  questionBusinessService;

@Autowired
private UserDao userDao;

@Autowired
private UserBusinessService userBusinessService;


    public AnswerEntity getAnswerId(String id) throws InvalidQuestionException {
        AnswerEntity answer = answerDao.getAnswerById(id);

        if(answer == null) {
            throw new InvalidQuestionException("ANS-001", "Entered answer uuid does not exist");
        }

        return answer;
    }


@Transactional
public AnswerEntity createAnswer(final String questionId, final String authorization,String  answerRequest) throws InvalidQuestionException, AuthorizationFailedException {
    AnswerEntity answerEntity = new AnswerEntity();
    UserAuthEntity userAuthTokenEntity = userDao.getUserByAccessToken(authorization);




    if (userAuthTokenEntity == null) {
        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }
    if (userAuthTokenEntity.getLogoutAt() != null) {
        throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
    }
    QuestionEntity questionEntity = questionDao.getQuestionById(questionId);


    if (questionEntity != null) {
        UsersEntity userEntity = userAuthTokenEntity.getUser();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAnswer(answerRequest);
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setQuestion(questionEntity);
        answerEntity.setUser(userEntity);
        answerDao.createAnswer(answerEntity);

        return answerEntity;
    } else {
        throw new InvalidQuestionException("QUES-001","The question entered is invalid");
    }

}

    private Boolean isAnswerOwner(UserAuthEntity userAuthEntity, AnswerEntity answerEntity) throws AuthorizationFailedException {
        if(answerEntity.getUser().getUuid().equals(userAuthEntity.getUser().getUuid()))
            return true;
        else
            return false;
    }
    @Transactional
    public String editAnswer(String uuid, String answerContent, String accessToken) throws InvalidQuestionException, AuthorizationFailedException {

        AnswerEntity answer = getAnswerId(uuid);
        UserAuthEntity userAuthByToken = userDao.getUserByAccessToken(accessToken);

        if(userAuthByToken == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuthByToken.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }

        if(isAnswerOwner(userAuthByToken,answer)) {
            answerDao.editAnswer(uuid, answerContent);
            return uuid;
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }

    }



    @Transactional
    public String deleteAnswer(String uuid, String accessToken) throws AuthorizationFailedException, InvalidQuestionException {

        AnswerEntity answer = getAnswerId(uuid);
        UserAuthEntity userAuthEntity = userBusinessService.getUserByToken(accessToken);

        UserAuthEntity userAuthByToken = userDao.getUserByAccessToken(accessToken);

        if(userAuthByToken == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userAuthByToken.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete an answer");
        }
        if(isAnswerOwner(userAuthEntity,answer) ) {
            answerDao.deleteAnswer(uuid);
            return uuid;
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }

    }

public List<AnswerEntity> performGetAllAnswersToQuestion(final String authorizationToken, String questionId) throws AuthorizationFailedException, InvalidQuestionException {
    UserAuthEntity userAuthTokenEntity = userBusinessService.getUserByToken(authorizationToken);
    if (userAuthTokenEntity != null) {
        System.out.println(userAuthTokenEntity.getLogoutAt());
        if (userAuthTokenEntity.getLogoutAt() == null) {
            QuestionEntity questionEntity = questionBusinessService.getQuestionById(questionId);
            if (questionEntity != null) {
             //Even if the question is valid and there are no answers to the question we are deliberately
        //not responding with a message that there are no answers as it is not mentioned in requirements
                return answerDao.getAllAnswersByQuestion(questionEntity.getId());
            } else {
                throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
            }
        } else {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get the answers");
        }
    } else {
        throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }
}




}
