package hanghackathon.lookids.user;

import jakarta.validation.constraints.Email;

public record UserDto(
        @Email String email,
        String nickname
) {
    public static UserDto of(
            String email,
            String nickname
    ) {
        return new UserDto(email, nickname);
    }
}