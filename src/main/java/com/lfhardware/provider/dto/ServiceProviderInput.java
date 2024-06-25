package com.lfhardware.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.provider_business.dto.ServiceDTO;

import java.util.List;


public record ServiceProviderInput(String name, String description, String address,
                                   @JsonProperty("contact_info") ContactInfoDTO contactInfo,
                                   @JsonProperty("bank_details") BankDetailsDTO bankingDetails,
                                   @JsonProperty("social_media_link") SocialMediaLinkDTO socialMediaLink,
                                   @JsonProperty("album") AlbumDTO album,

                                   @JsonProperty("type_of_services") List<ServiceDTO> services,
                                   @JsonProperty("coverage") CoverageDTO coverage) {

}
