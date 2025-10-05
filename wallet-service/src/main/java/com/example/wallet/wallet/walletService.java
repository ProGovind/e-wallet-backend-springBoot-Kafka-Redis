package com.example.wallet.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class walletService {

    private static Logger logger  = LoggerFactory.getLogger(walletService.class);

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @KafkaListener(topics = CommonConstants.USER_CREATION_TOPIC ,groupId = "grp123")
    public void createwallet(String msg) throws ParseException {
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

    @KafkaListener(topics = CommonConstants.TRANSACTION_CREATION_TOPIC,groupId = "grp123")
    public void updateWalletforTxn(String msg) throws ParseException , JsonProcessingException
    {
        JSONObject data = null;
        try {
            data = (JSONObject) new JSONParser().parse(msg);
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }

        String sender = (String) data.get("sender");
        String receiver = (String) data.get("receiver");
        String txnId = (String) data.get("transactionId");
        Double amount = (Double) data.get("amount");
        logger.info("Updating Wallet for Sender:::"+ sender);

        Wallet senderwallet = walletRepository.findByPhoneNumber(sender);
        Wallet receiverwallet = walletRepository.findByPhoneNumber(receiver);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("txnId", txnId);
        jsonObject.put("sender", sender);
        jsonObject.put("receiver", receiver);
        jsonObject.put("amount", amount);

        if(senderwallet == null || receiverwallet == null ||
                senderwallet.getBalance() < amount
        ){
            jsonObject.put("walletUpdateStatus",WalletUpdateStatus.SUCCESS);
             kafkaTemplate.send(CommonConstants.WALLET_UPDATE_TOPIC,objectMapper.writeValueAsString(jsonObject));
        }

        walletRepository.updateWallet(receiver,amount);
        walletRepository.updateWallet(sender,0-amount);


        jsonObject.put("walletUpdateStatus",WalletUpdateStatus.SUCCESS);
        kafkaTemplate.send(CommonConstants.WALLET_UPDATE_TOPIC,objectMapper.writeValueAsString(jsonObject));

    }

}
