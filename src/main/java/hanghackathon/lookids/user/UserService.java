package hanghackathon.lookids.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hanghackathon.lookids.global.ResponseDto;
import hanghackathon.lookids.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ResponseDto<UserDto> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 인가 코드로 액세스 토큰 요청
        String accessToken = getToken(code);

        // 토큰으로 카카오 API 호출 : 액세스 토큰으로 카카오 사용자 정보 가져오기
        UserDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // JWT 토큰 반환
        String token =  jwtUtil.createToken(kakaoUser.getEmail());

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return ResponseDto.setSuccess("카카오 로그인 성공", kakaoUserInfo);
    }

    // 인가 코드로 액세스 토큰 요청
    private String getToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "ff076a2f5e781b7b8c8bff83a4c179c9");
        body.add("redirect_uri", "http://localhost:3000/api/users/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    // 토큰으로 카카오 API 호출 : 액세스 토큰으로 카카오 사용자 정보 가져오기
    private UserDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String email = jsonNode.get("kakao_account")
                .get("email").asText();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();

        log.info("카카오 사용자 정보: " + email + ", " + nickname);
        return UserDto.of(email, nickname);
    }

    // 필요시에 회원가입
    @Transactional
    public User registerKakaoUserIfNeeded(UserDto kakaoUserInfo) {
        String email = kakaoUserInfo.email();
        User kakaoUser = userRepository.findByEmail(email)
                .orElse(null);
        if (kakaoUser == null) {
            // 신규 회원가입
            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            kakaoUser = User.of(email, kakaoUserInfo.email(), encodedPassword);

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }
}
