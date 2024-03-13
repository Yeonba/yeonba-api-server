package yeonba.be.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.Block;

@Component
@RequiredArgsConstructor
public class BlockCommand {

  private final BlockRepository blockRepository;

  public Block save(Block block) {

    return blockRepository.save(block);
  }

}
