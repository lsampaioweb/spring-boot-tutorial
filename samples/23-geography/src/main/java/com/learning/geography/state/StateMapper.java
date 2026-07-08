package com.learning.geography.state;

import org.springframework.stereotype.Component;

@Component
class StateMapper {

  StateResponse toResponse(State state) {
    return new StateResponse(state.id(), state.countryId(), state.name(), state.abbreviation());
  }

  State toEntity(CreateStateRequest request) {
    return new State(null, request.countryId(), request.name(), request.abbreviation());
  }

  State updateEntity(UpdateStateRequest request, State state) {
    return new State(state.id(), request.countryId(), request.name(), request.abbreviation());
  }
}
