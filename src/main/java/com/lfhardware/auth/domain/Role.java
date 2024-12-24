package com.lfhardware.auth.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name= "tbl_role")
@NamedQueries({
        @NamedQuery(name = "Role.findByName",query = "FROM Role r WHERE r.name = :name"),
        @NamedQuery(name = "Role.findAll", query = "FROM Role")
})
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "role")
    private Set<UserRole> userRoles = new HashSet<>();
}
