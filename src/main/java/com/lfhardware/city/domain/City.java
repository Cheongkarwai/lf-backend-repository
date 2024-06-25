package com.lfhardware.city.domain;

import com.lfhardware.shared.BasicNamedAttribute;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_city")
@NamedQueries({
        @NamedQuery(name = "City.findAllByNames", query = "SELECT u FROM City u WHERE u.name IN (:names)")
})
public class City extends BasicNamedAttribute {

}
