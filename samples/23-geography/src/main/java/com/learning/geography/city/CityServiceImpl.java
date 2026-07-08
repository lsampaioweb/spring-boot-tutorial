package com.learning.geography.city;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class CityServiceImpl implements CityService {

  private final CityRepository cityRepository;
  private final CityMapper cityMapper;

  CityServiceImpl(CityRepository cityRepository, CityMapper cityMapper) {
    this.cityRepository = cityRepository;
    this.cityMapper = cityMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CityResponse> findAll() {
    return cityRepository.findAll().stream().map(cityMapper::toResponse).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public CityResponse findById(Long id) {
    var city = cityRepository.findById(id);
    if (city == null) {
      throw new CityNotFoundException(id);
    }
    return cityMapper.toResponse(city);
  }

  @Override
  @Transactional
  public CityResponse create(CreateCityRequest request) {
    var city = cityMapper.toEntity(request);
    var created = cityRepository.insert(city);
    return cityMapper.toResponse(created);
  }

  @Override
  @Transactional
  public CityResponse update(Long id, UpdateCityRequest request) {
    var city = cityRepository.findById(id);
    if (city == null) {
      throw new CityNotFoundException(id);
    }
    var updatedCity = cityMapper.updateEntity(request, city);
    cityRepository.update(updatedCity);
    return cityMapper.toResponse(updatedCity);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    var city = cityRepository.findById(id);
    if (city == null) {
      throw new CityNotFoundException(id);
    }
    cityRepository.deleteById(id);
  }
}
