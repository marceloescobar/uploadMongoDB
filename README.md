"# uploadMongoDB" 

Simples teste de upload de arquivo utilizando o mongoDb

Subir MongoDB local com Docker

docker run -d --name mongo-teste -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=mongoadmin -e MONGO_INITDB_ROOT_PASSWORD=testenovo-e MONGO_INITDB_DATABASE=uploads mongo
