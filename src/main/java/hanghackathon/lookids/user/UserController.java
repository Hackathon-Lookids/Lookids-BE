package hanghackathon.lookids.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import hanghackathon.lookids.global.ResponseDto;
import hanghackathon.lookids.global.exception.CustomException;
import hanghackathon.lookids.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static hanghackathon.lookids.global.constant.ErrorCode.USER_NOT_FOUND;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @GetMapping("/kakao/callback")
    public ResponseDto<UserDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return userService.kakaoLogin(code, response);
    }

    @GetMapping("/token")
    public String getToken(@RequestParam String email, HttpServletResponse response) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        String token = jwtUtil.createToken(user.getEmail());
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        return "token issued";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
