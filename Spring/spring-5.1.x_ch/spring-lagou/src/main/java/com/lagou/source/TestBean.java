package com.lagou.source;

public class TestBean {
	private Integer id;
	private String username;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "TestBean{" +
				"id=" + id +
				", username='" + username + '\'' +
				'}';
	}
}
