package com.lfhardware.order.domain;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.reactive.id.ReactiveIdentifierGenerator;
import org.hibernate.reactive.session.ReactiveConnectionSupplier;

import java.util.concurrent.CompletionStage;

public class OrderIdGenerator implements ReactiveIdentifierGenerator<String> {

    private final String prefix = "000";


    @Override
    public CompletionStage generate(ReactiveConnectionSupplier reactiveConnectionSupplier, Object o) {
        return reactiveConnectionSupplier.getReactiveConnection().selectIdentifier("SELECT MAX(id) FROM tbl_order",new Object[]{},Integer.class)
                .thenApply(result-> {
                    if(result == null){
                        return prefix + 1;
                    }
                    return prefix + (result + 1);
                });
    }
}
