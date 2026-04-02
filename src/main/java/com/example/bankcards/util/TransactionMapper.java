package com.example.bankcards.util;

import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.entity.Transaction;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", imports = {java.math.BigDecimal.class})
public abstract class TransactionMapper {

    @Autowired
    protected EncryptionUtil encryptionUtil;

    @Mapping(target = "amount", expression = "java(transaction.getAmount().doubleValue())")
    @Mapping(target = "fromCardId", source = "fromCard.id")
    @Mapping(target = "toCardId", source = "toCard.id")
    @Mapping(target = "fromCardMask", source = "fromCard.maskedCardNumber")
    @Mapping(target = "toCardMask", source = "toCard.maskedCardNumber")
    @Mapping(target = "transactionDate", source = "transactionDate")
    public abstract TransactionDTO toDto(Transaction transaction);

    @Mapping(target = "amount", expression = "java(java.math.BigDecimal.valueOf(dto.getAmount()))")
    @Mapping(target = "fromCard", ignore = true)
    @Mapping(target = "toCard", ignore = true)
    @Mapping(target = "user", ignore = true)
    public abstract Transaction toEntity(TransactionDTO dto);

    @AfterMapping
    protected void afterToEntity(@MappingTarget Transaction transaction, TransactionDTO dto) {
        // Дополнительная логика после маппинга, если необходимо
    }

    // Метод для получения маскированного номера карты с дополнительным форматированием
    public String formatMaskedCardNumber(String maskedCardNumber) {
        if (maskedCardNumber == null || maskedCardNumber.isEmpty()) {
            return "**** **** **** ****";
        }

        // Если номер уже замаскирован в формате "**** **** **** 1234", возвращаем как есть
        if (maskedCardNumber.contains("*")) {
            return maskedCardNumber;
        }

        // Если пришел полный номер, маскируем его
        if (maskedCardNumber.length() == 16) {
            String last4 = maskedCardNumber.substring(12);
            return String.format("**** **** **** %s", last4);
        }

        return maskedCardNumber;
    }
}
