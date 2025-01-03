package skkunion.union2024.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @param email @Email(message = "이메일 형식에 적합하지 않습니다.")
 */
public record CreateAccountRequest(
        @NotBlank(message = "닉네임은 공백일 수 없습니다.")
        @Size(max = 15, message = "닉네임은 15자를 초과할 수 없습니다.")
        String nickname,
        // @Email(message = "이메일 형식에 적합하지 않습니다.")
        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        String email,
        @NotBlank(message = "패스워드는 공백일 수 없습니다.")
        @Size(min = 8, max = 20, message = "패스워드는 최소 8자, 최대 20자입니다.")
        String password) {
}
