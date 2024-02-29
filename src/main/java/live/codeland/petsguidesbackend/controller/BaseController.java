package live.codeland.petsguidesbackend.controller;

import live.codeland.petsguidesbackend.dto.ApiResponseDto;
import live.codeland.petsguidesbackend.dto.PaginationDto;
import live.codeland.petsguidesbackend.service.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public abstract class BaseController<T, ID> {

    protected BaseService<T,ID> baseService;

    public BaseController(BaseService<T,ID> baseService){
        this.baseService = baseService;
    }

    public ResponseEntity<ApiResponseDto<PaginationDto<T>>> findAll(int page, int limit, String sortBy, String orderBy){
        try{
            PaginationDto<T> list =  baseService.findAll(page, limit, sortBy, orderBy);
            ApiResponseDto<PaginationDto<T>> response;
            if (!list.getList().isEmpty()) {
                response = new ApiResponseDto<>(HttpStatus.OK, 200, list, "Successfully Get All ",
                        LocalDateTime.now());
            } else {
                response = new ApiResponseDto<>(HttpStatus.NO_CONTENT, 204, null, "Not found in Base find All", LocalDateTime.now());
            }
            return response.toClient();

        } catch (Exception exception){
            ApiResponseDto<PaginationDto<T>> exceptionResponse = new ApiResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    500, null, "Catch in Base find all: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();
        }
    }


    public ResponseEntity<ApiResponseDto<Optional<T>>> findById(ID id){
        try {
            Optional<T> item = baseService.findById(id);
            ApiResponseDto<Optional<T>> response;
            if(!item.isEmpty()){
                response = new ApiResponseDto<>(HttpStatus.OK, 200, item, "Successfully find by ID ",
                        LocalDateTime.now());
            } else {
                response = new ApiResponseDto<>(HttpStatus.NOT_FOUND, 404, null, "Not found in Base find by ID", LocalDateTime.now());
            }
            return response.toClient();
        } catch (Exception exception){
            ApiResponseDto<Optional<T>> exceptionResponse = new ApiResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    500, null, "Catch in Base find by ID: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();
        }

    }


    public List<T> saveAll(List<T> entities){
        return baseService.saveAll(entities);
    }


    public T save(T entity){
        return baseService.save(entity);
    }


    public List<T> updateAll(List<T> entities ){
        return baseService.updateAll(entities);
    }


    public T updateOne(T entity){
        return baseService.updateOne(entity);
    }


    public List<T> softDeleteAll(List<T> entities){
        return baseService.softDeleteAll(entities);
    }


    public ResponseEntity<ApiResponseDto<T>> softDeleteOne(ID id){
        try {
            Optional<T> item = baseService.findById(id);
            ApiResponseDto<T> response;
            if(item.isPresent()){
                T existingEntity = item.get();
                Method setDeleted = existingEntity.getClass().getMethod("setDeleted", Boolean.class);
                setDeleted.invoke(existingEntity, true);

                Method setDeletedAt = existingEntity.getClass().getMethod("setDeletedAt", LocalDateTime.class);
                setDeletedAt.invoke(existingEntity, LocalDateTime.now());

                Method setUpdatedAt = existingEntity.getClass().getMethod("setUpdatedAt", LocalDateTime.class);
                setUpdatedAt.invoke(existingEntity, LocalDateTime.now());

               T deletedItem = baseService.softDeleteOne(existingEntity);
               response = new ApiResponseDto<>(HttpStatus.OK, 200, deletedItem, "Successfully Soft Delete by ID ",
                       LocalDateTime.now());
            } else {
                response = new ApiResponseDto<>(HttpStatus.NOT_FOUND, 404, null, "Not found the being deleted item", LocalDateTime.now());
            }
            return response.toClient();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception){
            ApiResponseDto<T> exceptionResponse = new ApiResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    500, null, "Catch in Base Soft Delete the reflect Method error: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();
        } catch (Exception exception){
            ApiResponseDto<T> exceptionResponse = new ApiResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR,
                    500, null, "Catch in Base Soft Delete by ID: " + exception.getMessage(), LocalDateTime.now());
            return exceptionResponse.toClient();
        }

    }
}
