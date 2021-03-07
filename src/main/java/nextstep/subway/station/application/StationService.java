package nextstep.subway.station.application;

import nextstep.subway.common.exception.ExistResourceException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {

    private static final String EXCEPTION_MESSAGE_EXIST_STATION_NAME = "존재하는 지하철 역 입니다.";

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        validateStationName(stationRequest.getName());

        Station savedStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(savedStation);
    }

    private void validateStationName(String stationName) {
        if (stationRepository.existsByName(stationName)) {
            throw new ExistResourceException(EXCEPTION_MESSAGE_EXIST_STATION_NAME);
        }
    }

   @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
