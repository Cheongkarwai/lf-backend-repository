package com.lfhardware.customer.repository;

import com.lfhardware.customer.domain.Customer;
import com.lfhardware.customer.dto.CustomerCountGroupByDayDTO;
import com.lfhardware.provider.dto.ServiceProviderCountGroupByDayDTO;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.PageRepository;
import com.lfhardware.shared.PageRequestPredicateBuilder;
import com.lfhardware.shared.SortOrder;
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
public class CustomerRepository extends PageRepository implements ICustomerRepository{

    private final Stage.SessionFactory sessionFactory;

    public CustomerRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public CompletionStage<List<Customer>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<Customer> findById(Stage.Session session, String s) {
        return session.find(Customer.class, s);
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Customer obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<Customer>> findAllByIds(Stage.Session session, List<String> strings) {
        return null;
    }

    @Override
    public CompletionStage<Customer> merge(Stage.Session session, Customer obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, String s) {
        return null;
    }

    @Override
    public Customer loadReferenceById(Stage.Session session, String s) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Customer> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Customer> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Customer obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<String> strings) {
        return null;
    }

    @Override
    public CompletionStage<List<Customer>> findAll(Stage.Session session, PageInfo pageRequest) {

        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> root = cq.from(Customer.class);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, criteriaBuilder, root, cq);

        cq.where(predicates.toArray(Predicate[]::new));
        cq.select(root);
        return session.createQuery(cq).setFirstResult(pageRequest.getPage() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<Long> count(Stage.Session session, PageInfo pageRequest) {

        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Customer> root = cq.from(Customer.class);

        List<Predicate> predicates = super.buildPageRequestPredicates(pageRequest, criteriaBuilder, root, cq);

        cq.where(predicates.toArray(new Predicate[0]));
        cq.select(criteriaBuilder.count(root));

        return session.createQuery(cq).getSingleResult();
    }

    @Override
    public CompletionStage<List<CustomerCountGroupByDayDTO>> countCustomerGroupByDay(Stage.Session session, Integer day) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime daysBefore = today.minusDays(day);

        return session.createQuery("SELECT extract(day from u.createdAt), COUNT(u) " +
                        "FROM Customer u " +
                        "WHERE u.createdAt BETWEEN :startDate " +
                        "AND :endDate " +
                        "GROUP BY extract(day from u.createdAt)", Object[].class)
                .setParameter("startDate", daysBefore)
                .setParameter("endDate", today)
                .getResultList().thenApply(objects -> {

                    List<CustomerCountGroupByDayDTO> customerCountList = new ArrayList<>();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

                    first:
                    for (LocalDateTime date = daysBefore; date.isBefore(today); date = date.plusDays(1)) {

                        second:
                        for(Object[] object : objects){
                            if(String.valueOf(object[0]).equals(String.valueOf(date.getDayOfMonth()))){
                                customerCountList.add(new CustomerCountGroupByDayDTO(date.toLocalDate().format(formatter), Integer.parseInt(String.valueOf(object[1]))));

                                continue first;
                            }
                        }
                        customerCountList.add(new CustomerCountGroupByDayDTO(date.toLocalDate().format(formatter), 0));
                    }
                    return customerCountList;
                });
    }
}
