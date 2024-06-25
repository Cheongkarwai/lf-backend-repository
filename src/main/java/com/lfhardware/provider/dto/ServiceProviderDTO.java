package com.lfhardware.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.provider.domain.Status;

import java.util.Set;

public record ServiceProviderDTO(String id,
                                 String name,
                                 @JsonProperty("contact_info") ContactInfoDTO contactInfo,
                                 @JsonProperty("is_verified") boolean isVerified,
                                 @JsonProperty double rating,
                                 @JsonProperty("stripe_account_id") String stripeAccountId,
                                 @JsonProperty("front_identity_card") String frontIdentityCard,
                                 @JsonProperty("back_identity_card") String backIdentityCard,
                                 @JsonProperty("ssm") String ssm,
                                 Status status,
                                 @JsonProperty("business_profile_image") String businessProfileImage
) {
}
