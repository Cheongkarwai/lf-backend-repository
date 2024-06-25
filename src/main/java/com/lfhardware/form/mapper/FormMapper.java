package com.lfhardware.form.mapper;

import com.lfhardware.form.domain.Form;
import com.lfhardware.form.domain.FormConfiguration;
import com.lfhardware.form.dto.FormDTO;
import com.lfhardware.form.dto.FormInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FormMapper {

//    @Mappings({
//            @Mapping(target = "configuration", source = "value")
//    })
//    Form mapToForm(FormInput formInput);

    @Mappings({
            @Mapping(target = "configuration", source = "configuration"),
            @Mapping(target = "serviceId", source = "formId.serviceId"),
            @Mapping(target = "serviceProviderId", source = "formId.serviceProviderId")
    })
    FormDTO mapToFormDTO(Form form);

}
