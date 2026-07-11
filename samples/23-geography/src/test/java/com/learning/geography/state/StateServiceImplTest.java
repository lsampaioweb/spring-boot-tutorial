package com.learning.geography.state;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.learning.geography.i18n.LogMessages;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.learning.geography.common.PagedResponse;

@ExtendWith(MockitoExtension.class)
class StateServiceImplTest {

  @Mock
  StateRepository stateRepository;

  @Mock
  StateDtoMapper stateDtoMapper;

  @Mock
  LogMessages logMessages;

  @InjectMocks
  StateServiceImpl stateService;

  @Test
  void findAll_whenCalled_shouldReturnPagedResponse() {
    State state1 = new State(1, 10, "São Paulo", "SP");
    State state2 = new State(2, 10, "Rio de Janeiro", "RJ");
    StateResponse response1 = new StateResponse(1, 10, "São Paulo", "SP");
    StateResponse response2 = new StateResponse(2, 10, "Rio de Janeiro", "RJ");

    BDDMockito.given(stateRepository.findAll(20, 0)).willReturn(List.of(state1, state2));
    BDDMockito.given(stateRepository.countAll()).willReturn(2L);
    BDDMockito.given(stateDtoMapper.toResponse(state1)).willReturn(response1);
    BDDMockito.given(stateDtoMapper.toResponse(state2)).willReturn(response2);

    PagedResponse<StateResponse> result = stateService.findAll(0, 20);

    assertThat(result.items()).hasSize(2);
    assertThat(result.items()).containsExactly(response1, response2);
    assertThat(result.totalElements()).isEqualTo(2);
    assertThat(result.totalPages()).isEqualTo(1);
  }

  @Test
  void findById_whenStateExists_shouldReturnResponse() {
    State state = new State(1, 10, "São Paulo", "SP");
    StateResponse expected = new StateResponse(1, 10, "São Paulo", "SP");

    BDDMockito.given(stateRepository.findById(1)).willReturn(state);
    BDDMockito.given(stateDtoMapper.toResponse(state)).willReturn(expected);

    StateResponse result = stateService.findById(1);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void findById_whenStateNotFound_shouldThrowStateNotFoundException() {
    BDDMockito.given(stateRepository.findById(999)).willReturn(null);

    assertThatThrownBy(() -> stateService.findById(999))
        .isInstanceOf(StateNotFoundException.class);
  }

  @Test
  void create_whenValidRequest_shouldInsertAndReturnResponse() {
    CreateStateRequest request = new CreateStateRequest(10, "São Paulo", "SP");
    State entity = new State(null, 10, "São Paulo", "SP");
    State created = new State(1, 10, "São Paulo", "SP");
    StateResponse expected = new StateResponse(1, 10, "São Paulo", "SP");

    BDDMockito.given(stateDtoMapper.toEntity(request)).willReturn(entity);
    BDDMockito.given(stateRepository.insert(entity)).willReturn(created);
    BDDMockito.given(stateDtoMapper.toResponse(created)).willReturn(expected);

    StateResponse result = stateService.create(request);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void update_whenStateExists_shouldUpdateAndReturnResponse() {
    UpdateStateRequest request = new UpdateStateRequest(10, "São Paulo Updated", "SU");
    State existing = new State(1, 10, "São Paulo", "SP");
    State updated = new State(1, 10, "São Paulo Updated", "SU");
    StateResponse expected = new StateResponse(1, 10, "São Paulo Updated", "SU");

    BDDMockito.given(stateRepository.findById(1)).willReturn(existing);
    BDDMockito.given(stateDtoMapper.updateEntity(request, existing)).willReturn(updated);
    BDDMockito.given(stateDtoMapper.toResponse(updated)).willReturn(expected);

    StateResponse result = stateService.update(1, request);

    assertThat(result).isEqualTo(expected);
  }

  @Test
  void update_whenStateNotFound_shouldThrowStateNotFoundException() {
    BDDMockito.given(stateRepository.findById(999)).willReturn(null);
    UpdateStateRequest request = new UpdateStateRequest(10, "São Paulo", "SP");

    assertThatThrownBy(() -> stateService.update(999, request))
        .isInstanceOf(StateNotFoundException.class);
  }

  @Test
  void delete_whenStateExists_shouldCallDeleteById() {
    BDDMockito.given(stateRepository.deleteById(1)).willReturn(1);

    stateService.delete(1);

    Mockito.verify(stateRepository).deleteById(1);
  }

  @Test
  void delete_whenStateNotFound_shouldThrowStateNotFoundException() {
    BDDMockito.given(stateRepository.deleteById(999)).willReturn(0);

    assertThatThrownBy(() -> stateService.delete(999))
        .isInstanceOf(StateNotFoundException.class);
  }
}
