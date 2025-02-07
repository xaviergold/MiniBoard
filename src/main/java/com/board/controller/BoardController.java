package com.board.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.board.entity.Board;
import com.board.mapper.BoardMapper;
import com.board.util.Page;


@Controller 
@RequestMapping("/board/*") 

public class BoardController {
	
	Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	/*
	@Autowired
	 private miniBoardService service; //의존성 주입으로 객체 생성
	*/
	
	private BoardMapper service; 
	public BoardController(BoardMapper service) {
		this.service = service;
		
	}
	
	//미니 게시판 목록 보기
        //Source Change #1
        //Source Change #2
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String postList(HttpSession session, Model model, @RequestParam(name="page",required=false) int num, 
			@RequestParam(name="searchType", defaultValue="title", required=false) String searchType, 
			@RequestParam(name="keyword", defaultValue="", required=false) String keyword ) throws Exception {
		
		int postNum = 5; //한 페이지에 게시되는 게시물 갯수
		int listCount = 5; //페이지리스트에 게시되는 페이지 갯수
		int displayPost = (num - 1) * postNum; //읽어올 테이블 행의 위치
		
		Map<String, Object> data = new HashMap<>();
		data.put("displayPost", displayPost);
		data.put("postNum", postNum);
		data.put("searchType", searchType);
		data.put("keyword", keyword);
		
		List<Board> list = service.list(data);
		int totalCount = service.count(data);
		Page page = new Page();
		
		model.addAttribute("list", list);
		model.addAttribute("pageList", page.getPageList(num, postNum, listCount, totalCount, searchType, keyword));
		model.addAttribute("page", num);
		model.addAttribute("searchType", searchType);
		model.addAttribute("keyword", keyword);
		
		return null;
	}

	//미니 게시판 상세 내용 보기
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public void getMView(HttpSession session, Model model, @RequestParam(name="seqno", required=false) int seqno, 
			@RequestParam(name="page",required=false) int num, 
			@RequestParam(name="searchType", defaultValue="title", required=false) String searchType, 
			@RequestParam(name="keyword", defaultValue="", required=false) String keyword,			
			RedirectAttributes rttr) throws Exception {
		
		Board list = service.view(seqno);

		Map<String,Object> data = new HashMap<>();
		data.put("seqno", seqno);
		data.put("searchType", searchType);
		data.put("keyword", keyword);
		
		service.modifyViewcnt(list);
		
		int viewPrev = service.viewPrev(data); //이전 게시물 번호를 가져 온다.
		int viewNext = service.viewNext(data); //다음 게시물 정보를 가져 온다.
	
		model.addAttribute("list", list);
		model.addAttribute("page", num);
		model.addAttribute("viewPrev", viewPrev);
		model.addAttribute("viewNext", viewNext);
		model.addAttribute("searchType", searchType);
		model.addAttribute("keyword", keyword);

	}
	
	//미니 게시판 등록(GET)
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public void getMWrite(Board vo, HttpSession session) throws Exception {}

	//미니 게시판 등록(POST)
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String postMWrite(HttpSession session, Board vo) throws Exception {
		
		service.write(vo);
		return "redirect:/board/list?page=1";
		
	}

	//미니 게시판 내용 수정
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String getMModify(Model model, @RequestParam(name="seqno", required=false) int seqno,
			@RequestParam(name="page",required=false) int num, 
			@RequestParam(name="searchType", defaultValue="title", required=false) String searchType, 
			@RequestParam(name="keyword", defaultValue="", required=false) String keyword
			) throws Exception {
		
		Board list = service.view(seqno);
		
		model.addAttribute("list", list);
		model.addAttribute("page", num);
		model.addAttribute("searchType", searchType);
		model.addAttribute("keyword", keyword);
		
		return null;
		
	}

	//미니 게시판 내용 수정
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String postMModify(Model model, Board vo,
			@RequestParam(name="page",required=false) int num, 
			@RequestParam(name="searchType", defaultValue="title", required=false) String searchType, 
			@RequestParam(name="keyword", defaultValue="", required=false) String keyword
			) throws Exception {
		
		service.modify(vo);
		return "redirect:/board/view?seqno=" + vo.getSeqno() + "&page=" + Integer.toString(num)
		                 + "&searchType=" + searchType + "&keyword=" + keyword;	

	}

	//미니 게시판 내용 삭제
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String postDelete(Model model, @RequestParam(name="seqno", required=false) int seqno) throws Exception {
		
		service.delete(seqno);
		return "redirect:/board/list?page=1";	

	}
	
}
