package com.lfhardware.provider.dto;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialMediaLinkDTO {

    protected String facebook;

    protected String instagram;

    protected String twitter;

    protected String whatsapp;

}
