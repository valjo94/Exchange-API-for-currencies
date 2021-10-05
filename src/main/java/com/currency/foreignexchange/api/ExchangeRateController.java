package com.currency.foreignexchange.api;

import com.currency.foreignexchange.data.dto.ExchangeRateDTO;
import com.currency.foreignexchange.exceptionhandling.ErrorMessage;
import com.currency.foreignexchange.services.CurrencyExchangeService;
import com.currency.foreignexchange.validators.CurrencyConstraint;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ServiceUnavailableException;

/**
 * The ExchangeRateController API is used to get live currency exchange rates.
 */
@RestController
@Validated
@RequestMapping("exchange")
public class ExchangeRateController {

    private final CurrencyExchangeService currencyExchangeService;

    @Autowired
    public ExchangeRateController(CurrencyExchangeService currencyExchangeService) {
        this.currencyExchangeService = currencyExchangeService;
    }

    /**
     * This GET request is used to retrieve the currency exchange rates between two currencies.
     * @param sourceCurrency is the source currency 
     * @param targetCurrency is the target currency
     * @return ExchangeRate JSON object, containing the two currencies, as well as their current exchange rate 
     * @throws ServiceUnavailableException is thrown when the underlying service isn't responsive and the request is unsuccessful
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ExchangeRateDTO.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "503", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<ExchangeRateDTO> getExchangeRate(@RequestParam @CurrencyConstraint(message = "sourceCurrency") String sourceCurrency,
                                           @RequestParam @CurrencyConstraint(message = "targetCurrency") String targetCurrency) throws ServiceUnavailableException {
        double currencyExchange = currencyExchangeService.getCurrencyExchangeRate(sourceCurrency, targetCurrency);
        return new ResponseEntity<>(new ExchangeRateDTO(sourceCurrency, targetCurrency, currencyExchange), HttpStatus.OK);
    }

}