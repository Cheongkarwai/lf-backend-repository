package com.lfhardware.provider.dto;

import java.util.List;


public record CoverageDTO(List<String> countries, List<String> states, List<String> cities) {

}
