package hanghackathon.lookids.global.security;

import hanghackathon.lookids.global.exception.CustomException;
import hanghackathon.lookids.user.User;
import hanghackathon.lookids.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static hanghackathon.lookids.global.constant.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return new UserDetailsImpl(user, user.getEmail());
    }
}
