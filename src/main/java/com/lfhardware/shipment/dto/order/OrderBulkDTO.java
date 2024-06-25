package com.lfhardware.shipment.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.shipment.dto.BulkDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"date_coll"})
public class OrderBulkDTO extends BulkDTO {

    private String content;

    private BigDecimal value;

    @JsonProperty("service_id")
    private String serviceId;

    @JsonProperty("pick_point")
    private String pickPoint;

    @JsonProperty("pick_name")
    private String pickName;

    @JsonProperty("pick_email")
    private String pickEmail;

    @JsonProperty("pick_company")
    private String pickCompany;

    @JsonProperty("pick_contact")
    private String pickContact;

    @JsonProperty("pick_mobile")
    private String pickMobile;

    @JsonProperty("pick_addr1")
    private String pickAddress1;

    @JsonProperty("pick_addr2")
    private String pickAddress2;

    @JsonProperty("pick_addr3")
    private String pickAddress3;

    @JsonProperty("pick_addr4")
    private String pickAddress4;

    @JsonProperty("pick_city")
    private String pickCity;

    @JsonProperty("send_point")
    private String sendPoint;

    @JsonProperty("send_name")
    private String sendName;

    @JsonProperty("send_company")
    private String sendCompany;

    @JsonProperty("send_contact")
    private String sendContact;

    @JsonProperty("send_mobile")
    private String sendMobile;

    @JsonProperty("send_addr1")
    private String sendAddress1;

    @JsonProperty("send_addr2")
    private String sendAddress2;

    @JsonProperty("send_addr3")
    private String sendAddress3;

    @JsonProperty("send_addr4")
    private String sendAddress4;

    @JsonProperty("send_city")
    private String sendCity;

    @JsonProperty("collect_date")
    private LocalDate collectDate;

    private boolean sms;

    @JsonProperty("send_email")
    private String sendEmail;

    @JsonProperty("hs_code")
    private String hsCode;

    @JsonProperty("REQ_ID")
    private String reqId;

    @JsonProperty("reference")
    private String parcelReference;

    @JsonProperty("cod_enabled")
    private Boolean codEnabled;

    @JsonProperty("cod_amount")
    private BigDecimal codAmountl;

    @JsonProperty("taxDuty")
    private String taxDuty;

    @JsonProperty("parcel_category_id")
    private String parcelCategoryIdl;

    @JsonProperty("addon_insurance_enabled")
    private Boolean addOnInsuranceEnabled;

    @JsonProperty("addon_whatsapp_tracking_enabled")
    private Boolean addOnWhatsappTrackingEnabled;
}
