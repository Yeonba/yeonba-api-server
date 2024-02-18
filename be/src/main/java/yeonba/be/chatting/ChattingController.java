package yeonba.be.chatting;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.util.CustomResponse;

@Tag(name = "User", description = "사용자 API")
@RestController
public class ChattingController {

    @GetMapping("/chattings")
    public ResponseEntity<CustomResponse<Void>> test() {

        return ResponseEntity
            .ok()
            .body(new CustomResponse<>());
    }
}
