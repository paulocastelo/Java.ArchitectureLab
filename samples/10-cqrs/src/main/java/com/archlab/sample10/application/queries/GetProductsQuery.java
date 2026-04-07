package com.archlab.sample10.application.queries;

import com.archlab.sample10.application.contracts.ProductResponse;
import com.archlab.sample10.cqrs.Query;
import java.util.List;

public record GetProductsQuery() implements Query<List<ProductResponse>> {
}
