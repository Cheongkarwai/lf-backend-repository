package com.lfhardware.auth.domain;

import com.lfhardware.shared.CommonConstant;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name= CommonConstant.TBL_ROLE)
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
