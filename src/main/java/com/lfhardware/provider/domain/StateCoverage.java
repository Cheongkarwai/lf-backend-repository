package com.lfhardware.provider.domain;


import com.lfhardware.state.domain.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_state_coverage",
        uniqueConstraints ={
                @UniqueConstraint(columnNames={"state_id", "service__provider_id"})}
)
public class StateCoverage {

    @EmbeddedId
    private StateCoverageId stateCoverageId = new StateCoverageId();

    @ManyToOne
    @JoinColumn(name = "state_id")
    @MapsId("stateId")
    private State state;

    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    @MapsId("serviceProviderId")
    private ServiceProvider serviceProvider;

}
