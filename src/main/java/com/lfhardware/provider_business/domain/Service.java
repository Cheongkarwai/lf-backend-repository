package com.lfhardware.provider_business.domain;

import com.lfhardware.appointment.domain.Appointment;
import com.lfhardware.form.domain.Form;
import com.lfhardware.provider.domain.ServiceDetails;
import com.lfhardware.shared.BasicNamedAttribute;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_service")
public class Service extends BasicNamedAttribute {


    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "service")
    private Set<ServiceDetails> serviceDetails;

    @ManyToOne
    @JoinColumn(name = "service_category_id")
    private ServiceCategory serviceCategory;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "service")
    private List<Appointment> appointments = new ArrayList<>();
}
