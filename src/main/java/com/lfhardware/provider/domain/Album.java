package com.lfhardware.provider.domain;

import com.lfhardware.shared.BasicNamedAttribute;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_album")
public class Album extends BasicNamedAttribute {

    private String description;

    @OneToOne(mappedBy = "album", fetch = FetchType.LAZY)
    private ServiceProvider serviceProvider;
}
