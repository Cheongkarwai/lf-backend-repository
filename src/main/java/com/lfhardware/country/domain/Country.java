package com.lfhardware.country.domain;

import com.lfhardware.shared.BasicNamedAttribute;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "tbl_country")
@NamedQueries({
        @NamedQuery(name = "Country.findAllByNames", query = "SELECT u FROM Country u WHERE u.name IN (:names)")
})
public class Country extends BasicNamedAttribute {

}