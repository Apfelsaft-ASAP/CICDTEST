package com.example.seoulcultureport.controller;

import com.example.seoulcultureport.dto.*;
import com.example.seoulcultureport.dto.boardDto.BoardDetailResponseDto;
import com.example.seoulcultureport.dto.boardDto.BoardListResponseDto;
import com.example.seoulcultureport.dto.boardDto.BoardRequestDto;
import com.example.seoulcultureport.dto.boardDto.BoardSimpleResponseDto;
import com.example.seoulcultureport.entity.User;
import com.example.seoulcultureport.security.UserDetailsImpl;
import com.example.seoulcultureport.service.BoardService;
import com.example.seoulcultureport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//반환타입 Enum 으로 바꾸거나 Exception 처리하기 !
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {  //원트가능?//원트실패

    private final BoardService boardService;
    private final UserService userService;

    // 글생성
    @PostMapping("/")
    public MessageResponseDto writeBoard(
            @RequestBody @Valid BoardRequestDto boardRequestDto,
            @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        return boardService.writeBoard(boardRequestDto, userDetails.getUser());
    }

    // 내 게시글 수정
    @PutMapping("/{boardId}")
    public MessageResponseDto updateBoard(
            @PathVariable Long boardId,
            @RequestBody @Valid BoardRequestDto boardRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updateBoard(boardId, boardRequest, userDetails.getUser());
    }

    // 내 게시글 삭제
    @DeleteMapping("/{boardId}")
    public MessageResponseDto deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(boardId, userDetails.getUser());
    }

    //메인페이지 전체 글 [리스트] 조회 (토큰x)
    @GetMapping("/list")
    public List<BoardListResponseDto> getBoardList() {
        return boardService.getBoardList();
    }

    // 상세페이지 (토큰 x)
    @GetMapping("/detail/{boardId}")
    public BoardDetailResponseDto getBoardDetailList(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = null;
        if(userDetails != null) {
            user = userService.getUserByUsername(userDetails.getUsername());
        }
        return boardService.getBoardDetailList(boardId, user);
    }

    // 내 게시글 [리스트] (토큰 o)
    @GetMapping("/mylist")
    public List<BoardSimpleResponseDto> getBoardMyList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.getBoardMyList(userDetails.getUser());
    }

    @PostMapping("/{boardId}/thumbsup")
    public ThumbsupResponseDto addThumbsup(@PathVariable Long boardId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return boardService.addThumbsup(boardId, userDetails.getUser());
    }
}
