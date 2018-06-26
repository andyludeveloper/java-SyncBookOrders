package com.odde.isolated;

import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private BookDao mockBookDao = mock(BookDao.class);

    @Test
    public void syncbookorders_3_orders_only_2_book_order() {
        // hard to isolate dependency to unit test
        OrderService target = new OrderServiceForTest();

        target.syncBookOrders();

        verify(mockBookDao, times(2)).insert(
                should(order -> assertThat(order.getType()).isEqualTo("Book")));
    }

    public static <T> T should(Consumer<T> assertion) {
        return argThat(argument -> {
            assertion.accept(argument);
            return true;
        });
    }

    public class OrderServiceForTest extends OrderService {

        @Override
        protected List<Order> getOrders() {
            return asList(new Order() {{
                setType("Book");
            }}, new Order() {{
                setType("CD");
            }}, new Order() {{
                setType("Book");
            }});
        }

        @Override
        protected BookDao getBookDao() {
            return mockBookDao;
        }
    }
}
