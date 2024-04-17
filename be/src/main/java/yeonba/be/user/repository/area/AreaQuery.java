package yeonba.be.user.repository.area;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.user.entity.Area;

@Component
@RequiredArgsConstructor
public class AreaQuery {

    private final AreaRepository areaRepository;

    public Area findByName(String name) {

        return areaRepository.findByName(name)
            .orElseThrow(() -> new GeneralException(UserException.AREA_NOT_FOUND));
    }
}
