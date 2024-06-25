package com.lfhardware.state.domain;

import com.lfhardware.shared.BasicNamedAttribute;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "tbl_state")
@NamedQueries({
        @NamedQuery(name = "State.findAllByNames", query = "SELECT u FROM State u WHERE u.name IN (:names)")
})
public class State extends BasicNamedAttribute { }