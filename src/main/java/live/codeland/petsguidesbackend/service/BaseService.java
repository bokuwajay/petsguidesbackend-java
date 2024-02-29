package live.codeland.petsguidesbackend.service;

import live.codeland.petsguidesbackend.dto.PaginationDto;
import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {

    PaginationDto<T> findAll(int page, int limit, String sortBy, String orderBy);

    Optional<T> findById(ID id);

    List<T> saveAll(List<T> entities);

    T save(T entity);

    List<T> updateAll(List<T> entities);

    T updateOne(T entity);

    List<T> softDeleteAll(List<T> entities);

    T softDeleteOne(T entity);

}
