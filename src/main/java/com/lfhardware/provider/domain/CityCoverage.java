package com.lfhardware.provider.domain;

import com.lfhardware.city.domain.City;
import com.lfhardware.state.domain.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_city_coverage")
public class CityCoverage {

    @EmbeddedId
    private CityCoverageId cityCoverageId = new CityCoverageId();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "city_id")
    @MapsId("cityId")
    private City city;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_provider_id")
    @MapsId("serviceProviderId")
    private ServiceProvider serviceProvider;
}
