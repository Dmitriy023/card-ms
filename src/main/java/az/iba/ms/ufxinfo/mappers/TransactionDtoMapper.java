package az.iba.ms.ufxinfo.mappers;

import az.iba.ms.ufxinfo.dtos.AccountAmountDto;
import az.iba.ms.ufxinfo.dtos.BillingDto;
import az.iba.ms.ufxinfo.dtos.ExtraDto;
import az.iba.ms.ufxinfo.dtos.PostingDetailsDto;
import az.iba.ms.ufxinfo.dtos.TransactionDto;
import az.iba.ms.ufxinfo.models.azericard.commons.Billing;
import az.iba.ms.ufxinfo.models.azericard.commons.DocData;
import az.iba.ms.ufxinfo.models.azericard.commons.StmtItem;
import az.iba.ms.ufxinfo.models.azericard.commons.Transaction;
import az.iba.ms.ufxinfo.models.azericard.commons.UfxMsg;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransactionDtoMapper {

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionDtoMapper.class);

    public List<TransactionDto> map(UfxMsg transactionResponse, int pageSize, int page) {

        List<StmtItem> items =
                transactionResponse
                        .getMsgData()
                        .getDoc()
                        .getDataRs()
                        .getStmt()
                        .getAdditionalStmt()
                        .getStmtItems();

        if (items != null) {
            items = items.stream()
                    .skip((long) (page - 1) * pageSize)
                    .limit(pageSize)
                    .collect(Collectors.toList());

            return items.parallelStream().map(this::map).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private TransactionDto map(StmtItem item) {
        TransactionDto transactionDto = new TransactionDto();
        try {
            DocData docData = item.getDocData();
            transactionDto.setDate(docData.getLocalDt());
            transactionDto.setDescription(docData.getDescription());
            if (docData.getSourceDtls() != null) {
                transactionDto.setMcc(docData.getSourceDtls().getSic());
            }
            transactionDto.setTrnTypeId(docData.getTransType().getTransCode().getTransTypeId());
            transactionDto.setTrnTypeCode(docData.getTransType().getTransCode().getTransTypeCode());
            transactionDto.setStatus(item.getPostingDetails().getStatus().getRespText());
            if (item.getPostingDetails().getAccountAmount() != null) {
                transactionDto.setLocalAmount(
                        getAmount(item.getPostingDetails().getAccountAmount().getAmount()));
            }

            Transaction transaction = docData.getTransaction();
            setAmountAndCurrency(transaction, transactionDto);
            setRrn(docData, transactionDto);
            setTrnType(transaction, item, transactionDto);

            transactionDto.setDescriptionExt(docData.getDescriptionExt());
            transactionDto.setPostingDetailsDto(getPostingDetailsDto(item));
            transactionDto.setBillingDto(getBillingDto(item.getDocData().getBilling()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return transactionDto;
    }

    private void setAmountAndCurrency(Transaction transaction, TransactionDto transactionDto) {
        if (transaction != null) {
            transactionDto.setAmount(getAmount(transaction.getAmount()));
            transactionDto.setCurrency(transaction.getCurrency());
        } else {
            transactionDto.setAmount(BigDecimal.valueOf(0, 2));
        }
    }

    private void setRrn(DocData docData, TransactionDto transactionDto) {
        if (docData.getParms() != null) {
            docData.getParms().forEach(
                    p -> {
                        if (p.getParmCode().equals("RRN")) {
                            transactionDto.setRrn(p.getValue());
                        }
                    });
        }
    }

    private void setTrnType(Transaction transaction, StmtItem item, TransactionDto transactionDto) {
        if (transaction == null
                || transaction.getAmount() == null
                || transaction.getAmount().equals("0.00")
                || item.getPostingDetails().getAccountAmount() == null
                || item.getPostingDetails().getAccountAmount().getAmount() == null
                || item.getPostingDetails().getAccountAmount().getAmount().charAt(0) == '-'
                || item.getPostingDetails().getAccountAmount().getType().equals("Blocked")) {
            transactionDto.setTrnType("D");
        } else {
            transactionDto.setTrnType("C");
        }
    }

    private PostingDetailsDto getPostingDetailsDto(StmtItem item) {
        PostingDetailsDto postingDetailsDto = new PostingDetailsDto();
        postingDetailsDto.setProcessingStatus(item.getPostingDetails().getProcessingStatus());
        if (item.getPostingDetails().getAccountAmount() == null
                || (item.getPostingDetails().getAccountAmount().getType() == null
                && item.getPostingDetails().getAccountAmount().getCurrency() == null
                && item.getPostingDetails().getAccountAmount().getAmount() == null)) {
            postingDetailsDto.setAccountAmountDto(null);
        } else {
            postingDetailsDto.setAccountAmountDto(
                    AccountAmountDto.builder()
                            .type(item.getPostingDetails().getAccountAmount().getType())
                            .currency(item.getPostingDetails().getAccountAmount().getCurrency())
                            .amount(getAmount(item.getPostingDetails().getAccountAmount().getAmount()))
                            .build());
        }
        return postingDetailsDto;
    }

    private BillingDto getBillingDto(Billing billing) {
        return BillingDto.builder()
                .phaseDate(billing.getPhaseDate())
                .currency(billing.getCurrency())
                .amount(getAmount(billing.getAmount()))
                .extras(billing.getExtras() == null
                        ? new ArrayList<>()
                        : billing.getExtras().stream()
                        .map(e -> ExtraDto.builder()
                                .type(e.getType())
                                .currency(e.getCurrency())
                                .amount(getAmount(e.getAmount()))
                                .details(e.getDetails())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private BigDecimal getAmount(String amount) {
        return StringUtils.isNotBlank(amount) ? new BigDecimal(amount) : null;
    }
}
