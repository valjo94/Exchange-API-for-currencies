package com.currency.foreignexchange.services;

import com.currency.foreignexchange.data.TransactionRepository;
import com.currency.foreignexchange.data.dto.CurrencyTransactionDTO;
import com.currency.foreignexchange.data.entities.CurrencyTransaction;
import com.currency.foreignexchange.exceptionhandling.custom.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.sql.Date;
import java.util.List;

/**
 * This service is to be used when currency exchange transaction related CRUD operations are needed.
 */
@Service
public class ConversionService {

    private final CurrencyExchangeService currencyExchangeService;

    private final TransactionRepository transactionRepository;

    @Autowired
    public ConversionService(CurrencyExchangeService currencyExchangeService, TransactionRepository transactionRepository) {
        this.currencyExchangeService = currencyExchangeService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * This method converts a given amount from one currency to another in live currency value and saves the transaction to the database.
     *
     * @param requestTransaction is an object, containing sourceCurrency, sourceAmount and targetCurrency
     * @return CurrencyTransaction is the saved requestTransaction, with targetAmount, date and transactionId.
     * @throws ServiceUnavailableException is thrown when the third party service API is not responsive
     */
    public CurrencyTransaction convertCurrency(CurrencyTransactionDTO requestTransaction) throws ServiceUnavailableException {
        String sourceCurrency = requestTransaction.getSourceCurrency();
        String targetCurrency = requestTransaction.getTargetCurrency();
        double amount = requestTransaction.getAmount();

        double resultAmount = currencyExchangeService.convertCurrency(sourceCurrency, amount, targetCurrency);

        CurrencyTransaction transaction = new CurrencyTransaction();
        transaction.setSourceCurrency(sourceCurrency);
        transaction.setTargetCurrency(targetCurrency);
        transaction.setAmount(resultAmount);

        return transactionRepository.save(transaction);
    }

    /**
     * This method retrieves the currency exchange transaction from the database
     *
     * @param transactionId is the id, used for the retrieval
     * @return CurrencyTransaction object, containing the transaction data from the database
     */
    public CurrencyTransaction getTransactionById(long transactionId) throws ElementNotFoundException {
        if (transactionRepository.findById(transactionId).isPresent()) {
            return transactionRepository.findById(transactionId).get();
        }
        throw new ElementNotFoundException("Transaction with id: " + transactionId + " not found");
    }

    /**
     * This method retrieves the currency exchanges for a given day.
     *
     * @param date     is the date for the currency exchanges
     * @param page     is the number of the page
     * @param pageSize is the size of results to be listed per page
     * @return list of transactions for the given day
     */
    public List<CurrencyTransaction> getTransactionByDate(String date, int page, int pageSize) throws ElementNotFoundException {
        List<CurrencyTransaction> result = transactionRepository.findAllByDate(Date.valueOf(date), PageRequest.of(page, pageSize));
        if (result.isEmpty()) {
            if (page > 0) {
                throw new ElementNotFoundException("No more transactions found for date");
            }
            throw new ElementNotFoundException("No transactions found for date " + date);
        }
        return result;
    }

}
