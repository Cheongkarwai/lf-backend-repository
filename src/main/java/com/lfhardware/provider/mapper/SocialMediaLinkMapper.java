package com.lfhardware.provider.mapper;

import com.lfhardware.provider.domain.SocialMediaLink;
import com.lfhardware.provider.dto.SocialMediaLinkDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SocialMediaLinkMapper {

    @Mappings({
            @Mapping(target = "facebook",source = "facebook"),
            @Mapping(target = "instagram",source = "instagram"),
            @Mapping(target = "tiktok",source = "tiktok")
    })
    SocialMediaLink mapToSocialMediaLinkEntity(SocialMediaLinkDTO socialMediaLinkDTO);
}
