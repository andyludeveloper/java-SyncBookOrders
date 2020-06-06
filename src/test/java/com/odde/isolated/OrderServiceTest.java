package com.odde.isolated;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class OrderServiceTest {
    private final IBookDao _bookDao = mock(IBookDao.class);
    private  FakeOrderService _orderService = new FakeOrderService();
    @Test
    public void syncbookorders_3_orders_only_2_book_order() {

        // hard to isolate dependency to unit test
        List<Order> orderList = new ArrayList<>();
        Order order1 = new Order();
        order1.setType("Book");
        Order order2 = new Order();
        order2.setType("CD");
        Order order3 = new Order();
        order3.setType("Book");

        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        _orderService.setBookDao(_bookDao);
        _orderService.setOrders(orderList);

        _orderService.syncBookOrders();
        //OrderService target = new OrderService();
        verify(_bookDao, times(2)).insert(argThat(argument -> argument.getType().equals("Book")));
        //target.SyncBookOrders();
    }

}

class FakeOrderService extends OrderService{
    List<Order> _orders;

    private IBookDao bookDao;

    public void setBookDao(IBookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    protected IBookDao getBookDao() {
        return bookDao;
    }

    public void setOrders(List<Order> orders) {
        this._orders = orders;
    }

    @Override
    protected List<Order> getOrders() {
        return _orders;
    }

}
