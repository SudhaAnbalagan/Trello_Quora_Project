package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.AdminDao;
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
public class AdminBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AdminDao adminDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UsersEntity deleteUser(final String userUuid, final String authorizationToken) throws AuthorizationFailedException,UserNotFoundException {

        UserAuthEntity userAuthTokenEntity = userDao.getUserByAccessToken(authorizationToken);
        if(userAuthTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthTokenEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }

        UsersEntity usersEntity = userDao.getUserByUuid(userUuid);
        if(usersEntity.getRole().equals("nonadmin")){
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
        }

        if(usersEntity==null){
            throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
        }

        adminDao.deleteUserByUuid(userUuid);
        return usersEntity;
    }
}
