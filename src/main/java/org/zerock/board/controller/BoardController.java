package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

@Controller
@RequestMapping("/board/")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("BoardController.list() 실행 ... pageRequestDTO : " + pageRequestDTO);
        model.addAttribute("result", boardService.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register(){
        log.info("BoardController.register() 실행 ... ");
    }

    @PostMapping("/register") // 프론트에서 넘어온 dto
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes){
        log.info("BoardController.registerPost() 실행 ... dto : " + dto);
        Long bno = boardService.register(dto); // insert 후 bno 리턴
        log.info("bno : " + bno);
        redirectAttributes.addFlashAttribute("msg", bno);
        return "redirect:/board/list";
    }

    @GetMapping({"/read", "/modify"}) // 페이징&검색 정보와 bno를 받는다.
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model){
        log.info("BoardController./read, /modify 실행 ... bno : " + bno);
        BoardDTO boardDTO = boardService.get(bno); // bno를 이용해 DB에서 entity 객체를 가져와 DTO로 변환 후 DTO 리턴해준다.
        log.info("boardDTO : " + boardDTO);
        model.addAttribute("dto", boardDTO); // 모델 영역에 dto 저장
    }

    @PostMapping("/remove")
    public String remove(long bno, RedirectAttributes redirectAttributes){
        log.info("BoardController.remove() 실행 ... bno : " + bno);
        boardService.removeWithReplies(bno);
        redirectAttributes.addFlashAttribute("msg", bno);
        return "redirect:/board/list";
    }

    @PostMapping("/modify")
    public String modify(BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){
        log.info("BoardController.remove() 실행 ... dto : " + dto);
        boardService.modify(dto);
        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", dto.getBno());
        return "redirect:/board/read";
    }



}
