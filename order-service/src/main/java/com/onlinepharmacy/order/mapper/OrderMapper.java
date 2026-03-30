package com.onlinepharmacy.order.mapper;

import com.onlinepharmacy.order.dto.AddressDto;
import com.onlinepharmacy.order.dto.response.OrderCartResponseDto;
import com.onlinepharmacy.order.dto.response.OrderDetailsResponseDto;
import com.onlinepharmacy.order.dto.response.OrderItemResponseDto;
import com.onlinepharmacy.order.model.Order;

public final class OrderMapper {

  private OrderMapper() {}

  public static com.onlinepharmacy.order.model.Address toAddressEntity(AddressDto dto) {
    if (dto == null) return null;
    com.onlinepharmacy.order.model.Address address = new com.onlinepharmacy.order.model.Address();
    address.setLine1(dto.line1());
    address.setCity(dto.city());
    address.setPincode(dto.pincode());
    address.setState(dto.state());
    return address;
  }

  public static OrderCartResponseDto toCartResponseDto(Order order) {
    return new OrderCartResponseDto(
        order.getId(),
        order.getStatus(),
        order.getItems().stream().map(i -> new OrderItemResponseDto(
            i.getMedicineId(),
            i.getMedicineName(),
            i.getQuantity(),
            i.getUnitPrice()
        )).toList(),
        order.getTotalAmount()
    );
  }

  public static OrderDetailsResponseDto toDetailsResponseDto(Order order) {
    return new OrderDetailsResponseDto(
        order.getId(),
        order.getCustomerId(),
        order.getStatus(),
        order.getAddress() == null ? null : new AddressDto(
            order.getAddress().getLine1(),
            order.getAddress().getCity(),
            order.getAddress().getPincode(),
            order.getAddress().getState()
        ),
        order.getItems().stream().map(i -> new OrderItemResponseDto(
            i.getMedicineId(),
            i.getMedicineName(),
            i.getQuantity(),
            i.getUnitPrice()
        )).toList(),
        order.getTotalAmount(),
        order.getCreatedAt(),
        order.getUpdatedAt()
    );
  }
}
