package com.lfhardware.auth.repository;

import com.lfhardware.auth.domain.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletionStage;

@Repository
public class UserRepository implements IUserRepository{

    private Stage.SessionFactory stagedSessionFactory;

    @PostConstruct
    public void init(){
        stagedSessionFactory = Persistence.createEntityManagerFactory("postgres").unwrap(Stage.SessionFactory.class);
    }
    @Override
    public CompletionStage<User> findById(Stage.Session session, String id) {
        return session.find(User.class,id);
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, User user) {
        return session.persist(user);
    }

    @Override
    public CompletionStage<User> findUserRoleById(Stage.Session session, String username) {
        EntityGraph<User> userEntityGraph = session.getEntityGraph(User.class,"user-role-graph");
        return session.find(userEntityGraph,username);
    }

    @Override
    public CompletionStage<User> findByEmailAddress(Stage.Session session, String emailAddress) {
        CriteriaBuilder criteriaBuilder = stagedSessionFactory.getCriteriaBuilder();
        CriteriaQuery<User> cq = criteriaBuilder.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root).where(criteriaBuilder.equal(root.get("profile").get("emailAddress"),emailAddress));
        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public CompletionStage<User> merge(Stage.Session session, User user) {
        return session.detach(user).merge(user);
    }
}
