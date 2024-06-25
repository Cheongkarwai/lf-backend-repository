package com.lfhardware.provider.domain;

import com.lfhardware.shared.BasicNamedAttribute;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_bank")
@NamedQueries({
        @NamedQuery(name = "Bank.findByName", query = "FROM Bank b WHERE b.name = :name")
})
public class Bank extends BasicNamedAttribute {


    @OneToMany(mappedBy = "bank",cascade = CascadeType.ALL)
    public Set<BankingDetails> bankingDetails = new HashSet<>();

}
