package org.crm.auth_service.dto;

import org.crm.auth_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserLoginResponseDTO {
    private String userId;
//    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    private String userName;
}
