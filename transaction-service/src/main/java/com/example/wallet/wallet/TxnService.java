package com.example.wallet.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TxnService implements UserDetailsService {

    @Autowired
    TxnRepository txnRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;


    public String initiateTxn(String username, String receiver, String purpose, Double amount) throws JsonProcessingException {

         Transaction transaction = Transaction.builder()
                 .sender(username)
                 .receiver(receiver)
                 .purpose(purpose)
                 .amount(amount)
                 .transactionStatus(TransactionStatus.PENDING)
                 .transactionId(UUID.randomUUID().toString())
                 .build();

         txnRepository.save(transaction);

         //send kafka msg to wallet service for update wallet
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sender",username);
        jsonObject.put("receiver",receiver);
        jsonObject.put("purpose",purpose);
        jsonObject.put("amount",amount);
        jsonObject.put("transactionId",transaction.getTransactionId());

        kafkaTemplate.send(CommonConstants.TRANSACTION_CREATION_TOPIC,objectMapper.writeValueAsString(jsonObject));
         return transaction.getTransactionId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        JSONObject requestedUser = getUserFromUserService(username);

        List<GrantedAuthority> authorities;

        List<LinkedHashMap<String,String>> requestAuthories = (List<LinkedHashMap<String,String>>)requestedUser.get("authorities");

        authorities = requestAuthories.stream()
                .map(x->x.get("authority"))
                .map(x -> new SimpleGrantedAuthority(x))
                .collect(Collectors.toList());
        return new User(
                (String) requestedUser.get("username"),
                (String) requestedUser.get("password"),
                authorities

        );
    }

    private JSONObject getUserFromUserService(String username)
    {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setBasicAuth("txn_service","txn123");

        HttpEntity request = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange("http://localhost:4000/admin/user/" + username,
                HttpMethod.GET,request,JSONObject.class).getBody();
    }
}


