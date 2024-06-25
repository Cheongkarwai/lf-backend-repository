package com.lfhardware.provider.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_banking_details")
public class BankingDetails {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "account_number")
    private String accountNumber;

    @EmbeddedId
    private BankDetailsId bankDetailsId = new BankDetailsId();

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("serviceProviderId")
    @JoinColumn(name = "service_provider_id")
    private ServiceProvider serviceProvider;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_id")
    @MapsId("bankId")
    private Bank bank;



}
