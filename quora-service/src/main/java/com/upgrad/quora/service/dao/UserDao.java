package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UsersEntity createUser(UsersEntity usersEntity) {
        entityManager.persist(usersEntity);
        return usersEntity;
    }

    public UsersEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UsersEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UsersEntity getUserByUuid(String uuid) {
        try {
            UsersEntity userEntity = entityManager.createNamedQuery("userByUuid", UsersEntity.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
            return userEntity;
        } catch (Exception e) {
            return null;
        }
    }

    public UsersEntity getUser(final String userName) {
        try {
            return entityManager.createNamedQuery("userByUserName", UsersEntity.class).setParameter("userName", userName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthTokenEntity){
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    public void updateUser(final UsersEntity updatedUserEntity){
        entityManager.merge(updatedUserEntity);
    }

    public UserAuthEntity getUserByAccessToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userByAccessToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void updateUserAuth(final UserAuthEntity updatedUserEntity)
    {
        entityManager.merge(updatedUserEntity);
    }

    /*public void deleteUser(final UsersEntity usersEntity){
        entityManager.remove(usersEntity);
    }*/



}

