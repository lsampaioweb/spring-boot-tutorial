package com.learning.geography.city;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.geography.common.PagedResponse;
import com.learning.geography.i18n.LogMessages;

@Slf4j
@Service
class CityServiceImpl implements CityService {

  private static final String LOG_CITY_INSERTING = "log.city.inserting";
  private static final String LOG_CITY_UPDATING = "log.city.updating";
  private static final String LOG_CITY_DELETING = "log.city.deleting";

  private final CityRepository cityRepository;
  private final CityDtoMapper cityDtoMapper;
  private final LogMessages logMessages;

  CityServiceImpl(CityRepository cityRepository, CityDtoMapper cityDtoMapper, LogMessages logMessages) {
    this.cityRepository = cityRepository;
    this.cityDtoMapper = cityDtoMapper;
    this.logMessages = logMessages;
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResponse<CityResponse> findAll(int page, int size) {
    int limit = size;
    int offset = page * size;
    List<CityResponse> items = cityRepository.findAll(limit, offset).stream()
        .map(cityDtoMapper::toResponse)
        .toList();
    long totalElements = cityRepository.countAll();
    int totalPages = calculateTotalPages(totalElements, size);
    return new PagedResponse<>(items, page, size, totalElements, totalPages);
  }

  @Override
  @Transactional(readOnly = true)
  public CityResponse findById(Integer id) {
    City city = cityRepository.findById(id);
    if (city == null) {
      throw new CityNotFoundException(id);
    }
    return cityDtoMapper.toResponse(city);
  }

  @Override
  @Transactional
  public CityResponse create(CreateCityRequest request) {
    log.info(logMessages.get(LOG_CITY_INSERTING));
    City city = cityDtoMapper.toEntity(request);
    City created = cityRepository.insert(city);
    return cityDtoMapper.toResponse(created);
  }

  @Override
  @Transactional
  public CityResponse update(Integer id, UpdateCityRequest request) {
    log.info(logMessages.get(LOG_CITY_UPDATING, id));
    City city = cityRepository.findById(id);
    if (city == null) {
      throw new CityNotFoundException(id);
    }
    City updatedCity = cityDtoMapper.updateEntity(request, city);
    cityRepository.update(updatedCity);
    return cityDtoMapper.toResponse(updatedCity);
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    log.info(logMessages.get(LOG_CITY_DELETING, id));
    int rowsAffected = cityRepository.deleteById(id);
    if (rowsAffected == 0) {
      throw new CityNotFoundException(id);
    }
  }

  private int calculateTotalPages(long totalElements, int size) {
    if (totalElements == 0) {
      return 0;
    }
    return (int) Math.ceil((double) totalElements / size);
  }
}
