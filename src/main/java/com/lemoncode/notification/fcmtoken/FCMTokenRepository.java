package com.lemoncode.notification.fcmtoken;


import com.lemoncode.notification.fcmtoken.FCMToken_;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class FCMTokenRepository {

    @PersistenceContext
    EntityManager entityManager;


    @Transactional
    public FCMToken save(FCMToken fcmToken) {

        if (fcmToken.getId() == null) {
            fcmToken.setCreationDate(LocalDate.now());
            this.entityManager.persist(fcmToken);
            return fcmToken;
        } else {
            return this.entityManager.merge(fcmToken);
        }
    }


    @Transactional
    public int deleteByToken(String token) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create delete
        CriteriaDelete<FCMToken> delete = cb.createCriteriaDelete(FCMToken.class);
        // set the root class
        Root<FCMToken> root = delete.from(FCMToken.class);
        // set where clause
        delete.where(cb.equal(root.get(FCMToken_.token), token));
        // perform update
        return this.entityManager.createQuery(delete).executeUpdate();
    }

    public List<FCMToken> findAll() {
        try {
            this.entityManager.clear();
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<FCMToken> q = cb.createQuery(FCMToken.class);
            Root<FCMToken> variableRoot = q.from(FCMToken.class);
            q.select(variableRoot);
            return this.entityManager.createQuery(q).getResultList();
        } catch (Exception e) {
            log.error("ERror retrieving all tokens");
            return new ArrayList<>();
        }
    }

    public FCMToken findByUsernameAndToken(String username, String fcmToken) {
        try {
            this.entityManager.clear();
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<FCMToken> query = cb.createQuery(FCMToken.class);
            // set the root class
            Root<FCMToken> root = query.from(FCMToken.class);

            Predicate usernameMatch = cb.equal(root.get(FCMToken_.username), username);
            Predicate tokenMatch = cb.equal(root.get(FCMToken_.token), fcmToken);

            query.where(cb.and(usernameMatch, tokenMatch));
            //perform query
            return this.entityManager.createQuery(query).getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }
}
