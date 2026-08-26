package com.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.board.entity.Board;

@Repository
@Mapper
public interface BoardMapper {

	//미니 게시판 게시물 등록
	 public void write(Board vo) throws Exception;

	 //미니 게시판 게시물 목록 보기
	 public List<Board> list(Map<String, Object> data) throws Exception;
	 
	 //미니 게시판 조회수 수정
	 public void modifyViewcnt(Board vo) throws Exception;
	 
	 //게시물 총 갯수
	 public int count(Map<String,Object> map) throws Exception;
	 
	 //게시물 이전 페이지
	 public int viewPrev(Map<String,Object> map) throws Exception;

	 //게시물 다음 페이지
	 public int viewNext(Map<String,Object> map) throws Exception;

	 //미니 게시판 게시물 수정
	 public void modify(Board vo) throws Exception;

	 //미니 게시판 게시물 삭제
	 public void delete(int seqno) throws Exception;

	//미니 게시판 게시물 내용 보기
	 public Board view(int seqno) throws Exception;	

}
