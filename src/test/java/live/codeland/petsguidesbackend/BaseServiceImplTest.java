package live.codeland.petsguidesbackend;

import jakarta.persistence.EntityNotFoundException;
import live.codeland.petsguidesbackend.dto.PaginationDto;
import live.codeland.petsguidesbackend.service.BaseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BaseServiceImplTest {

    private BaseServiceImpl<TestEntity, String> baseService;

    @Mock
    private MongoRepository<TestEntity, String> repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        baseService = new BaseServiceImpl<>(repository) {};
    }

    @Test
    void testFindAll() {
        List<TestEntity> data = Arrays.asList(new TestEntity("1", "Title A"), new TestEntity("2", "Title B"));
        Page<TestEntity> mockPage = new PageImpl<>(data);
        when(repository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        PaginationDto<TestEntity> result = baseService.findAll(1, 10, "title", "ASC");

        assertEquals(2, result.getList().size());
        assertEquals(2, result.getTotal());
        assertEquals(1, result.getCurrentPage());
        verify(repository).findAll(any(PageRequest.class));
    }

    @Test
    void testFindById() {
        TestEntity entity = new TestEntity("1", "Title A");
        when(repository.findById("1")).thenReturn(Optional.of(entity));

        Optional<TestEntity> result = baseService.findById("1");

        assertTrue(result.isPresent());
        assertEquals("Title A", result.get().getTitle());
    }

    @Test
    void testSaveAll() {
        List<TestEntity> entities = List.of(new TestEntity("1", "One"), new TestEntity("2", "Two"));
        when(repository.saveAll(entities)).thenReturn(entities);

        List<TestEntity> saved = baseService.saveAll(entities);

        assertEquals(2, saved.size());
        verify(repository).saveAll(entities);
    }

    @Test
    void testSave() {
        TestEntity entity = new TestEntity("1", "Saved");
        when(repository.save(entity)).thenReturn(entity);

        TestEntity saved = baseService.save(entity);

        assertEquals("Saved", saved.getTitle());
    }

    @Test
    void testUpdateOne_success() {
        TestEntity original = new TestEntity("1", "Old Title");
        TestEntity update = new TestEntity("1", "New Title");

        when(repository.findById("1")).thenReturn(Optional.of(original));
        when(repository.save(any(TestEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TestEntity result = baseService.updateOne(update, "1");

        assertEquals("New Title", result.getTitle());
    }

    @Test
    void testUpdateOne_entityNotFound() {
        TestEntity update = new TestEntity("1", "New Title");
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> baseService.updateOne(update, "1"));
    }

    @Test
    void testSoftDeleteAll() {
        List<TestEntity> list = List.of(new TestEntity("1", "Deleted"));
        when(repository.saveAll(list)).thenReturn(list);

        List<TestEntity> result = baseService.softDeleteAll(list);

        assertEquals(1, result.size());
        verify(repository).saveAll(list);
    }

    @Test
    void testSoftDeleteOne() {
        TestEntity entity = new TestEntity("1", "Soft Deleted");
        when(repository.save(entity)).thenReturn(entity);

        TestEntity result = baseService.softDeleteOne(entity);

        assertEquals("Soft Deleted", result.getTitle());
    }

    // Inner static class for testing
    static class TestEntity {
        private String id;
        private String title;

        public TestEntity() {}

        public TestEntity(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
