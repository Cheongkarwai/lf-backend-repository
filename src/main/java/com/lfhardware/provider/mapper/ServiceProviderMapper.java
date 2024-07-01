package com.lfhardware.provider.mapper;

import com.lfhardware.address.mapper.AddressMapper;
import com.lfhardware.provider.domain.*;
import com.lfhardware.provider.dto.ServiceProviderDTO;
import com.lfhardware.provider.dto.ServiceProviderDetailsDTO;
import com.lfhardware.provider.dto.ServiceProviderInput;
import com.lfhardware.provider.dto.ServiceProviderOnboardInput;
import com.lfhardware.provider_business.mapper.ServiceDetailsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {SocialMediaLinkMapper.class,CoverageMapper.class, AlbumMapper.class, ServiceDetailsMapper.class,
        ServiceProviderReviewMapper.class, AddressMapper.class})
public interface ServiceProviderMapper {

//    @Mappings({
//            @Mapping(target = "name",source = "name"),
//            @Mapping(target = "businessDescription",source = "description"),
//            @Mapping(target = "address",source = "address"),
//            @Mapping(target = "emailAddress",source = "contactInfo.emailAddress"),
//            @Mapping(target = "bankingDetails",ignore = true)
//    })
//    ServiceProvider mapToServiceProviderEntity(ServiceProviderInput serviceProviderDTO);

    @Mappings({
            @Mapping(target = "name",source = "basicInformation.businessDetails.name"),
            @Mapping(target = "address",source = "basicInformation.businessDetails.address"),
            @Mapping(target = "emailAddress",source = "basicInformation.businessDetails.emailAddress"),
            @Mapping(target = "phoneNumber", source = "basicInformation.businessDetails.phoneNumber"),
            @Mapping(target = "faxNo", source = "basicInformation.businessDetails.faxNo"),
            @Mapping(target = "location", source = "basicInformation.businessDetails.location"),
            @Mapping(target = "socialMediaLink", source = "basicInformation.socialMediaLink")
    })
    ServiceProvider mapToServiceProviderEntity(ServiceProviderOnboardInput serviceProviderInput);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name",source = "name"),
            @Mapping(target = "contactInfo.emailAddress", source = "emailAddress"),
            @Mapping(target = "verified", source = "verified"),
            @Mapping(target = "rating", source = "rating"),
            @Mapping(target = "backIdentityCard", source = "backIdentityCard"),
            @Mapping(target = "frontIdentityCard", source = "frontIdentityCard"),
            @Mapping(target = "ssm", source = "ssm"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "businessProfileImage", source = "businessProfileImage"),
            @Mapping(target = "socialMedia", source = "socialMediaLink"),
//            @Mapping(target = "businessDescription",source = "description"),
//            @Mapping(target = "businessAddress",source = "address"),
//            @Mapping(target = "businessEmailAddress",source = "contactInfo.emailAddress"),
    })
    ServiceProviderDTO mapToServiceProviderDTO(ServiceProvider serviceProvider);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name",source = "name"),
            @Mapping(target = "contactInfo.emailAddress", source = "emailAddress"),
            @Mapping(target = "contactInfo.phoneNumber", source = "phoneNumber"),
            @Mapping(target = "verified", source = "verified"),
            @Mapping(target = "rating", source = "rating"),
            @Mapping(target = "overview", source = "overview"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "services", source = "serviceDetails"),
            @Mapping(target = "businessProfileImage", source = "businessProfileImage"),
            @Mapping(target = "reviews", source = "reviews"),
            @Mapping(target = "socialMedia", source = "socialMediaLink"),
            @Mapping(target = "frontIdentityCard", source = "frontIdentityCard"),
            @Mapping(target = "backIdentityCard", source = "backIdentityCard"),
            @Mapping(target = "stripeAccountId", source = "stripeAccountId")
    })
    ServiceProviderDetailsDTO mapToServiceProviderDetailsDTO(ServiceProvider serviceProvider);

    default Set<String> mapToStateCoverage(Set<StateCoverage> stateCoverages){
        return stateCoverages.stream().map(stateCoverage -> stateCoverage.getState().getName()).collect(Collectors.toSet());
    }

    default Set<String> mapToCountryCoverage(Set<CountryCoverage> countryCoverages){
        return countryCoverages.stream().map(countryCoverage -> countryCoverage.getCountry().getName()).collect(Collectors.toSet());
    }

    default Set<String> mapCityCoverageName(Set<CityCoverage> coverages){
        return coverages.stream().map(coverage->coverage.getCity().getName()).collect(Collectors.toSet());
    }
}
