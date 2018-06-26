package com.odde.isolated;

import org.junit.Test;

import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private BookDao mockBookDao = mock(BookDao.class);

    OrderService target = spy(OrderService.class);

    @Test
    public void syncbookorders_3_orders_only_2_book_order() {
        // hard to isolate dependency to unit test
        givenOrders(new Order() {{
            setType("Book");
        }}, new Order() {{
            setType("CD");
        }}, new Order() {{
            setType("Book");
        }});
        when(target.getBookDao()).thenReturn(mockBookDao);

        target.syncBookOrders();

        verify(mockBookDao, times(2)).insert(
                should(order -> assertThat(order.getType()).isEqualTo("Book")));
    }

    private void givenOrders(Order... orders) {
        when(target.getOrders()).thenReturn(asList(orders));
    }

    public static <T> T should(Consumer<T> assertion) {
        return argThat(argument -> {
            assertion.accept(argument);
            return true;
        });
    }

}
