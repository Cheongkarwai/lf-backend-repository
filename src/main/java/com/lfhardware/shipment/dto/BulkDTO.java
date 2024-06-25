package com.lfhardware.shipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BulkDTO {

    protected Double weight;

    protected Double width;

    protected Double length;

    protected Double height;

    @JsonProperty("pick_code")
    protected String pickCode;

    @JsonProperty("pick_state")
    protected String pickState;

    @JsonProperty("pick_country")
    protected String pickCountry;

    @JsonProperty("send_code")
    protected String sendCode;

    @JsonProperty("send_state")
    protected String sendState;

    @JsonProperty("send_country")
    protected String sendCountry;

    @JsonProperty("date_coll")
    protected LocalDate dateCollect;
//    String content, BigDecimal value, @JsonProperty("service_id") String serviceId,
//    @JsonProperty("pick_point") String pickPoint, @JsonProperty("pick_name") String pickName,
//    @JsonProperty("pick_company") String pickCompany, @JsonProperty("pick_contact") String pickContact,
//    @JsonProperty("pick_mobile") String pickMobile, @JsonProperty("pick_addr1") String pickAddress1,
//    @JsonProperty("pick_addr2") String pickAddress2, @JsonProperty("pick_addr3") String pickAddress3,
//    @JsonProperty("pick_addr4") String pickAddress4, @JsonProperty("pick_city") String pickCity,
//    @JsonProperty("pick_state") String pickState, @JsonProperty("pick_code") String pickCode,
//    @JsonProperty("pick_country") String pickCountry, @JsonProperty("send_point") String sendPoint,
//    @JsonProperty("send_name") String sendName, @JsonProperty("send_company") String sendCompany,
//    @JsonProperty("send_contact") String sendContact, @JsonProperty("send_mobile") String sendMobile,
//    @JsonProperty("send_addr1") String sendAddress1, @JsonProperty("send_addr2") String sendAddress2,
//    @JsonProperty("send_addr3") String sendAddress3, @JsonProperty("send_addr4") String sendAddress4,
//    @JsonProperty("send_city") String sendCity, @JsonProperty("send_state") String sendState,
//    @JsonProperty("send_code") String sendCode, @JsonProperty("send_country") String sendCountry,
//    @JsonProperty("collect_date")LocalDate collectDate, boolean sms, @JsonProperty("send_email") String sendEmail,
//    @JsonProperty("hs_code") String hsCode, @JsonProperty("REQ_ID") String reqId, @JsonProperty("reference")
//    String parcelReference, @JsonProperty("cod_enabled") Boolean codEnabled,
//    @JsonProperty("cod_amount") BigDecimal codAmount, @JsonProperty("taxDuty") String taxDuty,
//    @JsonProperty("parcel_category_id") String parcelCategoryId, @JsonProperty("addon_insurance_enabled")
//    Boolean addOnInsuranceEnabled, @JsonProperty("addon_whatsapp_tracking_enabled") Boolean addOnWhatsappTrackingEnabled
}
