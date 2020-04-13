package com.lwj.sell.service.impl;

import com.lwj.sell.dataobject.OrderDetail;
import com.lwj.sell.dto.OrderDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * create by lwj on 2020/4/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("lwj");
        orderDTO.setBuyerPhone("15229265351");
        orderDTO.setBuyerAddress("杭州");
        orderDTO.setBuyerOpenid("wxdasifhdwqafbcv");
        List<OrderDetail> orderDetailList = new ArrayList<>();
        orderDetailList.add(new OrderDetail("1", 2));
//        orderDetailList.add(new OrderDetail("2",1));
        orderDTO.setOrderDetailList(orderDetailList);
        orderService.create(orderDTO);
    }

    @Test
    public void findOne() {
        OrderDTO serviceOne = orderService.findOne("1586514683193917382");
        System.out.println(serviceOne);
    }

    @Test
    public void findList() {
        PageRequest pageRequest = PageRequest.of(0,5);
        Page<OrderDTO> list = orderService.findList(pageRequest);
        System.out.println(list.getContent());
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO = orderService.findOne("1586514683193917382");
        orderService.cancel(orderDTO);
    }

    @Test
    public void finish() {
    }

    @Test
    public void paid() {
    }

    @Test
    public void testFindList() {
    }
}