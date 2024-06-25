package com.lfhardware.form.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormConfiguration implements Serializable {

    private String title;

    @JsonProperty("form_groups")
    private FormGroup[] formGroups;
}
