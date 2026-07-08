package com.learning.geography.country;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class CountryServiceImpl implements CountryService {

  private final CountryRepository countryRepository;
  private final CountryMapper countryMapper;

  CountryServiceImpl(CountryRepository countryRepository, CountryMapper countryMapper) {
    this.countryRepository = countryRepository;
    this.countryMapper = countryMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CountryResponse> findAll() {
    return countryRepository.findAll().stream().map(countryMapper::toResponse).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public CountryResponse findById(Long id) {
    var country = countryRepository.findById(id);
    if (country == null) {
      throw new CountryNotFoundException(id);
    }
    return countryMapper.toResponse(country);
  }

  @Override
  @Transactional
  public CountryResponse create(CreateCountryRequest request) {
    var country = countryMapper.toEntity(request);
    var created = countryRepository.insert(country);
    return countryMapper.toResponse(created);
  }

  @Override
  @Transactional
  public CountryResponse update(Long id, UpdateCountryRequest request) {
    var country = countryRepository.findById(id);
    if (country == null) {
      throw new CountryNotFoundException(id);
    }
    var updatedCountry = countryMapper.updateEntity(request, country);
    countryRepository.update(updatedCountry);
    return countryMapper.toResponse(updatedCountry);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    var country = countryRepository.findById(id);
    if (country == null) {
      throw new CountryNotFoundException(id);
    }
    countryRepository.deleteById(id);
  }
}
