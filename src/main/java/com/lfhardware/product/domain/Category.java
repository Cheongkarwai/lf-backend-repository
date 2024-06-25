package com.lfhardware.product.domain;

import com.lfhardware.shared.BasicNamedAttribute;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_category")
public class Category extends BasicNamedAttribute {

    @OneToMany(mappedBy = "category")
    private Set<Product> products;
}