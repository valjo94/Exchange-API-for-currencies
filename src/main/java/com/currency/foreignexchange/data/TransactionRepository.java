package com.currency.foreignexchange.data;  

import com.currency.foreignexchange.data.entities.CurrencyTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * Repository class used for all the CRUD operations regarding currency transactions.
 */
@Repository
public interface TransactionRepository extends CrudRepository<CurrencyTransaction, Long>  
{
    List<CurrencyTransaction> findAllByDate(Date date, Pageable pageRequest);
} 