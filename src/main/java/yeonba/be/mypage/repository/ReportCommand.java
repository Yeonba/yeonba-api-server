package yeonba.be.mypage.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.mypage.entity.Report;

@Component
@RequiredArgsConstructor
public class ReportCommand {

  private final ReportRepository reportRepository;

  public Report save(Report report) {

    return reportRepository.save(report);
  }

}
