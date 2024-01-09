package live.codeland.petsguidesbackend.helpers.dto;

import java.util.List;

public class PaginationDto<T> {

    private final List<T> list;
    private final long total;
    private final int totalPages;
    private final int currentPage;
    private final Integer nextPage;
    private final Integer prevPage;

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
