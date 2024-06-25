package com.lfhardware.order.repository;

import com.lfhardware.order.domain.OrderDetails;
import com.lfhardware.shared.CrudRepository;
import org.hibernate.reactive.stage.Stage;

public interface IOrderDetailsRepository extends CrudRepository<OrderDetails,Long> {

}
