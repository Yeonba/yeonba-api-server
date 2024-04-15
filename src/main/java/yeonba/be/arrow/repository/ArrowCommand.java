package yeonba.be.arrow.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.arrow.entity.ArrowTransaction;

@Component
@RequiredArgsConstructor
public class ArrowCommand {

  private final ArrowTransactionRepository arrowTransactionRepository;

  public ArrowTransaction save(ArrowTransaction arrowTransaction) {

    return arrowTransactionRepository.save(arrowTransaction);
  }

}
