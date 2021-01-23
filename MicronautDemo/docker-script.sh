docker system prune --volumes --filter "label!=keep" -f
mvn clean install
docker-compose up --build