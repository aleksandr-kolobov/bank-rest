package com.example.bankcards.util;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.dto.UpdateCardStatusRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")public abstract class CardMapper {

    @Mapping(source = "user.id", target = "userId")
    public abstract CardDTO toCardDTO(Card card);

    public abstract Card toCardEntity(CreateCardRequest request, User user, String encryptedCardNumber, String maskedCardNumber);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "encryptedCardNumber", ignore = true)
    @Mapping(target = "maskedCardNumber", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Card updateCardEntity(UpdateCardStatusRequest request, @MappingTarget Card card);

    @Mapping(source = "fromCard.id", target = "fromCardId")
    @Mapping(source = "fromCard.maskedCardNumber", target = "fromCardMask")
    @Mapping(source = "toCard.id", target = "toCardId")
    @Mapping(source = "toCard.maskedCardNumber", target = "toCardMask")
    public abstract TransactionDTO toTransactionDTO(Transaction transaction);

}
