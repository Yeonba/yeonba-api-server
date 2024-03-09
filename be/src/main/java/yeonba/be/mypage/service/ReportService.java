package yeonba.be.mypage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import yeonba.be.exception.ExceptionType;
import yeonba.be.exception.GeneralException;
import yeonba.be.mypage.entity.Report;
import yeonba.be.mypage.repository.ReportCommand;
import yeonba.be.user.dto.request.UserReportRequest;
import yeonba.be.user.entity.User;
import yeonba.be.user.service.UserService;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final String CATEGORY_ETC = "기타";
  private final int MAX_REPORT_REASON_LENGTH = 1024;

  private final UserService userService;
  private final ReportCommand reportCommand;

  /*
    신고 생성은 다음 과정을 거쳐 이뤄진다.
    1. 신고 요청 검증
    2. 자기 자신을 신고할 수 없는 상황 검증
    3. 신고한 유저, 신고 당한 유저 조회
    4. 신고 엔티티 생성 및 저장
   */
  @Transactional
  public void makeReport(
      long userId,
      long reportedUserId,
      UserReportRequest request) {

    checkReportRequest(request);
    if (userId == reportedUserId) {
      throw new GeneralException(ExceptionType.CAN_NOT_REPORT_SELF);
    }

    User user = userService.findById(userId);
    User reportedUser = userService.findById(reportedUserId);

    Report report = new Report(
        user,
        reportedUser,
        request.getCategory(),
        request.getReason());
    reportCommand.save(report);
  }

  /*
    신고 요청에 포함된 카테고리와 사유를 검증한다.
    - 카테고리가 기타가 아닐 경우 사유는 null이므로 검증하지 않는다.
    - 카테고리가 기타인데 사유가 존재하지 않는 경우 예외 발생
    - 카테고리가 기타인데 사유 길이가 1024자를 초과할 경우 예외 발생
   */
  private void checkReportRequest(UserReportRequest request) {

    String category = request.getCategory();
    String reason = request.getReason();

    if (!category.equals(CATEGORY_ETC)) {
      return;
    }

    if (!isReportReasonExist(reason)) {
      throw new GeneralException(ExceptionType.REPORT_REASON_NOT_EXIST);
    }

    if (!isReportReasonLessOrEqualThanMaxLength(reason)) {
      throw new GeneralException(ExceptionType.REPORT_REASON_LENGTH_NOT_VALID);
    }
  }

  private boolean isReportReasonExist(String reason) {

    return StringUtils.hasText(reason);
  }

  private boolean isReportReasonLessOrEqualThanMaxLength(String reason) {

    return reason.length() <= MAX_REPORT_REASON_LENGTH;
  }
}
