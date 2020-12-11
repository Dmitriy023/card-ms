package az.iba.ms.ufxinfo.controllers;

import az.iba.ms.ufxinfo.dtos.ResponseDto;
import az.iba.ms.ufxinfo.dtos.TransactionDto;
import az.iba.ms.ufxinfo.enums.ReturnTypes;
import az.iba.ms.ufxinfo.services.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(TransactionController.ENDPOINT)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "UFX Transactions")
public class TransactionController {

    public static final String ENDPOINT = "/v1/cards/{contractNumber}/transactions";

    @Autowired
    private TransactionService transactionService;

    @ApiOperation("Returns transactions by contractNumber")
    @GetMapping
    public ResponseEntity<ResponseDto<List<TransactionDto>>> getTransactions(
            @ApiParam(
                    name = "contractNumber",
                    type = "String",
                    value = "Contract Number",
                    example = "5490349934171340",
                    required = true)
            @PathVariable("contractNumber")
            @Pattern(regexp = "\\d+")
                    String contractNumber,
            @ApiParam(
                    name = "from",
                    type = "String",
                    value = "Begin date filter",
                    example = "2020-09-20",
                    required = true)
            @RequestParam
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
                    String from,
            @ApiParam(
                    name = "to",
                    type = "String",
                    value = "End date filter",
                    example = "2020-09-21",
                    required = true)
            @RequestParam
            @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
                    String to,
            @ApiParam(
                    name = "pagesize",
                    type = "String",
                    value = "List size of a page",
                    example = "1231",
                    allowableValues = "range[1, 100]",
                    required = true)
            @RequestParam
            @Min(1)
            @Max(100)
                    int pagesize,
            @ApiParam(
                    name = "page",
                    type = "String",
                    value = "Current page index",
                    example = "1",
                    allowableValues = "range[1, 100]",
                    required = true)
            @RequestParam
            @Min(1)
            @Max(100)
                    int page) {

        List<TransactionDto> cardTransactionDtos =
                transactionService.getTransactions(contractNumber, from, to, pagesize, page);

        ResponseDto<List<TransactionDto>> response =
                new ResponseDto<>(cardTransactionDtos, ReturnTypes.OK.name());

        return ResponseEntity.ok(response);
    }
}
