package com.lagou.edu;

/**
 * @author 应癫
 */
public class ItBean {

	private LagouBean lagouBean;

	public void setLagouBean(LagouBean lagouBean) {
		this.lagouBean = lagouBean;
	}

	/**
	 * 构造函数
	 */
	public ItBean(){
		System.out.println("ItBean 构造器...");
	}
}
