package com.lwj._06_proxy_pattern._1staticProxy;

/**
 * create by lwj on 2019/1/19
 */
public class ProxyStar implements Star{
    private Star star;

    public ProxyStar(Star star)
    {
        this.star = star;
    }

    @Override
    public void confer() {
        System.out.println("代理 面谈");
    }

    @Override
    public void signContract() {
        System.out.println("代理 签合同");
    }

    @Override
    public void bookTicket() {
        System.out.println("代理 订票");
    }

    @Override
    public void sing() {
        star.sing();
    }

    @Override
    public void collectMoney() {
        System.out.println("代理 收钱");
    }
}
