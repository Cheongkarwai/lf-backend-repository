package com.lfhardware.sales.util;

import com.lfhardware.cart.dto.ItemDTO;
import com.lfhardware.order.dto.OrderItemInput;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class SalesUtil {

    public static BigDecimal calculateAfterSalesTax(BigDecimal subtotal){
        return subtotal;
    }

    public static BigDecimal calculateSubtotal(Set<ItemDTO> itemDTOS){
        return itemDTOS.stream().map(itemDTO -> itemDTO.price()).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

}
