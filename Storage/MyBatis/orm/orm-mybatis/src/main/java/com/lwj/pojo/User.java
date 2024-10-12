package com.lwj.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 2021/11/16
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -990950995978225611L;
    private int id;
    private String username;
    private List<Role> roleList;
    private List<Order> orderList;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }
}
