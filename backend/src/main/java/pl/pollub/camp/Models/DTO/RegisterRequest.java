package pl.pollub.camp.Models.DTO;

import lombok.Getter;

@Getter
public class RegisterRequest {
    private String email;
    private String username;
    private String address;
    private String phone;
    private String password;
}
