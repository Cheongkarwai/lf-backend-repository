package com.lfhardware.auth.service;

import com.lfhardware.auth.domain.Role;
import com.lfhardware.auth.dto.RoleDTO;
import com.lfhardware.auth.mapper.RoleMapper;
import com.lfhardware.auth.repository.IRoleRepository;
import com.lfhardware.configuration.KeycloakProperties;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;
import org.hibernate.reactive.stage.Stage;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    public final IRoleRepository roleRepository;

    private final Stage.SessionFactory sessionFactory;

    private final RoleMapper roleMapper;

    private final Keycloak keycloak;

    private final KeycloakProperties keycloakProperties;

    public RoleService(IRoleRepository roleRepository, Stage.SessionFactory sessionFactory,
                       RoleMapper roleMapper, Keycloak keycloak, KeycloakProperties keycloakProperties) {
        this.roleRepository = roleRepository;
        this.sessionFactory = sessionFactory;
        this.roleMapper = roleMapper;
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
    }

    public Mono<List<RoleDTO>> findAll() {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> roleRepository.findAll(session)
                .thenApply(roles -> roles.stream()
                        .map(roleMapper::mapToRoleDTO)
                        .collect(Collectors.toList()))));
    }

    public Mono<List<RoleRepresentation>> findByName(String name) {
        return Mono.fromCallable(() -> keycloak.realm(this.keycloakProperties.getRealm())
                .roles()
                .list()
                .stream()
                .filter(roleRepresentation -> roleRepresentation.getName()
                        .equals(name))
                .collect(Collectors.toList()));
    }


}
