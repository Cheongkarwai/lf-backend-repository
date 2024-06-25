package com.lfhardware.shipment.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.shipment.dto.ApiInput;
import com.lfhardware.shipment.dto.BulkDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInput extends ApiInput {

    private List<OrderBulkDTO> bulk;

}
