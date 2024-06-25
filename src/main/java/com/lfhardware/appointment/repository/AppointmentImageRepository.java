package com.lfhardware.appointment.repository;

import com.lfhardware.appointment.domain.AppointmentImage;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class AppointmentImageRepository implements IAppointmentImageRepository{

    private final Stage.SessionFactory sessionFactory;
    public AppointmentImageRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    @Override
    public CompletionStage<List<AppointmentImage>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<AppointmentImage> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, AppointmentImage obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<AppointmentImage>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<AppointmentImage> merge(Stage.Session session, AppointmentImage obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public AppointmentImage loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<AppointmentImage> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<AppointmentImage> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, AppointmentImage obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }
}
