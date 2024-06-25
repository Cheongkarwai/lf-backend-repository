package com.lfhardware.shipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PgeonPointDTO {

    @JsonProperty("Sender_point")
    private List<PointDTO> senderPoints = new ArrayList<>();

    @JsonProperty("Receiver_point")
    private List<PointDTO> receiverPoints = new ArrayList<>();

}