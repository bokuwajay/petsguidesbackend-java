package live.codeland.petsguidesbackend.helpers.dto;

import live.codeland.petsguidesbackend.model.User;

import java.util.List;

public class UserListDto {

    private List<User> userList;
    private long total;
    private int totalPages;
    private int currentPage;
    private Integer nextPage;
    private Integer prevPage;

    public UserListDto(List<User> userList, long total, int totalPages, int currentPage, Integer nextPage, Integer prevPage) {
        this.userList = userList;
        this.total = total;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.nextPage = nextPage;
        this.prevPage = prevPage;
    }

    public List<User> getUserList() {
        return userList;
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
