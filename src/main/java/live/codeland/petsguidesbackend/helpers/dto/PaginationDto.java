package live.codeland.petsguidesbackend.helpers.dto;

import java.util.List;

public class PaginationDto<T> {

    private List<T> list;
    private long total;
    private int totalPages;
    private int currentPage;
    private Integer nextPage;
    private Integer prevPage;

    public PaginationDto(List<T> list, long total, int totalPages, int currentPage, Integer nextPage, Integer prevPage) {
        this.list = list;
        this.total = total;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.nextPage = nextPage;
        this.prevPage = prevPage;
    }

    public List<T> getList() {
        return list;
    }

    public long getTotal() {
        return total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public Integer getPrevPage() {
        return prevPage;
    }
}
