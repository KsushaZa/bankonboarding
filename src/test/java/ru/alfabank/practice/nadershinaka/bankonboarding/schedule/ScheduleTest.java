package ru.alfabank.practice.nadershinaka.bankonboarding.schedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SchedulerAvailableProduct schedulerAvailableProduct;

    @Mock
    private Random random;

    @Test
    public void changeAvailable_ShouldAvailableAllNotAvailable() {
        Product p1 = new Product("1", "Orange Juce", 220, true);
        Product p2 = new Product("2", "Pomegranate Juce", 220, true);
        Product p3 = new Product("3", "Grape Juce", 220, true);
        Product p4 = new Product("4", "Water", 50, true);
        Product p5 = new Product("5", "Sparkle water", 120, false);
        Product p6 = new Product("6", "Wine", 899, false);

        List<Product> allProducts = Arrays.asList(p1, p2, p3, p4, p5, p6);

        when(productRepository.findAll()).thenReturn(allProducts);

        when(random.nextInt(6)).thenReturn(0).thenReturn(1);

        schedulerAvailableProduct.changeAvailable();

        ArgumentCaptor<List<Product>> captor = ArgumentCaptor.forClass(List.class);
        verify(productRepository).saveAll(captor.capture());

        Assertions.assertFalse(captor.getValue().get(0).isAvailable());
        Assertions.assertFalse(captor.getValue().get(1).isAvailable());
        Assertions.assertTrue(captor.getValue().get(2).isAvailable());
        Assertions.assertTrue(captor.getValue().get(3).isAvailable());
        Assertions.assertTrue(captor.getValue().get(4).isAvailable());
        Assertions.assertTrue(captor.getValue().get(5).isAvailable());

    }


    @Test
    void changeAvailable_WhenNoProducts_ShouldDoNothing() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        schedulerAvailableProduct.changeAvailable();
        verify(productRepository, never()).saveAll(any());
        assertDoesNotThrow(() -> schedulerAvailableProduct.changeAvailable());
        verify(productRepository, never()).saveAll(any());
    }

}


