package com.learning.geography.country;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.geography.common.PagedResponse;
import com.learning.geography.i18n.LogMessages;

@Slf4j
@Service
class CountryServiceImpl implements CountryService {

  private static final String LOG_COUNTRY_INSERTING = "log.country.inserting";
  private static final String LOG_COUNTRY_UPDATING = "log.country.updating";
  private static final String LOG_COUNTRY_DELETING = "log.country.deleting";

  private final CountryRepository countryRepository;
  private final CountryDtoMapper countryDtoMapper;
  private final LogMessages logMessages;

  CountryServiceImpl(CountryRepository countryRepository, CountryDtoMapper countryDtoMapper, LogMessages logMessages) {
    this.countryRepository = countryRepository;
    this.countryDtoMapper = countryDtoMapper;
    this.logMessages = logMessages;
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResponse<CountryResponse> findAll(int page, int size) {
    int limit = size;
    int offset = page * size;
    List<CountryResponse> items = countryRepository.findAll(limit, offset)
        .stream()
        .map(countryDtoMapper::toResponse)
        .toList();
    long totalElements = countryRepository.countAll();
    int totalPages = calculateTotalPages(totalElements, size);

    return new PagedResponse<>(items, page, size, totalElements, totalPages);
  }

  @Override
  @Transactional(readOnly = true)
  public CountryResponse findById(Integer id) {
    Country country = countryRepository.findById(id);
    if (country == null) {
      throw new CountryNotFoundException(id);
    }

    return countryDtoMapper.toResponse(country);
  }

  @Override
  @Transactional
  public CountryResponse create(CreateCountryRequest request) {
    log.info(logMessages.get(LOG_COUNTRY_INSERTING));
    Country country = countryDtoMapper.toEntity(request);
    Country created = countryRepository.insert(country);

    return countryDtoMapper.toResponse(created);
  }

  @Override
  @Transactional
  public CountryResponse update(Integer id, UpdateCountryRequest request) {
    log.info(logMessages.get(LOG_COUNTRY_UPDATING, id));
    Country country = countryRepository.findById(id);
    if (country == null) {
      throw new CountryNotFoundException(id);
    }

    Country updatedCountry = countryDtoMapper.updateEntity(request, country);
    countryRepository.update(updatedCountry);

    return countryDtoMapper.toResponse(updatedCountry);
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    log.info(logMessages.get(LOG_COUNTRY_DELETING, id));
    int rowsAffected = countryRepository.deleteById(id);
    if (rowsAffected == 0) {
      throw new CountryNotFoundException(id);
    }
  }

  private int calculateTotalPages(long totalElements, int size) {
    if (totalElements == 0) {
      return 0;
    }

    return (int) Math.ceil((double) totalElements / size);
  }
}
