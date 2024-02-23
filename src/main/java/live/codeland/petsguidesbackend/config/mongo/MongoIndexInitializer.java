package live.codeland.petsguidesbackend.config.mongo;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Collation;

@Configuration
public class MongoIndexInitializer implements InitializingBean {
    private final MongoTemplate mongoTemplate;

    public MongoIndexInitializer(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void afterPropertiesSet(){
        createUniqueIndex();
    }

    private void createUniqueIndex(){
        IndexOperations indexOperations = mongoTemplate.indexOps("user");
        Index index = new Index();
        index.on("email", Sort.Direction.ASC).unique().collation(Collation.of("en").strength(Collation.ComparisonLevel.secondary()));
        indexOperations.ensureIndex(index);
    }
}
