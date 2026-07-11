package com.learning.geography.state;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.geography.common.PagedResponse;
import com.learning.geography.i18n.LogMessages;

@Slf4j
@Service
class StateServiceImpl implements StateService {

  private static final String LOG_STATE_INSERTING = "log.state.inserting";
  private static final String LOG_STATE_UPDATING = "log.state.updating";
  private static final String LOG_STATE_DELETING = "log.state.deleting";

  private final StateRepository stateRepository;
  private final StateDtoMapper stateDtoMapper;
  private final LogMessages logMessages;

  StateServiceImpl(StateRepository stateRepository, StateDtoMapper stateDtoMapper, LogMessages logMessages) {
    this.stateRepository = stateRepository;
    this.stateDtoMapper = stateDtoMapper;
    this.logMessages = logMessages;
  }

  @Override
  @Transactional(readOnly = true)
  public PagedResponse<StateResponse> findAll(int page, int size) {
    int limit = size;
    int offset = page * size;
    List<StateResponse> items = stateRepository.findAll(limit, offset)
        .stream()
        .map(stateDtoMapper::toResponse)
        .toList();
    long totalElements = stateRepository.countAll();
    int totalPages = calculateTotalPages(totalElements, size);

    return new PagedResponse<>(items, page, size, totalElements, totalPages);
  }

  @Override
  @Transactional(readOnly = true)
  public StateResponse findById(Integer id) {
    State state = stateRepository.findById(id);
    if (state == null) {
      throw new StateNotFoundException(id);
    }

    return stateDtoMapper.toResponse(state);
  }

  @Override
  @Transactional
  public StateResponse create(CreateStateRequest request) {
    log.info(logMessages.get(LOG_STATE_INSERTING));
    State state = stateDtoMapper.toEntity(request);
    State created = stateRepository.insert(state);

    return stateDtoMapper.toResponse(created);
  }

  @Override
  @Transactional
  public StateResponse update(Integer id, UpdateStateRequest request) {
    log.info(logMessages.get(LOG_STATE_UPDATING, id));
    State state = stateRepository.findById(id);
    if (state == null) {
      throw new StateNotFoundException(id);
    }

    State updatedState = stateDtoMapper.updateEntity(request, state);
    stateRepository.update(updatedState);

    return stateDtoMapper.toResponse(updatedState);
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    log.info(logMessages.get(LOG_STATE_DELETING, id));
    int rowsAffected = stateRepository.deleteById(id);
    if (rowsAffected == 0) {
      throw new StateNotFoundException(id);
    }
  }

  private int calculateTotalPages(long totalElements, int size) {
    if (totalElements == 0) {
      return 0;
    }

    return (int) Math.ceil((double) totalElements / size);
  }
}
