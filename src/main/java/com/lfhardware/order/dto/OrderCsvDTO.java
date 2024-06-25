package com.lfhardware.order.dto;

import com.lfhardware.order.domain.DeliveryStatus;
import com.lfhardware.order.domain.PaymentStatus;
import com.lfhardware.stock.domain.Size;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCsvDTO {

    @CsvBindByName
    private Long orderId;

    @CsvBindByName
    private BigDecimal shippingFees;

    @CsvBindByName
    private BigDecimal subtotal;

    @CsvBindByName
    private BigDecimal total;

    @CsvBindByName
    private String recipientFirstName;

    @CsvBindByName
    private String recipientLastName;

    @CsvBindByName
    private String recipientPhoneNumber;

    @CsvBindByName
    private String recipientEmailAddress;

    @CsvBindByName
    private String recipientAddressLine1;

    @CsvBindByName
    private String recipientAddressLine2;

    @CsvBindByName
    private String state;

    @CsvBindByName
    private String city;

    @CsvBindByName
    private String zipcode;

    @CsvBindByName
    private String shippingId;

    @CsvBindByName
    private DeliveryStatus deliveryStatus;

    @CsvBindByName
    private PaymentStatus paymentStatus;

    @CsvBindByName
    private String itemName;

    @CsvBindByName
    private String itemQuantity;

    @CsvBindByName
    private Size itemSize;

    @CsvBindByName
    private BigDecimal itemPrice;
}
