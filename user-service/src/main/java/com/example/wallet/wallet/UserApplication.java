package com.example.wallet.wallet;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserApplication implements CommandLineRunner {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        User txnServiceUser = User.builder()
//                .phoneNumber("txn_service")
//                .password(passwordEncoder.encode("txn123"))
//                .email("txnexample@gmail.com")
//                .userIdentifier(UserIdentifier.SERVICE_ID)
//                .authorities("ADM")
//                .identifierValue("txn123")
//                .build();
//
//        userRepository.save(txnServiceUser);
    }
}
