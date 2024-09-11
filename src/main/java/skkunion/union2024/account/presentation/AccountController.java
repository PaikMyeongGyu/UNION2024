package skkunion.union2024.account.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import skkunion.union2024.account.dto.request.CreateAccountRequest;
import skkunion.union2024.account.dto.request.DeleteAccountRequest;
import skkunion.union2024.account.dto.request.ResendEmailVerificationRequest;
import skkunion.union2024.account.dto.response.CreateAccountResponse;
import skkunion.union2024.account.service.AccountServiceFacade;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountServiceFacade accountServiceFacade;

    @PostMapping("/accounts")
    public ResponseEntity<CreateAccountResponse> createAccount(
            @RequestBody final CreateAccountRequest accountRequest
    ) {
        String nickname = accountRequest.nickname();
        String email = accountRequest.email();
        String password = accountRequest.password();

        accountServiceFacade.createAccountWithEmailVerification(nickname, email, password);
        return ResponseEntity.status(CREATED).body(new CreateAccountResponse(nickname, email));
    }

    @PostMapping("/accounts/resend")
    public ResponseEntity<Void> resendEmailVerification(
            @RequestBody final ResendEmailVerificationRequest resendRequest
    ) {
        String email = resendRequest.email();
        String password = resendRequest.password();

        accountServiceFacade.resendEmailVerification(email, password);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/accounts/{verificationToken}")
    public ResponseEntity<Void> verifyAccount(
            @PathVariable final String verificationToken
    ) {
        accountServiceFacade.tryEmailVerification(verificationToken);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/accounts")
    public ResponseEntity<Void> deleteAccount(
        @RequestBody final DeleteAccountRequest deleteAccountRequest
    ) {
        String email = deleteAccountRequest.email();
        String password = deleteAccountRequest.password();

        accountServiceFacade.deleteAccount(email, password);
        return ResponseEntity.noContent().build();
    }
}
