package com.lfhardware.provider.domain;

//import com.lfhardware.service_provider.domain.Coverage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.appointment.domain.Appointment;
import com.lfhardware.auth.domain.Address;
import com.lfhardware.country.domain.Country;
import com.lfhardware.form.domain.Form;
import com.lfhardware.provider_business.domain.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_service_provider")
@NamedQueries({
        @NamedQuery(name = "ServiceProviderDetails.findById", query = "FROM ServiceProvider sp LEFT JOIN FETCH sp.cityCoverages cc  LEFT JOIN FETCH sp.stateCoverages sc " +
                "LEFT JOIN FETCH sp.countryCoverages LEFT JOIN FETCH sp.serviceDetails sd LEFT JOIN FETCH sd.service  WHERE sp.id = :id")
})
@NamedEntityGraphs({
        @NamedEntityGraph(name = "serviceProvider",includeAllAttributes = true)
})
public class ServiceProvider {

    @Id
    private String id;

    @Column(name = "business_name")
    private String name;

    @Column(name = "business_email_address")
    private String emailAddress;

    @Column(name = "business_phone_number")
    private String phoneNumber;

    @Column(name = "business_fax_no")
    private String faxNo;

    @Column(name = "business_location")
    private String location;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="addressLine1",
            column = @Column(name = "business_address_line_1")),
            @AttributeOverride(name="addressLine2",
                    column = @Column(name = "business_address_line_2")),
            @AttributeOverride(name="state",
                    column=@Column(name="business_state")),
            @AttributeOverride(name="zipcode",
                    column=@Column(name="business_zipcode")),
            @AttributeOverride(name="city",
                    column = @Column(name = "business_city"))
    })
    private Address address;

    @Column(name = "business_description")
    private String businessDescription;

    @OneToMany(cascade =  {CascadeType.ALL}, mappedBy = "serviceProvider", orphanRemoval = true)
    private Set<ServiceDetails> serviceDetails = new HashSet<>();

//    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    @JoinColumn(name = "coverage_id")
//    private Coverage coverage;

    @OneToMany( cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "serviceProvider")
    private Set<CityCoverage> cityCoverages = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "serviceProvider", orphanRemoval = true)
    private Set<StateCoverage> stateCoverages = new HashSet<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "serviceProvider")
    private Set<CountryCoverage> countryCoverages = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "album_id")
    private Album album;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private Set<BankingDetails> bankingDetails = new HashSet<>();

    @Embedded
    private SocialMediaLink socialMediaLink;

    @Column(name = "is_verified")
    private boolean isVerified;

    private double rating;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "stripe_account_id")
    private String stripeAccountId;

    private String ssm;

    @Column(name="front_identity_card")
    private String frontIdentityCard;

    @Column(name="back_identity_card")
    private String backIdentityCard;

    @Column(name = "business_profile_image")
    private String businessProfileImage;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "serviceProvider")
    private Set<ServiceProviderReview> reviews = new HashSet<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "serviceProvider")
    private List<Appointment> appointments = new ArrayList<>();
//    public void setCoverage(Coverage coverage) {
//        this.coverage = coverage;
//        this.coverage.setServiceProvider(this);
//    }

    public void setAlbum(Album album) {
        this.album = album;
        this.album.setServiceProvider(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceProvider that = (ServiceProvider) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addBankingDetails(BankingDetails bankingDetails) {
        this.bankingDetails.add(bankingDetails);
        bankingDetails.setServiceProvider(this);
    }

    public void addServiceDetails(ServiceDetails serviceDetails) {
        this.serviceDetails.add(serviceDetails);
        serviceDetails.setServiceProvider(this);
    }

    public void addServiceDetailsList(Set<ServiceDetails> serviceDetailsList) {
        for (ServiceDetails serviceDetails : serviceDetailsList) {
            this.serviceDetails.add(serviceDetails);
            serviceDetails.setServiceProvider(this);
        }
    }

    public void addStateCoverages(Set<StateCoverage> stateCoverages) {
        for (StateCoverage stateCoverage : stateCoverages) {
            this.stateCoverages.add(stateCoverage);
            stateCoverage.setServiceProvider(this);
        }
    }


    public void addCountryCoverages(List<CountryCoverage> countryCoverages) {
        for (CountryCoverage countryCoverage : countryCoverages) {
            this.countryCoverages.add(countryCoverage);
            countryCoverage.setServiceProvider(this);
        }
    }

    public void addCityCoverages(List<CityCoverage> cityCoverages) {
        for (CityCoverage cityCoverage : cityCoverages) {
            this.cityCoverages.add(cityCoverage);
            cityCoverage.setServiceProvider(this);
        }
    }


}
