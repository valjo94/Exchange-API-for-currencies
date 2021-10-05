package com.currency.foreignexchange.services;

import com.currency.foreignexchange.data.dto.external.CurrencyLayerDTO;
import com.currency.foreignexchange.validators.CurrencyConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.naming.ServiceUnavailableException;
import javax.validation.constraints.Min;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * This service uses a CurrencyLayer currency exchange service API provider to get live currency exchange rates.
 */
@Service
@Validated
public class CurrencyExchangeService {

    public static final String API_KEY = "68076783504e3563861157074d499103";
    private static final String API_URL = "http://api.currencylayer.com/live?access_key={0}&currencies={1},{2}&format=1";
    private static final String USD_PREFIX = "USD";

    private final RestTemplate externalExchangeService;

    @Autowired
    public CurrencyExchangeService(RestTemplate externalExchangeService) {
        this.externalExchangeService = externalExchangeService;
    }

    /**
     * This method retrieves live currency exchange rates between two currencies.
     *
     * @param sourceCurrency is the source currency
     * @param targetCurrency is the target currency
     * @return double value for the exchange rate between the two currencies
     * @throws ServiceUnavailableException is thrown when the third party service API is not responsive
     */
    public double getCurrencyExchangeRate(@CurrencyConstraint String sourceCurrency, @CurrencyConstraint String targetCurrency) 
            throws ServiceUnavailableException {
        CurrencyLayerDTO thirdPartyCurrencyLayerObject;
        try {
            thirdPartyCurrencyLayerObject = externalExchangeService.getForObject(buildURL(sourceCurrency, targetCurrency), CurrencyLayerDTO.class);
            if (thirdPartyCurrencyLayerObject != null) {
                return calculateExchangeRate(sourceCurrency, targetCurrency, thirdPartyCurrencyLayerObject);
            }
            return 0;
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException("Third party exchange rate vendors are unavailable. Please try again later.");
        }
    }

    /**
     * This method converts a given amount from one currency to another in live currency value.
     *
     * @param sourceCurrency is the source currency
     * @param amount         is the source amount
     * @param targetCurrency is the target currency
     * @return double value for the given amount in target currency
     * @throws ServiceUnavailableException is thrown when the third party service API is not responsive
     */
    public double convertCurrency(@CurrencyConstraint String sourceCurrency,
                                  @Min(value = 0, message = "amount parameter must be greater than 0") double amount,
                                  @CurrencyConstraint String targetCurrency) throws ServiceUnavailableException {
        CurrencyLayerDTO thirdPartyCurrencyLayerObject;
        try {
            thirdPartyCurrencyLayerObject = externalExchangeService.getForObject(buildURL(sourceCurrency, targetCurrency), CurrencyLayerDTO.class);
            if (thirdPartyCurrencyLayerObject != null) {
                double exchangeRate = calculateExchangeRate(sourceCurrency, targetCurrency, thirdPartyCurrencyLayerObject);
                return amount * exchangeRate;
            }
            return 0;
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException("Something went wrong. Please try again later");
        }
    }

    private double calculateExchangeRate(String from, String to, CurrencyLayerDTO thirdPartyCurrencyLayerObject) {
        HashMap<String, Double> quotes = thirdPartyCurrencyLayerObject.getQuotes();
        double sourceCurrencyToUSDRate = quotes.get(USD_PREFIX + from.toUpperCase(Locale.ROOT));
        double targetCurrencyToUSDRate = quotes.get(USD_PREFIX + to.toUpperCase(Locale.ROOT));

        return targetCurrencyToUSDRate / sourceCurrencyToUSDRate;
    }

    private String buildURL(String sourceCurrency, String targetCurrency) {
        return MessageFormat.format(API_URL, API_KEY, sourceCurrency, targetCurrency);
    }

}
