package com.lfhardware.auth.domain;

import com.lfhardware.core.dto.CommonConstant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@NamedEntityGraph(
        name = "user-role-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "userRoles", subgraph = "roles-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(name = "roles-subgraph", attributeNodes = {
                        @NamedAttributeNode("role")
                })
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = CommonConstant.TBL_USER)
public class User {

    @Id
    private String username;

    @Column(name = "mfa_enabled")
    private boolean mfaEnabled;

    @Column(name = "stripe_id")
    private String stripeId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "emailAddress",
                    column = @Column(name = "email_address", unique = true)),
            @AttributeOverride(name = "phoneNumber",
                    column = @Column(name = "phone_number", unique = true))
    })
    private Profile profile;

    @Singular
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void addUserRole(UserRole userRole) {
        userRoles.add(userRole);
    }

}
