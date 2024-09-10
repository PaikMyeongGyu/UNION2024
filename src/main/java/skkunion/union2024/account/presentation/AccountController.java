package skkunion.union2024.account.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import skkunion.union2024.account.dto.request.CreateAccountRequest;
import skkunion.union2024.account.dto.response.CreateAccountResponse;
import skkunion.union2024.account.service.AccountServiceFacade;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountServiceFacade accountServiceFacade;
    private final PasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/accounts")
    public ResponseEntity<CreateAccountResponse> createAccount(
            @RequestBody final CreateAccountRequest accountRequest
    ) {
        String nickname = accountRequest.nickname();
        String email = accountRequest.email();
        String password = bCryptPasswordEncoder.encode(accountRequest.password());

        accountServiceFacade.createAccountWithEmailVerification(nickname, email, password);
        return ResponseEntity.status(CREATED).body(new CreateAccountResponse(nickname, email));
    }

    @GetMapping("/accounts/{verificationToken}")
    public ResponseEntity<Void> verifyAccount(
            @PathVariable final String verificationToken
    ) {
        accountServiceFacade.tryEmailVerification(verificationToken);
        return ResponseEntity.noContent().build();
    }
}
