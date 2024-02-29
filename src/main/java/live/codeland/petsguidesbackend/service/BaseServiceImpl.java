package live.codeland.petsguidesbackend.service;


import live.codeland.petsguidesbackend.dto.PaginationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T, ID> implements BaseService<T,ID> {
    protected MongoRepository<T,ID> repository;

    public BaseServiceImpl(MongoRepository<T,ID> repository){
        this.repository = repository;
    }

    @Override
    public PaginationDto<T> findAll(int page, int limit, String sortBy, String orderBy) {
        PageRequest pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Order.by(sortBy).with(Sort.Direction.fromString(orderBy))));
        Page<T> getAll = repository.findAll(pageable);
        List<T> list = getAll.getContent();
        long total = list.isEmpty()? 0: getAll.getTotalElements();
        int totalPages = list.isEmpty()? 0: getAll.getTotalPages();
        int currentPage = list.isEmpty()? 1: getAll.getNumber() + 1;
        Integer nextPage = getAll.hasNext() ? getAll.getNumber() + 2 : null;
        Integer prevPage = getAll.hasPrevious() && !list.isEmpty() ? getAll.getNumber() : null;
        return new PaginationDto<>(list, total, totalPages, currentPage, nextPage, prevPage);
    }
    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> saveAll(List<T> entities){
        return repository.saveAll(entities);
    }

    @Override
    public T save(T entity){
        return repository.save(entity);
    }


    @Override
    public List<T> updateAll(List<T> entities ){
        return repository.saveAll(entities);
    }

    @Override
    public T updateOne(T entity){
        return repository.save(entity);
    }

    @Override
    public List<T> softDeleteAll(List<T> entities){
        return repository.saveAll(entities);
    }

    @Override
    public T softDeleteOne(T entity){
       return repository.save(entity);
    }

}
