package com.lfhardware.shipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateDTO {

    @JsonProperty("rate_id")
    private String rateId;

    @JsonProperty("service_detail")
    private String serviceDetail;

    @JsonProperty("service_id")
    private String serviceId;

    @JsonProperty("service_type")
    private String serviceType;

    @JsonProperty("courier_id")
    private String courierId;

    @JsonProperty("courier_logo")
    private String courierLogo;

    @JsonProperty("scheduled_start_date")
    private String scheduledStartDate;

    @JsonProperty("pickup_date")
    private LocalDate  pickupDate;

    @JsonProperty("delivery")
    private String deliveryDay;

    @JsonProperty("price")
    private BigDecimal totalPrice;

    @JsonProperty("addon_price")
    private BigDecimal addOnPrice;

    @JsonProperty("shipment_price")
    private BigDecimal shipmentPrice;

    @JsonProperty("require_min_order")
    private int minOrder;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("courier_name")
    private String courierName;

    @JsonProperty("dropoff_point")
    private List<PointDTO> dropoffPoints = new ArrayList<>();

    @JsonProperty("pickup_point")
    private List<PointDTO> pickupPoints = new ArrayList<>();

    @JsonProperty("cod_service_available")
    private boolean isCodServiceAvailable;

    @JsonProperty("cod_service_min_cod_amount")
    private BigDecimal codServiceMinCodAmount;

    @JsonProperty("cod_service_max_cod_amount")
    private BigDecimal codServiceMaxCodAmount;

    @JsonProperty("cod_charges_calculation")
    private Object codChargesCalculation;

    @JsonProperty("basic_insurance_max_value")
    private int basicInsuranceMaxValue;

    @JsonProperty("basic_insurance_currency")
    private String basicInsuranceCurrency;

    @JsonProperty("covered_by_insure_plus")
    private boolean isCoveredByInsurePlus;

    @JsonProperty("addon_insurance_available")
    private boolean addonInsuranceAvailable;

    @JsonProperty("whatsapp_tracking_available")
    private boolean isWhatsappTrackingAvailable;

    @JsonProperty("whatsapp_tracking_price")
    private BigDecimal whatsappTrackingPrice;

}
