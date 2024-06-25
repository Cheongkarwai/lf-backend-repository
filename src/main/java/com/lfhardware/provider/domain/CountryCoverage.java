package com.lfhardware.provider.domain;

import com.lfhardware.country.domain.Country;
import com.lfhardware.state.domain.State;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_country_coverage")
public class CountryCoverage {

    @EmbeddedId
    private CountryCoverageId countryCoverageId = new CountryCoverageId();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
    @MapsId("countryId")
    private Country country;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_provider_id")
    @MapsId("serviceProviderId")
    private ServiceProvider serviceProvider;
}
