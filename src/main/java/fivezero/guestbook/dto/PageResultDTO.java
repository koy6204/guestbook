package fivezero.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {

    private List<DTO> dtoList;

    private int totalPage;

    private int page; // 현재 페이지 번호

    private int size; // 목록사이즈

    private int start, end;

    private boolean prev, next;//이전, 다음

    private List<Integer> pageList; // 페이지번호 목록


    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){

        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {

        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1추가
        this.size = pageable.getPageSize();

        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());






    }
}
