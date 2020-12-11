package az.iba.ms.ufxinfo.controllers;

import az.iba.ms.ufxinfo.dtos.BalanceDto;
import az.iba.ms.ufxinfo.dtos.ResponseDto;
import az.iba.ms.ufxinfo.enums.ReturnTypes;
import az.iba.ms.ufxinfo.services.BalanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(BalanceController.ENDPOINT)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "UFX Balance")
public class BalanceController {

    public static final String ENDPOINT = "/v1/balances";

    @Autowired
    private BalanceService balanceService;

    @ApiOperation("Returns balance info by card numbers")
    @GetMapping
    public ResponseEntity<ResponseDto<List<BalanceDto>>> getBalance(
            @ApiParam(
                    name = "card-list",
                    type = "String",
                    value = "Comma separated card numbers",
                    example = "4444555566667777 or 4444555566667777,3333555566667777",
                    required = true)
            @RequestParam(name = "card-list")
            @Pattern(regexp = "\\d+(,\\d+)*")
                    String cardNumbers) {

        ResponseDto<List<BalanceDto>> response =
                new ResponseDto<>(balanceService.getBalance(cardNumbers), ReturnTypes.OK.name());

        return ResponseEntity.ok(response);
    }
}
