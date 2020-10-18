package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonBusinessService {


    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UsersEntity getUser(final String userUuid, final String authorizationToken) throws AuthorizationFailedException , UserNotFoundException {

        UsersEntity usersEntity = userDao.getUserByUuid(userUuid);
        if(usersEntity == null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }

        UserAuthEntity userAuthTokenEntity = userDao.getUserByAccessToken(authorizationToken);
        if(userAuthTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthTokenEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }

        return usersEntity;

    }
}


