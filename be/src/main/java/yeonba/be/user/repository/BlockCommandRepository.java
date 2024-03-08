package yeonba.be.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.Block;

@Repository
public interface BlockCommandRepository extends JpaRepository<Block, Long> {

}
