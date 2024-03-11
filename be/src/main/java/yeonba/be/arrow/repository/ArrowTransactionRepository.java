package yeonba.be.arrow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.arrow.entity.ArrowTransaction;

@Repository
public interface ArrowTransactionRepository extends JpaRepository<ArrowTransaction, Long> {

}
