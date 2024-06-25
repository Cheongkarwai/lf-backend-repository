package com.lfhardware.provider.domain;

import com.lfhardware.provider.converter.CoverageLocationConverter;
import com.lfhardware.provider.converter.StringListToStringConverter;
import com.lfhardware.state.domain.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name="tbl_coverage")
//public class Coverage {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "cities")
//    @Convert(converter = StringListToStringConverter.class)
//    private List<String> cities;
//
//    @Column(name = "countries")
//    @Convert(converter = StringListToStringConverter.class)
//    private List<String> countries;
//
//    @Column(name = "states")
//    @Convert(converter = StringListToStringConverter.class)
//    private List<String> states;
//
//    @OneToOne(mappedBy = "coverage", fetch = FetchType.LAZY)
//    private ServiceProvider serviceProvider;
//
//}
