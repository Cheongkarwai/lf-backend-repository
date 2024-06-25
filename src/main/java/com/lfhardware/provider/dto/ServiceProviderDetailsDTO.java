package com.lfhardware.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.auth.dto.AddressDTO;
import com.lfhardware.provider.domain.Status;
import com.lfhardware.provider_business.dto.ServiceDTO;
import com.lfhardware.shared.BaseAddress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record ServiceProviderDetailsDTO(String id, String name,
                                        @JsonProperty("contact_info") ContactInfoDTO contactInfo,
                                        @JsonProperty("is_verified") boolean isVerified, @JsonProperty double rating,
                                        List<ServiceDTO> services, String overview,
                                        @JsonProperty("state_coverages") Set<String> stateCoverages,
                                        @JsonProperty("country_coverages") Set<String> countryCoverages,
                                        @JsonProperty("city_coverages") Set<String> cityCoverages, Status status,
                                        @JsonProperty("created_at") LocalDateTime createdAt,
                                        @JsonProperty("business_profile_image") String businessProfileImage,
                                        Set<ServiceProviderReviewDTO> reviews,
                                        @JsonProperty("social_media") SocialMediaLinkDTO socialMedia,
                                        @JsonProperty("stripe_account_id") String stripeAccountId,
                                        @JsonProperty("front_identity_card") String frontIdentityCard,
                                        @JsonProperty("back_identity_card") String backIdentityCard,
                                        @JsonProperty AddressDTO address) {
}
