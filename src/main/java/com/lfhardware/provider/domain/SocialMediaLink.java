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
    protected String facebook;

    @Column(name = "instagram_link")
    protected String instagram;

    @Column(name = "twitter_link")
    private String twitter;

    @Column(name = "whatsapp_link")
    private String whatsapp;
}
