package com.lfhardware.appointment.repository;

import com.lfhardware.appointment.domain.*;
import com.lfhardware.appointment.dto.AppointmentCountGroupByDayDTO;
import com.lfhardware.appointment.repository.predicate.AppointmentPredicateBuilder;
import com.lfhardware.provider.dto.ServiceProviderAppointmentCountGroupByDayDTO;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.PageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

@Repository
public class AppointmentRepository extends PageRepository implements IAppointmentRepository {

    private final Stage.SessionFactory sessionFactory;

    private final EntityManager entityManager;

    public AppointmentRepository(Stage.SessionFactory sessionFactory, EntityManager entityManager) {
        this.sessionFactory = sessionFactory;
        this.entityManager = entityManager;
    }

    @Override
    public CompletionStage<List<Appointment>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<Appointment> findById(Stage.Session session, AppointmentId appointmentPK) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = criteriaBuilder.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);

        cq.select(root)
                .where(criteriaBuilder.equal(root.get(Appointment_.APPOINTMENT_ID), appointmentPK));
        ;
        return session.createQuery(cq)
                .setPlan(session.getEntityGraph(Appointment.class, "Appointment.appointmentImage"))
                .getSingleResultOrNull();
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Appointment obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<Appointment>> findAllByIds(Stage.Session session, List<AppointmentId> ids) {
        return session.find(Appointment.class, ids.toArray());
    }

    @Override
    public CompletionStage<Appointment> merge(Stage.Session session, Appointment obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, AppointmentId appointmentPK) {
        return null;
    }

    @Override
    public Appointment loadReferenceById(Stage.Session session, AppointmentId appointmentPK) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Appointment> objs) {
        return session.persist(objs.toArray());
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Appointment> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Appointment obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<AppointmentId> appointmentPKS) {
        return null;
    }

    @Override
    public CompletionStage<List<Appointment>> findAll(Stage.Session session, PageInfo pageRequest, List<String> status) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);
        root.fetch(Appointment_.APPOINTMENT_IMAGES, JoinType.LEFT);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, cb, root, cq);

        if (CollectionUtils.isNotEmpty(status)) {
            predicates.add(AppointmentPredicateBuilder.hasStatus(cb, root, status));
        }

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(root);

        return session.createQuery(cq)
                .setFirstResult(pageRequest.getPage() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<Long> count(Stage.Session session, PageInfo pageRequest, List<String> status) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Appointment> root = cq.from(Appointment.class);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, cb, root, cq);

        if (CollectionUtils.isNotEmpty(status)) {
            predicates.add(AppointmentPredicateBuilder.hasStatus(cb, root, status));
        }

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(cb.count(root));

        return session.createQuery(cq)
                .getSingleResult();
    }

    @Override
    public CompletionStage<List<Appointment>> findAllByServiceProviderId(Stage.Session session, PageInfo pageRequest, String serviceProviderId
            , List<String> status) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);
        root.fetch(Appointment_.APPOINTMENT_IMAGES, JoinType.LEFT);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, cb, root, cq);

        if (Objects.nonNull(serviceProviderId)) {
            predicates.add(AppointmentPredicateBuilder.hasServiceId(cb, root, serviceProviderId));
        }

        //Status
        if (CollectionUtils.isNotEmpty(status)) {
            predicates.add(AppointmentPredicateBuilder.hasStatus(cb, root, status));
        }

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(root);

        return session.createQuery(cq)
                .setFirstResult(pageRequest.getPage() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

    }

    @Override
    public CompletionStage<Long> countByServiceProviderId(Stage.Session session, PageInfo pageRequest, String serviceProviderId, List<String> status) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Appointment> root = cq.from(Appointment.class);
        root.join(Appointment_.APPOINTMENT_IMAGES, JoinType.LEFT);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, cb, root, cq);

        if (CollectionUtils.isNotEmpty(status)) {
            predicates.add(AppointmentPredicateBuilder.hasStatus(cb, root, status));
        }

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(cb.count(root));

        return session.createQuery(cq)
                .getSingleResult();
    }


    @Override
    public CompletionStage<List<Appointment>> findAllByCustomerId(Stage.Session session, PageInfo pageRequest, String customerId) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);
        root.fetch(Appointment_.APPOINTMENT_IMAGES, JoinType.LEFT);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, cb, root, cq);

        if (StringUtils.hasText(customerId)) {
            predicates.add(AppointmentPredicateBuilder.hasCustomerId(cb, root, customerId));
        }

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(root);

        return session.createQuery(cq)
                .setFirstResult(pageRequest.getPage() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<Long> countByCustomerId(Stage.Session session, PageInfo pageRequest, String customerId) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Appointment> root = cq.from(Appointment.class);
        root.fetch(Appointment_.APPOINTMENT_IMAGES, JoinType.LEFT);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, cb, root, cq);

        if (StringUtils.hasText(customerId)) {
            predicates.add(AppointmentPredicateBuilder.hasCustomerId(cb, root, customerId));
        }

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(cb.count(root));

        return session.createQuery(cq)
                .getSingleResult();
    }

    @Override
    public CompletionStage<List<Appointment>> findAllByCustomerIdAndBookingDateAndStatus(Stage.Session session, PageInfo pageRequest, String customerId, LocalDateTime bookingDatetime,
                                                                                         List<String> status) {

        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);
        root.fetch(Appointment_.APPOINTMENT_IMAGES, JoinType.LEFT);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, cb, root, cq);

        if (StringUtils.hasText(customerId)) {
            predicates.add(AppointmentPredicateBuilder.hasCustomerId(cb, root, customerId));
        }

        if (Objects.nonNull(bookingDatetime)) {
            predicates.add(AppointmentPredicateBuilder.hasBookingDateTime(cb, root, bookingDatetime));
        }
        if (CollectionUtils.isNotEmpty(status)) {
            predicates.add(AppointmentPredicateBuilder.hasStatus(cb, root, status));
        }

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(root);

        return session.createQuery(cq)
                .setFirstResult(pageRequest.getPage() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();
    }

    public CompletionStage<Long> countByCustomerIdAndBookingDateAndStatus(Stage.Session session, PageInfo pageRequest, String customerId, LocalDateTime bookingDateTime,
                                                                          List<String> status) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Appointment> root = cq.from(Appointment.class);
        //root.fetch(Appointment_.APPOINTMENT_IMAGES, JoinType.LEFT);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, cb, root, cq);

        if(StringUtils.hasText(customerId)) {
            predicates.add(AppointmentPredicateBuilder.hasCustomerId(cb, root, customerId));
        }

        if (Objects.nonNull(bookingDateTime)) {
            predicates.add(AppointmentPredicateBuilder.hasBookingDateTime(cb, root, bookingDateTime));
        }

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(cb.count(root));

        return session.createQuery(cq)
                .getSingleResult();
    }

    @Override
    public CompletionStage<List<ServiceProviderAppointmentCountGroupByDayDTO>> countAppointmentsByServiceProviderIdGroupByDay(Stage.Session session,
                                                                                                                              String serviceProviderId,
                                                                                                                              AppointmentStatus status,
                                                                                                                              Integer day) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime daysBefore = today.minusDays(day);

        return session.createQuery("SELECT extract(day from u.appointmentId.createdAt), COUNT(u) " +
                        "FROM Appointment u " +
                        "WHERE u.appointmentId.createdAt BETWEEN :startDate " +
                        "AND :endDate " +
                        "AND u.appointmentId.serviceProviderId = :serviceProviderId " +
                        "AND u.status = :status " +
                        "GROUP BY extract(day from u.appointmentId.createdAt)", Object[].class)
                .setParameter("startDate", daysBefore)
                .setParameter("endDate", today)
                .setParameter("serviceProviderId", serviceProviderId)
                .setParameter("status", status)
                .getResultList().thenApply(objects -> {

                    List<ServiceProviderAppointmentCountGroupByDayDTO> serviceProviderAppointmentCountList = new ArrayList<>();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

                    first:
                    for (LocalDateTime date = daysBefore; date.isBefore(today); date = date.plusDays(1)) {

                        second:
                        for(Object[] object : objects){
                            if(String.valueOf(object[0]).equals(String.valueOf(date.getDayOfMonth()))){
                                serviceProviderAppointmentCountList.add(new ServiceProviderAppointmentCountGroupByDayDTO(date.toLocalDate().format(formatter), Integer.parseInt(String.valueOf(object[1]))));

                                continue first;
                            }
                        }
                        serviceProviderAppointmentCountList.add(new ServiceProviderAppointmentCountGroupByDayDTO(date.toLocalDate().format(formatter), 0));
                    }
                    return serviceProviderAppointmentCountList;
                });
    }

    @Override
    public CompletionStage<List<AppointmentCountGroupByDayDTO>> countAppointmentsGroupByDay(Stage.Session session, AppointmentStatus appointmentStatus, Integer day) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime daysBefore = today.minusDays(day);

        return session.createQuery("SELECT extract(day from u.appointmentId.createdAt), COUNT(u) " +
                        "FROM Appointment u " +
                        "WHERE u.appointmentId.createdAt BETWEEN :startDate " +
                        "AND :endDate " +
                        "AND u.status = :status " +
                        "GROUP BY extract(day from u.appointmentId.createdAt)", Object[].class)
                .setParameter("startDate", daysBefore)
                .setParameter("endDate", today)
                .setParameter("status", appointmentStatus)
                .getResultList().thenApply(objects -> {

                    List<AppointmentCountGroupByDayDTO> appointmentCountList = new ArrayList<>();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

                    first:
                    for (LocalDateTime date = daysBefore; date.isBefore(today); date = date.plusDays(1)) {

                        second:
                        for(Object[] object : objects){
                            if(String.valueOf(object[0]).equals(String.valueOf(date.getDayOfMonth()))){
                                appointmentCountList.add(new AppointmentCountGroupByDayDTO(date.toLocalDate().format(formatter), Integer.parseInt(String.valueOf(object[1]))));

                                continue first;
                            }
                        }
                        appointmentCountList.add(new AppointmentCountGroupByDayDTO(date.toLocalDate().format(formatter), 0));
                    }
                    return appointmentCountList;
                });
    }

    @Override
    public CompletionStage<Appointment> findByCheckoutSessionId(Stage.Session session, String checkoutSessionId){
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);

        cq.select(root).where(cb.equal(root.get(Appointment_.CHECKOUT_SESSION_ID), checkoutSessionId));

        return session.createQuery(cq)
                .getSingleResultOrNull();
    }

}
