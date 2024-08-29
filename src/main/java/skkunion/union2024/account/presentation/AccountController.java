package skkunion.union2024.account.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import skkunion.union2024.account.dto.request.createAccountRequest;
import skkunion.union2024.account.dto.response.createAccountResponse;
import skkunion.union2024.account.service.AccountServiceFacade;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class AccountController {

    public final AccountServiceFacade accountServiceFacade;

    @PostMapping("/account")
    public ResponseEntity<createAccountResponse> createAccount(
            @RequestBody final createAccountRequest accountRequest
    ) {
        String nickname = accountRequest.nickname();
        String email = accountRequest.email();
        String password = accountRequest.password();

        accountServiceFacade.createAccountWithEmailVerification(nickname, email, password);
        return ResponseEntity.status(CREATED).body(new createAccountResponse(nickname, email));
    }
}
