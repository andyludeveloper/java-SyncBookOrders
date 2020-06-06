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
        prepareDataList("Book", "CD", "Book");

        givenBookDao();
        _orderService.syncBookOrders();

        bookDaoShouldInsertOrders(2, "Book");
        bookDaoShouldNotBeInserted("CD");
    }

    private void givenBookDao() {
        _orderService.setBookDao(_bookDao);
    }

    private void bookDaoShouldNotBeInserted(String cd) {
        verify(_bookDao, never()).insert(argThat(argument -> argument.getType().equals(cd)));
    }

    private void bookDaoShouldInsertOrders(int count, String type) {
        verify(_bookDao, times(count)).insert(argThat(argument -> argument.getType().equals(type)));
    }

    private void prepareDataList(String type1, String type2, String type3) {
        List<Order> orderList = new ArrayList<>();
        Order order1 = new Order();
        order1.setType(type1);
        Order order2 = new Order();
        order2.setType(type2);
        Order order3 = new Order();
        order3.setType(type3);

        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        _orderService.setOrders(orderList);
    }

    private String order3() {
        return "Book";
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
