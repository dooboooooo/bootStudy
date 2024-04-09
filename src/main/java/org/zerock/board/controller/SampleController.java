package org.zerock.board.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.SampleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//@RestController    // 기본 json으로 처리한다.
@Controller
@RequestMapping("/sample") // http://localhost/sample/?
@Log4j2
public class SampleController {

    @GetMapping("/hello")
    public String[] hello(){
        return new String[]{"Hello", "World"};
    }

    @GetMapping("/ex1") // http://localhost/sample/ex1.html -> void는 같은 경로와 파일.html을 찾는다.
    public void ex1(){
        log.info("ex1 메서드 실행 ... ");
        // resources/templates/sample/ex1.html
    }

//    @GetMapping("/ex2") // http://localhost/sample/ex2.html
//    public void exModel(Model model){
//        // Spring은 Model 타입으로 모든 객체나 데이터를 가지고 있다.
//        List<SampleDTO> list = IntStream.rangeClosed(1,20).asLongStream().mapToObj(i -> {
//            SampleDTO dto = SampleDTO.builder()
//                    .sno(i)
//                    .first("첫번째 필드 ..." + i)
//                    .last("마지막 필드 ..." + i)
//                    .regTime(LocalDateTime.now())
//                    .build(); // 빌더 패턴을 이용해서 값을 리스트로 만든다.
//            return dto;
//        }).collect(Collectors.toList()); // 문법(db를 사용하지 않을 때) // 리스트 완성!
//        // model.addAllAttributes() : 모델에 여러 객체를 추가할 때 사용
//        model.addAttribute("list", list); // : 모델에 하나의 객체를 추가할 때 사용
//        // 프론트에서 list를 호출하면 list 객체가 호출된다.
//    } 아래와 같이 수정함

    @GetMapping({"/ex2", "/exLink"})
    // http://localhost/sample/ex2.html
    // http://localhost/sample/exLink.html
    public void exModel(Model model){
        // Spring은 Model 타입으로 모든 객체나 데이터를 가지고 있다.
        List<SampleDTO> list = IntStream.rangeClosed(1,20).asLongStream().mapToObj(i -> {
            SampleDTO dto = SampleDTO.builder()
                    .sno(i)
                    .first("첫번째 필드 ..." + i)
                    .last("마지막 필드 ..." + i)
                    .regTime(LocalDateTime.now())
                    .build(); // 빌더 패턴을 이용해서 값을 리스트로 만든다.
            return dto;
        }).collect(Collectors.toList()); // 문법(db를 사용하지 않을 때) // 리스트 완성!
        // model.addAllAttributes() : 모델에 여러 객체를 추가할 때 사용
        model.addAttribute("list", list); // : 모델에 하나의 객체를 추가할 때 사용
        // 프론트에서 list를 호출하면 list 객체가 호출된다.
    }

    @GetMapping({"/exInline"}) // http://localhost/sample/exInline.html
    public String exInline(RedirectAttributes redirectAttributes){
        SampleDTO dto = SampleDTO.builder()
                .sno(100L)
                .first("First .. 100")
                .last("Last .. 100")
                .regTime(LocalDateTime.now())
                .build();
        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("dto", dto);
        return "redirect:/sample/ex3"; // 위에서 설정한 속성값을 가지고 /sample/ex3으로 이동
    }

    @GetMapping("/ex3") // http://localhost/sample/ex3.html
    public void ex3(){
        log.info("ex3메서드 실행중 ... ");
    }

//    @GetMapping("/exLayout1") // http://localhost/sample/exLayout1.html
//    public void exLayout1(){
//        log.info("exLayout1 메서드 실행중 ...");
//    } 아래와 같이 수정함

    @GetMapping({"/exLayout1", "/exLayout2", "/exTemplate", "/exSidebar"})
    // http://localhost/sample/exLayout1.html
    // http://localhost/sample/exLayout2.html
    // http://localhost/sample/exTemplate.html
    public void exLayout1(){
        log.info("exLayout1 메서드 실행중 ...");
    }



} // class
