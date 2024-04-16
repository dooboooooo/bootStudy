package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.service.ReplyService;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/replies/")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService; // 자동 주입을 위한 final 선언

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE) // localhost/replies/board/{}
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno){
        // url에서 bno 값을 받아 댓글들을 리턴한다.
        log.info("ReplyController.getListByBoard() ... bno : " + bno);
        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK); // ResponseEntity : http 상태코드 등을 같이 전달할 수 있는 객체
    }

    @PostMapping("") // replies/ : json 형태로 들어온다.(bno, text, replyer)
    // @RequestBody : json으로 들어오는 데이터를 자동으로 해당 타입의 객체로 매핑해주는 역할
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){
        log.info("ReplyController.getListByBoard() ... replyDTO : " + replyDTO);
        Long rno = replyService.register(replyDTO);
        return new ResponseEntity<>(rno, HttpStatus.OK);
    }

    @DeleteMapping("/{rno}") // delete !
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno){
        log.info("ReplyController.remove() ... rno : " + rno);
        replyService.remove(rno);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
