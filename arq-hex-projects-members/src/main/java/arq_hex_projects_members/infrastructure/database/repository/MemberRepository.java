package arq_hex_projects_members.infrastructure.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import arq_hex_projects_members.infrastructure.database.entity.MemberEntity;

@EnableMongoRepositories
@Repository
public interface MemberRepository extends MongoRepository<MemberEntity, String> {

}
