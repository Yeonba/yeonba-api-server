package yeonba.be.user.repository.block;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class BlockQuery {

    private final BlockRepository blockRepository;

    public Optional<Block> findByUser(User user, User blockedUser) {

        return blockRepository.findByUserAndBlockedUser(user, blockedUser);
    }

    public boolean isBlockExist(User user, User blockedUser) {

        return blockRepository.existsByUserAndBlockedUser(user, blockedUser);
    }

    public List<Block> findBlocksByUser(User user) {

        return blockRepository.findByUser(user);
    }
}
