package life.majiang.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    // 是否显示上一页的icon(<)
    private boolean showPrevious;
    // 是否显示回到第一页的icon(<<)
    private boolean showFirstPage;
    // 是否显示下一页的icon(>)
    private boolean showNext;
    // 是否显示回到最后一页的icon(>>)
    private boolean showEndPage;
    // 记录当前页是哪一页
    private Integer page;
    private List<Integer> pages=new ArrayList<>();
    // 总共totalPage页
    private Integer totalPage;

    // 初始化pagination
    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage=totalPage;
        this.page=page;

        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }

            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        if(page==1){
            showPrevious=false;
        }
        else{
            showPrevious=true;
        }

        if(page==totalPage){
            showNext=false;
        }
        else{
            showNext=true;
        }

        if(pages.contains(1)){
            showFirstPage=false;
        }
        else {
            showFirstPage=true;
        }

        if(pages.contains(totalPage)){
            showEndPage=false;
        }
        else {
            showEndPage=true;
        }
    }
}
