package yeonba.be.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.user.dto.request.UserJoinRequest;
import yeonba.be.user.dto.response.UserJoinResponse;
import yeonba.be.util.CustomResponse;

@Tag(name = "User", description = "사용자 API")
@RestController
public class UserController {

    @GetMapping("/users")
    public ResponseEntity<CustomResponse<Void>> test() {

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>());
    }

    @Operation(summary = "회원가입", description = "회원가입을 할 수 있습니다.")
    @PostMapping("/users/join")
    public ResponseEntity<CustomResponse<UserJoinResponse>> join(
        @RequestBody UserJoinRequest request) {

        // TODO: 회원가입 로직 구현
        String createdJwt = "created";

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(new UserJoinResponse(createdJwt)));
    }
}
