package yeonba.be.user.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.BlockException;
import yeonba.be.exception.GeneralException;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;

@Component
@RequiredArgsConstructor
public class BlockQuery {

    private final BlockRepository blockRepository;

    public Block findByUsers(User user, User blockedUser) {

        return blockRepository.findByUserAndBlockedUser(user, blockedUser)
            .orElseThrow(
                () -> new GeneralException(BlockException.ALREADY_BLOCKED_USER)
            );
    }

    public boolean isBlockExist(User user, User blockedUser) {

        return blockRepository.existsByUserAndBlockedUser(user, blockedUser);
    }

    public List<Block> findBlocksByUser(User user) {

        return blockRepository.findByUser(user);
    }
}
