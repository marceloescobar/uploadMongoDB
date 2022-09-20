package com.mescobar.fileupload.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mescobar.fileupload.model.LoadFile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class FileService {

  @Autowired
  private GridFsTemplate template;

  @Autowired
  private GridFsOperations operations;

  /**
   * @param upload
   * @return
   * @throws IOException
   */
  public String addFile(MultipartFile upload) throws IOException {

    DBObject metadata = new BasicDBObject();
    metadata.put("fileSize", upload.getSize());

    Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(),
        upload.getContentType(), metadata);

    return fileID.toString();
  }

  /**
   * @param id
   * @return
   * @throws IOException
   */
  public LoadFile downloadFile(String id) {
    GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));
    return this.convert(gridFSFile);
  }

  /**
   * @return
   */
  public List<LoadFile> findAll() {
    List<GridFSFile> fileList = new ArrayList<GridFSFile>();
    template.find(new Query()).into(fileList);

    return fileList.stream().map(f -> this.convert(f)).collect(Collectors.toList());
  }


  /**
   * @param metadata
   * @param metadatavalue
   * @return
   */
  public List<LoadFile> find(String metadata, String metadatavalue) {
    List<GridFSFile> gridFSFiles = new ArrayList<GridFSFile>();
    template.find(new Query(Criteria.where(metadata).is(metadatavalue))).into(gridFSFiles);

    return gridFSFiles.stream().map(f -> this.convert(f)).collect(Collectors.toList());
  }

  /**
   * @param id
   */
  public void deleteFile(String id) {
    template.delete(new Query(Criteria.where("_id").is(id)));
  }


  /**
   * @param gridFSFile
   * @return
   */
  private LoadFile convert(GridFSFile gridFSFile) {
    LoadFile loadFile = new LoadFile();

    if (gridFSFile != null && gridFSFile.getMetadata() != null) {
      loadFile.setFilename(gridFSFile.getFilename());

      loadFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());

      loadFile.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());

      try {
        loadFile.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
      } catch (IllegalStateException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    return loadFile;
  }
}
