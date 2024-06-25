package com.lfhardware.form.domain;

import com.lfhardware.city.domain.City;
import com.lfhardware.provider.domain.ServiceProvider;
import com.lfhardware.provider_business.domain.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_form")
public class Form {


    @EmbeddedId
    private FormId formId = new FormId();

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "service_provider_id")
//    @MapsId("serviceProviderId")
//    private ServiceProvider serviceProvider;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_id")
    @MapsId("serviceId")
    private Service service;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> configuration;

}
