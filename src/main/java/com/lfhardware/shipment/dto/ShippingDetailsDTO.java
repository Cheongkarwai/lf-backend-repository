package com.lfhardware.shipment.dto;

import com.lfhardware.order.dto.RecipientDTO;

public record ShippingDetailsDTO(Long id, RecipientDTO recipient){}
