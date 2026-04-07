package com.archlab.sample03.application.mappings;

import com.archlab.sample03.application.contracts.ProductResponse;
import com.archlab.sample03.domain.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toResponse(Product product);
}
