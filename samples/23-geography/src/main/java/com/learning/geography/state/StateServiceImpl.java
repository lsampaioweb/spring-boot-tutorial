package com.learning.geography.state;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class StateServiceImpl implements StateService {

  private final StateRepository stateRepository;
  private final StateMapper stateMapper;

  StateServiceImpl(StateRepository stateRepository, StateMapper stateMapper) {
    this.stateRepository = stateRepository;
    this.stateMapper = stateMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public List<StateResponse> findAll() {
    return stateRepository.findAll().stream().map(stateMapper::toResponse).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public StateResponse findById(Long id) {
    var state = stateRepository.findById(id);
    if (state == null) {
      throw new StateNotFoundException(id);
    }
    return stateMapper.toResponse(state);
  }

  @Override
  @Transactional
  public StateResponse create(CreateStateRequest request) {
    var state = stateMapper.toEntity(request);
    var created = stateRepository.insert(state);
    return stateMapper.toResponse(created);
  }

  @Override
  @Transactional
  public StateResponse update(Long id, UpdateStateRequest request) {
    var state = stateRepository.findById(id);
    if (state == null) {
      throw new StateNotFoundException(id);
    }
    var updatedState = stateMapper.updateEntity(request, state);
    stateRepository.update(updatedState);
    return stateMapper.toResponse(updatedState);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    var state = stateRepository.findById(id);
    if (state == null) {
      throw new StateNotFoundException(id);
    }
    stateRepository.deleteById(id);
  }
}
