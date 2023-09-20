package hanghackathon.lookids.user;

public record UserDto(
        String email,
        String nickname
) {
    public static UserDto of(
            String email,
            String nickname
    ) {
        return new UserDto(email, nickname);
    }
}