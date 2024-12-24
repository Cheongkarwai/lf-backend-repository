package com.lfhardware.shipment.repository;

import com.lfhardware.core.repository.CrudRepository;
import com.lfhardware.shipment.domain.ShippingDetails;

public interface IShipmentRepository extends CrudRepository<ShippingDetails, Long> {
}
