package com.archlab.sample10.application.queries;

import com.archlab.sample10.application.contracts.ProductResponse;
import com.archlab.sample10.cqrs.Query;

public record GetProductByIdQuery(String id) implements Query<ProductResponse> {
}
