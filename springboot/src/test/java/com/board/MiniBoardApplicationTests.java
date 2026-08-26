package com.board;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.board.mapper.BoardMapper;

@SpringBootTest
class MiniBoardApplicationTests {

	@Autowired
	BoardMapper service;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void printBoardRowCount() {
		
		Map<String, Object> data = new HashMap<>();
		data.put("searchType", "title");
		data.put("keyword", "");
		
		try {
			System.out.println("게시물 총갯수 : " + service.count(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
