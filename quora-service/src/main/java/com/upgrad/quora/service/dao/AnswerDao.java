package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {
    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }
    public AnswerEntity getAnswerById(final String id) {
        try {
            return entityManager.createNamedQuery("getAnswerById", AnswerEntity.class)
                    .setParameter("uuid", id)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    public void editAnswer(String uuid, String content) {
        try {
            entityManager.createNamedQuery("editAnswerById")
                    .setParameter("uuid", uuid)
                    .setParameter("answer", content)
                    .executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteAnswer(String uuid) {
        try {
            entityManager.createNamedQuery("deleteAnswerById")
                    .setParameter("uuid", uuid)
                    .executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public List<AnswerEntity> getAllAnswersByQuestion(final Integer id) {
        try {
            List answersList = entityManager.createNamedQuery("getAllAnswersByUser")
                    .setParameter("id", id)
                    .getResultList();

            System.out.println(answersList.size());

            return answersList;
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
