package com.lemoncode.manga;


import com.lemoncode.util.UrlNormalizer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class MangaRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Manga> findAll() {
        this.entityManager.clear();
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create query
        CriteriaQuery<Manga> q = cb.createQuery(Manga.class);
        // set the root class
        Root<Manga> root = q.from(Manga.class);
        //perform query
        q.orderBy(cb.desc(root.get(Manga_.hasUpdate)), cb.asc(root.get(Manga_.title)));

        return this.entityManager.createQuery(q)
                .getResultList();
    }


    @Transactional
    public Manga save(Manga m) {

        //TODO: there must be ab beter way to normalize url before saving
        Manga manga = UrlNormalizer.normalize(m);

        if (manga.getId() == null) {
            this.entityManager.persist(manga);
            return manga;
        } else {
            return this.entityManager.merge(manga);
        }
    }


    @Transactional
    public int updateLastChapter(String id, String lastChapter) {
//        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
//        // create update
//        CriteriaUpdate<Post> delete = cb.createCriteriaUpdate(Post.class);
//        // set the root class
//        Root<Post> root = delete.from(Post.class);
//        // set where clause
//        delete.set(root.get(Post_.status), status);
//        delete.where(cb.equal(root.get(Post_.id), id));
//        // perform update
//        return this.entityManager.createQuery(delete).executeUpdate();
        return 0;
    }

    @Transactional
    public int deleteById(String id) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create delete
        CriteriaDelete<Manga> delete = cb.createCriteriaDelete(Manga.class);
        // set the root class
        Root<Manga> root = delete.from(Manga.class);
        // set where clause
        delete.where(cb.equal(root.get(Manga_.id), id));
        // perform update
        return this.entityManager.createQuery(delete).executeUpdate();
    }

    public List<Manga> findNew() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create query
        CriteriaQuery<Manga> query = cb.createQuery(Manga.class);
        // set the root class
        Root<Manga> root = query.from(Manga.class);

        query.where(cb.equal(root.get(Manga_.hasUpdate), true));
        //perform query
        return this.entityManager.createQuery(query).getResultList();
    }

    public Manga findById(Long id) {
        try {
            this.entityManager.clear();
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<Manga> query = cb.createQuery(Manga.class);
            // set the root class
            Root<Manga> root = query.from(Manga.class);

            query.where(cb.equal(root.get(Manga_.id), id));
            //perform query
            return this.entityManager.createQuery(query).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Manga> findNoUpdatesAndDoneReadAndNotEnded() {
        this.entityManager.clear();
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create query
        CriteriaQuery<Manga> query = cb.createQuery(Manga.class);
        // set the root class
        Root<Manga> root = query.from(Manga.class);


        Predicate noUpdate = cb.equal(root.get(Manga_.hasUpdate), false);
        Predicate doneRead = cb.equal(root.get(Manga_.doneRead), true);
        Predicate notEnded = cb.equal(root.get(Manga_.ended), false);

        query.where(cb.and(noUpdate, doneRead, notEnded));
        //perform query
        return this.entityManager.createQuery(query).getResultList();
    }

    public Manga findByTitle(String title) {
        try {
            this.entityManager.clear();
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            // create query
            CriteriaQuery<Manga> query = cb.createQuery(Manga.class);
            // set the root class
            Root<Manga> root = query.from(Manga.class);

            query.where(cb.equal(root.get(Manga_.title), title));
            //perform query
            return this.entityManager.createQuery(query).getSingleResult();
        } catch (Exception e) {
            return null;
        }

    }

    public List<Manga> findOngoing() {
        this.entityManager.clear();
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create query
        CriteriaQuery<Manga> query = cb.createQuery(Manga.class);
        // set the root class
        Root<Manga> root = query.from(Manga.class);

        Predicate doneRead = cb.equal(root.get(Manga_.doneRead), false);

        query.where(doneRead);
        //perform query
        return this.entityManager.createQuery(query).getResultList();
    }

    public List<Manga> findEnded() {
        this.entityManager.clear();
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        // create query
        CriteriaQuery<Manga> query = cb.createQuery(Manga.class);
        // set the root class
        Root<Manga> root = query.from(Manga.class);

        Predicate doneRead = cb.equal(root.get(Manga_.doneRead), true);

        query.where(doneRead);
        //perform query
        return this.entityManager.createQuery(query).getResultList();
    }
}