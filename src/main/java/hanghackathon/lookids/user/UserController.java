package hanghackathon.lookids.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import hanghackathon.lookids.global.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/kakao/callback")
    public ResponseDto<UserDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return userService.kakaoLogin(code, response);
    }
}
