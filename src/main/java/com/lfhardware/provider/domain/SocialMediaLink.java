package com.lfhardware.provider.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SocialMediaLink {

    @Column(name = "facebook_link")
    private String facebook;

    @Column(name = "instagram_link")
    private String instagram;

    @Column(name = "tiktok_link")
    private String tiktok;
}
