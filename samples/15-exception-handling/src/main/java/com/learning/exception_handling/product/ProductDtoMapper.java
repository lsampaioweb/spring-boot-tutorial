package com.learning.exception_handling.product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface ProductDtoMapper {

  @Mapping(target = "id", ignore = true)
  Product toEntity(ProductRequest request);

  ProductResponse toResponse(Product product);
}
