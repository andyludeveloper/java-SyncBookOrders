package com.odde.isolated;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Test
    public void syncbookorders_3_orders_only_2_book_order() {

        OrderService target = spy(OrderService.class);
        when(target.getOrders()).thenReturn(Arrays.asList(
                new Order() {{
                    setType("Book");
                }},
                new Order() {{
                    setType("CD");
                }},
                new Order() {{
                    setType("Book");
                }}
        ));

        IBookDao mockBookDao = mock(IBookDao.class);
        when(target.getBookDao()).thenReturn(mockBookDao);

        target.syncBookOrders();

        verify(mockBookDao, times(2)).insert(
                should(order -> assertThat(order.getType()).isEqualTo("Book"))
        );
    }

    public static <T> T should(Consumer<T> assertion) {
        return argThat(argument -> {
            assertion.accept(argument);
            return true;
        });
    }
}
