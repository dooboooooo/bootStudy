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
import org.zerock.board.dto.GuestbookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.GuestbookService;

@Controller // 컨트롤러 역할 지정
@RequestMapping("/guestbook") // http://localhost/guestbook/?
@Log4j2
@RequiredArgsConstructor // 자동 주입을 위한 어노테이션
public class GuestBookController {

    private final GuestbookService service; // 반드시 final로 선언해야함

    @GetMapping("/")
    public String index(){
        log.info("GuestBookController.index() 실행 ... ");
        return "redirect:/guestbook/list";
    }

    // 현재 페이지 번호, 목록 사이즈를 받아 service를 실행하고 model영역에 result 속성으로 저장 후 list.html로 이동
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("GuestBookController.list() 실행 ... ");
        model.addAttribute("result", service.getList(pageRequestDTO));
    }

    @GetMapping("/register")
    public void register(){
        log.info("GuestBookController.register() 실행 ... ");
    } // register.html로 이동

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){
        log.info("GuestBookController.registerPost() 실행 ... ");
        log.info("프론트에서 넘어온 dto : " + dto);
        // 프론트에서 넘겨받은 dto값을 service에서 entity 객체로 변환하여 저장하고 저장된 게시물의 gno를 리턴받는다.
        Long gno = service.register(dto);
        redirectAttributes.addFlashAttribute("msg", gno); // .addFlashAttribute : 1회성, url 뒤에 붙지 않음
        return "redirect:/guestbook/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        // long gno : 파라미터로 수집(gno)
        // PageRequestDTO requestDTO : 파라미터로 수집(page)
        // @ModelAttribute("requestDTO") : list.html에서 넘어오는 PageRequestDTO를 requestDTO라는 이름으로 처리한다.
        log.info("GuestBookController.상세보기, 수정 페이지 이동 메서드 실행 ... gno : " + gno);
        // service에서 gno를 통해 읽어온 entity 객체를 dto로 변환하여 리턴해줌
        GuestbookDTO dto = service.read(gno);
        // 모델 영역에 dto 라는 이름으로 속성 지정
        model.addAttribute("dto", dto);
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){
        log.info("GuestBookController.modify() 실행 ... dto : " + dto);
        service.modify(dto); // dto.getGno를 이용해 해당 게시물을 entity로 불러온 다음 제목,내용을 수정하고 DB에 저장
        redirectAttributes.addAttribute("page", requestDTO.getPage()); // 페이지 정보 유지 위함
        redirectAttributes.addAttribute("type", requestDTO.getType()); // 검색 조건 유지 위함
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword()); // 검색 조건 유지 위함
        redirectAttributes.addAttribute("gno", dto.getGno());
        return "redirect:/guestbook/read"; // 수정 후 상세보기로 이동(위의 page, gno를 가지고 감)
    }

    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes){
        log.info("GuestBookController.remove() 실행 ... gno : " + gno);
        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg", gno);
        return "redirect:/guestbook/list";
    }




}
