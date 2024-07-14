package com.learning.exception_handling.product;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/products")
@Slf4j
public class ProductController {

  private final MessageSource messageSource;
  private final ProductService service;

  public ProductController(ProductService service, MessageSource messageSource) {
    this.messageSource = messageSource;
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<Product>> findAll() {
    log.info(getMethodCalledMessage("findAll"));

    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> findById(@PathVariable Long id) {
    log.info(getMethodCalledMessage("findById"));

    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<Product> create(@RequestBody Product entity, UriComponentsBuilder uriBuilder) {
    log.info(getMethodCalledMessage("create"));

    Product createdEntity = service.create(entity);

    URI location = getLocation(uriBuilder, "/{id}", createdEntity.getId());

    return ResponseEntity.created(location).body(createdEntity);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product entity) {
    log.info(getMethodCalledMessage("update"));

    Product updatedEntity = service.update(id, entity);

    return ResponseEntity.ok(updatedEntity);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    log.info(getMethodCalledMessage("delete"));

    service.delete(id);

    return ResponseEntity.ok().build();
  }

  private URI getLocation(UriComponentsBuilder uriBuilder, String path, Object... uriVariableValues) {
    return uriBuilder.path(path).buildAndExpand(uriVariableValues).toUri();
  }

  private String getMethodCalledMessage(String methodName) {
    return messageSource.getMessage("method.called", new Object[] { methodName }, getLocale());
  }

  private Locale getLocale() {
    return LocaleContextHolder.getLocale();
  }
}
