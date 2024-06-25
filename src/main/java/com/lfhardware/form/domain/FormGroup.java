package com.lfhardware.form.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormGroup implements Serializable {

    private String type;

    private String label;

    private String name;

    private String value;

    private Option[] options;
}
