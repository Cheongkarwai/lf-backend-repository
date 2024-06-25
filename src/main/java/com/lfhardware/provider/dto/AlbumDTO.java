package com.lfhardware.provider.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.lfhardware.provider.domain.Album}
 */
public record AlbumDTO(String name, String description, List<String> photos) implements Serializable {

}