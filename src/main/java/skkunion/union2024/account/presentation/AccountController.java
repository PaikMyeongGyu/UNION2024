package skkunion.union2024.account.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skkunion.union2024.account.dto.request.CreateAccountRequest;
import skkunion.union2024.account.dto.request.DeleteAccountRequest;
import skkunion.union2024.account.dto.request.ResendEmailVerificationRequest;
import skkunion.union2024.account.dto.response.CreateAccountResponse;
import skkunion.union2024.account.service.AccountServiceFacade;
import skkunion.union2024.account.service.AccountServiceTempFacade;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountServiceFacade accountServiceFacade;
    /**
     * 편리성을 위해 임시로 만든 코드로 제거해줘야 합니다.
     */
    // private final AccountServiceFacade accountServiceTempFacade;

    @PostMapping("/accounts")
    public ResponseEntity<CreateAccountResponse> createAccount(
            @RequestBody final CreateAccountRequest accountRequest
    ) {
        String nickname = accountRequest.nickname();
        String email = accountRequest.email();
        String password = accountRequest.password();

        // 편의성을 위해 임시로 만든 코드로 Temp가 아닌 다른 코드로 변경해주어야 합니다.
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
