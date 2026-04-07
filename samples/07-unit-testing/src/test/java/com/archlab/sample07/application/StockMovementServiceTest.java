package com.archlab.sample07.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.archlab.sample07.application.contracts.CreateMovementRequest;
import com.archlab.sample07.domain.MovementType;
import com.archlab.sample07.domain.Product;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockMovementServiceTest {

    @Mock
    private ProductRepository productRepository;

    private StockMovementService service;
    private Product product;

    @BeforeEach
    void setUp() {
        service = new StockMovementService(productRepository);
        product = new Product();
        product.setId("prod-1");
        product.setName("Mouse");
        product.setCurrentBalance(10);
    }

    @Test
    void createEntry_increasesBalance() {
        when(productRepository.findById("prod-1")).thenReturn(Optional.of(product));

        var result = service.create(new CreateMovementRequest("prod-1", MovementType.ENTRY, 5));

        assertEquals(15, result.currentBalance());
        verify(productRepository).save(product);
    }

    @Test
    void createExit_decreasesBalance() {
        when(productRepository.findById("prod-1")).thenReturn(Optional.of(product));

        var result = service.create(new CreateMovementRequest("prod-1", MovementType.EXIT, 4));

        assertEquals(6, result.currentBalance());
    }

    @Test
    void createExit_withInsufficientBalance_throwsException() {
        when(productRepository.findById("prod-1")).thenReturn(Optional.of(product));

        var ex = assertThrows(IllegalStateException.class,
                () -> service.create(new CreateMovementRequest("prod-1", MovementType.EXIT, 20)));

        assertEquals("Insufficient stock balance.", ex.getMessage());
    }

    @Test
    void createExit_withExactBalance_succeeds() {
        product.setCurrentBalance(7);
        when(productRepository.findById("prod-1")).thenReturn(Optional.of(product));

        var result = service.create(new CreateMovementRequest("prod-1", MovementType.EXIT, 7));

        assertEquals(0, result.currentBalance());
    }

    @Test
    void create_withInvalidQuantity_throwsException() {
        var ex = assertThrows(IllegalArgumentException.class,
                () -> service.create(new CreateMovementRequest("prod-1", MovementType.ENTRY, 0)));

        assertEquals("Quantity must be greater than zero.", ex.getMessage());
    }

    @Test
    void create_withUnknownProduct_throwsNotFoundException() {
        when(productRepository.findById("missing")).thenReturn(Optional.empty());

        var ex = assertThrows(NotFoundException.class,
                () -> service.create(new CreateMovementRequest("missing", MovementType.ENTRY, 2)));

        assertEquals("Product not found.", ex.getMessage());
    }
}
