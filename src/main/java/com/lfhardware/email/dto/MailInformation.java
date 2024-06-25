package com.lfhardware.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.thymeleaf.context.Context;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailInformation {

    private Recipient recipient;

    private String subject;

    private Context context;

}
