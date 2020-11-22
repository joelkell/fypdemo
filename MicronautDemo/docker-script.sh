docker system prune --volumes -f
mvn clean install
docker-compose up --build