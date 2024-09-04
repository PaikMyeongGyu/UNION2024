package skkunion.union2024.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import skkunion.union2024.auth.domain.Authority;
import skkunion.union2024.auth.domain.repository.AuthorityRepository;
import skkunion.union2024.global.exception.AuthException;
import skkunion.union2024.global.exception.exceptioncode.ExceptionCode;
import skkunion.union2024.member.domain.Member;
import skkunion.union2024.member.domain.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

import static skkunion.union2024.global.exception.exceptioncode.ExceptionCode.*;


@Component
@RequiredArgsConstructor
public class UnionAuthenticationProvider implements AuthenticationProvider {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        Member member = memberRepository.findByEmail(email)
                                    .orElseThrow(() -> new AuthException(ACCOUNT_NOT_FOUND));
        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new UsernameNotFoundException(ACCOUNT_NOT_FOUND.getMessage());

        List<Authority> findAuthorities = authorityRepository.findAllByEmail(email);
        return new UsernamePasswordAuthenticationToken(email, password, getGrantedAuthorities(findAuthorities));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<Authority> authorities){
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(Authority authority: authorities)
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getRole()));

        return grantedAuthorities;
    }
}
