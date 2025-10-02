package com.example.wallet.wallet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class walletService {

    @Autowired
    WalletRepository walletRepository;

    @KafkaListener(topics = CommonConstants.USER_CREATION_TOPIC ,groupId = "grp123")
    public void createallet(String msg) throws ParseException {
        JSONObject data = null;
        try {
            data = (JSONObject) new JSONParser().parse(msg);
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }

        String phoneNumber = (String) data.get("phoneNumber");
        Long userId = (Long) data.get("userId");
        String identifierValue = (String) data.get("identifierValue");
        String userIdentifier = (String) data.get("userIdentifier");

        Wallet wallet = Wallet.builder()
                .phoneNumber(phoneNumber)
                .userId(userId)
                .userIdentifier(UserIdentifier.valueOf(userIdentifier))
                .identifierValue(identifierValue)
                .balance(10.0)
                .build();

        walletRepository.save(wallet);

    }

}
