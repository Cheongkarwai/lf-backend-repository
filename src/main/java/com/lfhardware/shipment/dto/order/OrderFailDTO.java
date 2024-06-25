package com.lfhardware.shipment.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFailDTO {

    private String reference;

    private String status;

    private String remarks;
}
