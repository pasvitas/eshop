package ru.pasvitas.eshop.service;

import java.util.List;
import ru.pasvitas.eshop.model.Order;

public interface ProfileService {
    List<Order> getOrders(String person);
}
