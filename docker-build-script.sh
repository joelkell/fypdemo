#delete old volumes
docker system prune --volumes --filter "label!=keep" -f

#build backend services
cd MicronautDemo
mvn clean install

#build frontend services
cd ../ReactDemo/container
yarn build
cd ../login
yarn build
cd ../browse
yarn build
cd ../signup
yarn build
cd ../product
yarn build
cd ../productReview
yarn build
cd ../account
yarn build
cd ../accountOrders
yarn build
cd ../cart
yarn build

#start compose
cd ../../
docker-compose up --build