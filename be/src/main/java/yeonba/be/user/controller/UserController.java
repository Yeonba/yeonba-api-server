package yeonba.be.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.user.dto.request.UserJoinRequest;
import yeonba.be.user.dto.response.UnwantedAcquaintanceResponse;
import yeonba.be.user.dto.response.UnwantedAcquaintancesResponse;
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

    @Operation(summary = "만나고 싶지 않은 지인 목록 조회", description = "만나고 싶지 않은 지인 목록을 조회합니다.")
    @GetMapping("/users/unwanted-acquaintances")
    public ResponseEntity<CustomResponse<UnwantedAcquaintancesResponse>> getUnwantedAcquaintances() {

        List<UnwantedAcquaintanceResponse> sampleUnwantedAcquaintances = Arrays.asList(
            new UnwantedAcquaintanceResponse("01012345678", "안민재"),
            new UnwantedAcquaintanceResponse("01087654321", "김민재")
        );

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>(new UnwantedAcquaintancesResponse(sampleUnwantedAcquaintances)));
    }

}
