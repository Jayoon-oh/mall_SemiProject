package com.example.mall.vo;

import lombok.Data;

@Data
public class Staff {
	private Integer staffNo; // pk
	private String staffId;
	private String staffPw;
	private String updateDate;
	private String createDate;
}