package com.example.mall.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.mall.vo.Goods;

@Mapper
public interface GoodsMapper {
	// /admin/addGoods
	Integer insertGoods(Goods goods);
}
