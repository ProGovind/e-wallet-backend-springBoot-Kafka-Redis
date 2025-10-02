package com.example.wallet.wallet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    @NotBlank
   private String name;
   @NotBlank
   private String password;

   @NotBlank
   private String email;

   @NotBlank
   private String phoneNumber;

   private String dob;

   private String country;
   @NotBlank
   private String identifierValue;
   @NotNull
   private UserIdentifier userIdentifier;


   public User to()
   {
       return User.builder()
               .name(this.name)
               .password(this.password)
               .email(this.email)
               .dob(this.dob)
               .country(this.country)
               .phoneNumber(this.phoneNumber)
               .identifierValue(this.identifierValue)
               .userIdentifier(this.userIdentifier)
               .build();
   }


}
