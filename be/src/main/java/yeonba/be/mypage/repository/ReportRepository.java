package yeonba.be.mypage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.mypage.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
