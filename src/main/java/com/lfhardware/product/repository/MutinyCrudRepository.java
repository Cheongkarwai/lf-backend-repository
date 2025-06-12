package com.lfhardware.product.repository;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;


import java.util.Collection;
import java.util.List;

public interface MutinyCrudRepository<T, ID> {

    Uni<List<T>> findAll(Mutiny.Session session);

    Uni<T> findById(Mutiny.Session session, ID id);


    Uni<Void> save(Mutiny.Session session, T obj);

    Uni<List<T>> findAllByIds(Mutiny.Session session, List<ID> ids);

    Uni<T> merge(Mutiny.Session session, T obj);

    Uni<Integer> deleteById(Mutiny.Session session, ID id);

    T loadReferenceById(Mutiny.Session session, ID id);

    Uni<Void> saveAll(Mutiny.Session session, Collection<T> objs);

    Uni<Void> deleteAll(Mutiny.Session session, List<T> objs);

    Uni<Void> delete(Mutiny.Session session, T obj);

    Uni<Integer> deleteAllByIds(Mutiny.Session session, List<ID> ids);

}

