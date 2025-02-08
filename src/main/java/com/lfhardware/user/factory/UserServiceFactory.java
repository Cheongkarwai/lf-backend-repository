package com.lfhardware.user.factory;

import com.lfhardware.user.dto.UserType;
import com.lfhardware.user.service.IUserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

//@Component
//public class UserServiceFactory {
//
//    private final Map<UserType, IUserService> userServiceMap;
//
//    public UserServiceFactory(List<IUserService> userServices) {
//        userServiceMap = userServices
//                .parallelStream()
//                .collect(Collectors.toUnmodifiableMap(IUserService::getUserType, Function.identity()));
//    }
//
//    public <T> IUserService getUserService(UserType userType) {
//        return Optional.ofNullable(userServiceMap.get(userType)).orElseThrow(IllegalArgumentException::new);
//    }
//}
