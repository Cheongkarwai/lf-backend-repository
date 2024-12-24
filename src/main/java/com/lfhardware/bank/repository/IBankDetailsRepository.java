package com.lfhardware.bank.repository;

import com.lfhardware.provider.domain.BankDetailsId;
import com.lfhardware.provider.domain.BankingDetails;
import com.lfhardware.core.repository.CrudRepository;

public interface IBankDetailsRepository extends CrudRepository<BankingDetails, BankDetailsId> {
}
