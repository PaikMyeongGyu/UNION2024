package skkunion.union2024.account.presentation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skkunion.union2024.account.dto.request.CreateAccountRequest;
import skkunion.union2024.account.dto.response.CreateAccountResponse;
import skkunion.union2024.account.service.AccountServiceFacade;
import skkunion.union2024.global.exception.EmailVerificationExpiredException;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class AccountController {

    public final AccountServiceFacade accountServiceFacade;

    @PostMapping("/account")
    public ResponseEntity<CreateAccountResponse> createAccount(
            @RequestBody final CreateAccountRequest accountRequest
    ) {
        String nickname = accountRequest.nickname();
        String email = accountRequest.email();
        String password = accountRequest.password();

        accountServiceFacade.createAccountWithEmailVerification(nickname, email, password);
        return ResponseEntity.status(CREATED).body(new CreateAccountResponse(nickname, email));
    }

    @GetMapping("/account/{verificationToken}")
    public ResponseEntity<Void> verifyAccount(
            @PathVariable final String verificationToken
    ) {
        try {
            accountServiceFacade.tryEmailVerification(verificationToken);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (EmailVerificationExpiredException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }
}