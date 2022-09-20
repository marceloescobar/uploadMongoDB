package com.mescobar.fileupload.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.mescobar.fileupload.model.Photo;

public interface PhotoRepository extends MongoRepository<Photo, String> {

}
