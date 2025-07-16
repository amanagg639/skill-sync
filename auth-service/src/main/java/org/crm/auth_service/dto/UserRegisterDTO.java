package org.crm.auth_service.dto;

import org.crm.auth_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterDTO {
//    @Enumerated(EnumType.STRING)
    private Role role;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
}
