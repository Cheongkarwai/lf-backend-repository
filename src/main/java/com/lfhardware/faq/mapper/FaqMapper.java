package com.lfhardware.faq.mapper;

import com.lfhardware.faq.domain.Faq;
import com.lfhardware.faq.dto.FaqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FaqMapper {

   @ToFaq
   @Mapping(target = "id", source = "id")
   FaqDTO mapToFaqDTO(Faq faq);

    @ToFaq
    Faq mapToFaq(FaqDTO faqDTO);
}
