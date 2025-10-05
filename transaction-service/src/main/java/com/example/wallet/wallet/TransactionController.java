package com.example.wallet.wallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
public class TransactionController {

   @Autowired
   TxnService txnService;

   @PostMapping("/txn")
    public String initiateTxn(@RequestParam("receiver") String receiver,
                              @RequestParam("purpose") String purpose,
                              @RequestParam("amount") Double amount
                              ) throws JsonProcessingException
   {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       UserDetails user = (UserDetails) authentication.getPrincipal();

       return txnService.initiateTxn(user.getUsername(),receiver,purpose,amount);
   }


}
