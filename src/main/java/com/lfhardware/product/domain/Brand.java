package com.lfhardware.product.domain;

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
@AllArgsConstructor
@Entity
@Table(name = "tbl_brand")
public class Brand extends BasicNamedAttribute {

    @OneToMany(mappedBy = "brand")
    private Set<Product> products;
}
