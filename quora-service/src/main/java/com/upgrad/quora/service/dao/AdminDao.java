package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.UsersEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AdminDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void deleteUserByUuid(final String uuid) {
        try {
            entityManager.createNamedQuery("deleteUserById", UsersEntity.class)
                    .setParameter("uuid", uuid)
                    .executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
