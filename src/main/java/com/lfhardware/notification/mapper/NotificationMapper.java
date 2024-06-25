package com.lfhardware.notification.mapper;

import com.lfhardware.notification.domain.Notification;
import com.lfhardware.notification.domain.Notification_;
import com.lfhardware.notification.dto.NotificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "message", source = "message")
    NotificationDTO mapToNotificationDTO(Notification notification);


    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "userId", source = "userId")
    Notification mapToNotification(NotificationDTO notificationDTO);
}
