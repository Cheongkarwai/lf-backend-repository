package com.lfhardware.form.authorization;

import com.lfhardware.authorization.dto.Domain;
import com.lfhardware.authorization.dto.Scope;
import com.lfhardware.authorization.manager.PermissionManager;
import com.lfhardware.form.domain.FormId;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class FormAuthorization {

    private final Enforcer enforcer;

    private final PermissionManager permissionManager;

    public FormAuthorization(Enforcer enforcer, PermissionManager permissionManager) {
        this.enforcer = enforcer;
        this.permissionManager = permissionManager;
    }

    public Mono<Boolean> authorizeGetForm(FormId formId) {
        return permissionManager.getName()
                .flatMap(name ->
                        Mono.just(enforcer.enforce(name, formId.getServiceProviderId() + "," + formId.getServiceId(), Scope.FORM_READ.getScope())));
    }
}
