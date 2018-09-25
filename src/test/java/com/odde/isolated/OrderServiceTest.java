package com.odde.isolated;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    OrderService target = spy(OrderService.class);
    IBookDao mockBookDao = mock(IBookDao.class);

    @Test
    public void syncbookorders_3_orders_only_2_book_order() {

        givenOrders(
                createOrder("Book"),
                createOrder("CD"),
                createOrder("Book")
        );

        givenBookDao();

        target.syncBookOrders();

        bookDaoShouldInsertOrders(2, "Book");
    }

    private void bookDaoShouldInsertOrders(int times, String type) {
        verify(mockBookDao, times(times)).insert(
                should(order -> assertThat(order.getType()).isEqualTo(type))
        );
    }

    private void givenBookDao() {
        when(target.getBookDao()).thenReturn(mockBookDao);
    }

    private Order createOrder(final String type) {
        return new Order() {{
            setType(type);
        }};
    }

    private void givenOrders(Order... orders) {
        when(target.getOrders()).thenReturn(Arrays.asList(orders));
    }

    public static <T> T should(Consumer<T> assertion) {
        return argThat(argument -> {
            assertion.accept(argument);
            return true;
        });
    }
}
