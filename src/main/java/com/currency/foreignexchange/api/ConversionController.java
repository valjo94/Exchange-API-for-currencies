package com.currency.foreignexchange.api;

import com.currency.foreignexchange.data.dto.CurrencyTransactionDTO;
import com.currency.foreignexchange.data.entities.CurrencyTransaction;
import com.currency.foreignexchange.exceptionhandling.ErrorMessage;
import com.currency.foreignexchange.exceptionhandling.custom.ElementNotFoundException;
import com.currency.foreignexchange.services.ConversionService;
import com.currency.foreignexchange.validators.DateConstraint;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ServiceUnavailableException;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * The ConversionController API is used for currency conversion related exchanges.
 */
@RestController
@Validated
@RequestMapping("conversion")
public class ConversionController {

    private final ConversionService conversionService;

    @Autowired
    public ConversionController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }


    /**
     * POST request used to convert an amount of given currency into another currency.
     *
     * @param transaction is a JSON object, containing sourceCurrency,amount and targetCurrency
     * @return CurrencyTransaction JSON object, containing the transactionId and date in addition to the input parameters.
     * @throws ServiceUnavailableException is thrown when the underlying service isn't responsive and the request is unsuccessful
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CurrencyTransaction.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "503", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<CurrencyTransaction> createCurrencyConversion(@RequestBody CurrencyTransactionDTO transaction)
            throws ServiceUnavailableException {
        return new ResponseEntity<>(conversionService.convertCurrency(transaction), HttpStatus.OK);
    }

    /**
     * GET request used to retrieve a currency exchange transaction.
     *
     * @param transactionId is the transactionId for the transaction that is to be retrieved
     * @return CurrencyTransaction JSON object, containing the given transaction information.
     * @throws ElementNotFoundException is thrown when no conversions with the given id are found
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CurrencyTransaction.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping(value = "/id", produces = "application/json")
    public ResponseEntity<CurrencyTransaction> getConversionById(@RequestParam @Min(value = 1, message = "Incorrect transactionId.") long transactionId)
            throws ElementNotFoundException {
        return new ResponseEntity<>(conversionService.getTransactionById(transactionId), HttpStatus.OK);
    }

    /**
     * GET request used to retrieve all the currency exchange transactions for a given day.
     *
     * @param date     is the date for the transactions that are to be retrieved
     * @param page     is an optional parameter used to retrieve a page number for the resulting exchanges(default is 0)
     * @param pageSize is an optional parameter used to specify how many results per page to list(default is 10)
     * @return CurrencyTransaction JSON object, containing the given transaction information.
     * @throws ElementNotFoundException is thrown when no conversions are found for the given date
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CurrencyTransaction.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping(value = "/date", produces = "application/json")
    public ResponseEntity<List<CurrencyTransaction>> getConversionsByDate(
            @RequestParam @DateConstraint String date,
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0, message = "page numbers should be positive integers") Integer page,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 1, message = "pageSize property should be greater than 1") Integer pageSize)
            throws ElementNotFoundException {

        return new ResponseEntity<>(conversionService.getTransactionByDate(date, page, pageSize), HttpStatus.OK);
    }
}