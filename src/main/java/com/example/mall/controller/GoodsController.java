package com.example.mall.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.mall.service.GoodsFileService;
import com.example.mall.service.GoodsService;
import com.example.mall.vo.GoodsFile;
import com.example.mall.vo.GoodsForm;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class GoodsController {
	@Autowired GoodsService goodsService;
	@Autowired GoodsFileService goodsFileService;
	
	// /admin/addGoods
	@GetMapping("/admin/addGoods")
	public String addGoods() {
		return "admin/addGoods";
	}
	@PostMapping("/admin/addGoods")
	public String addGoods(HttpSession session,Model model, GoodsForm goodsForm) {
		if(goodsForm.getGoodsFile() != null) {
			log.debug("GoodsFile size : "+goodsForm.getGoodsFile().size());
		}
		
		List<MultipartFile> list = goodsForm.getGoodsFile();
		if(list != null && list.size()!=0) { // 첨부파일 있다면
			for(MultipartFile f : list) { 
				if(!(f.getContentType().equals("image/jpeg") || f.getContentType().equals("image/png"))) {
					model.addAttribute("msg","이미지 파일만 첨부 가능합니다");
					return "on/addGoods";
				}
			}
		}
		String path = session.getServletContext().getRealPath("/upload/");
		goodsService.addGoods(goodsForm, path);
		
		return "redirect:/admin/goodsList";
	}
	
	@GetMapping("/admin/goodsList")
	public String goodsList(Model model
							,@RequestParam(defaultValue="1") Integer currentPage
							,@RequestParam(defaultValue="10") Integer rowPerPage
							,@RequestParam(defaultValue="") String searchGoods) {
		List<Map<String,Object>> result = goodsService.getGoodsList(currentPage, rowPerPage, searchGoods);
		Map<String,Object> resultMap = result.get(0); // list의 첫번째 map 추출
		
		if(!(searchGoods.equals(""))) {
			log.debug("searchGoods: "+searchGoods);
		}
		
		List<GoodsFile> goodsFileList = goodsFileService.getGoodsFileList();
		model.addAttribute("goodsFileList",goodsFileList);
		
		model.addAttribute("goodsList",resultMap.get("goodsList"));
		model.addAttribute("currentPage",currentPage);
		model.addAttribute("startPagingNum", resultMap.get("startPagingNum"));
		model.addAttribute("endPagingNum", resultMap.get("endPagingNum"));
		
		return "admin/goodsList";
	}
	
}
