package com.lfhardware.provider_business.domain;

import com.lfhardware.shared.BasicNamedAttribute;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_service_category")
@NamedEntityGraphs({
        @NamedEntityGraph(name="ServiceCategory.services",
        attributeNodes = @NamedAttributeNode(value = "services"))
})
@NamedQueries({
        @NamedQuery(name = "ServiceCategory.findAll",query="FROM ServiceCategory")
})
public class ServiceCategory extends BasicNamedAttribute {

    @OneToMany(mappedBy = "serviceCategory")
    public Set<Service> services = new HashSet<>();
}
