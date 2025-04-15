package live.codeland.petsguidesbackend.service;


import jakarta.persistence.EntityNotFoundException;
import live.codeland.petsguidesbackend.dto.PaginationDto;
import live.codeland.petsguidesbackend.model.Identifiable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
    public T updateOne(T entity, ID id) {
        Optional<T> optionalExisting = repository.findById(id);

        if (optionalExisting.isEmpty()) {
            throw new EntityNotFoundException("Entity not found for ID: " + id);
        }

        T existingEntity = optionalExisting.get();

        // Merge non-null values using reflection
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object newValue = field.get(entity);
                if (newValue != null) {
                    field.set(existingEntity, newValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }

        return repository.save(existingEntity);
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
