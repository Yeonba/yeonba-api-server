package yeonba.be.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UserUpdateProfilePhotoRequest {

    @Schema(
        type = "array",
        description = "수정할 프로필 사진들")
    @Size(min = 2, max = 2, message = "프로필 사진은 정확히 2장이어야 합니다.")
    @NotNull(message = "프로필 사진은 반드시 입력되어야 합니다.")
    List<MultipartFile> profilePhotos;
}
