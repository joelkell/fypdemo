docker system prune --volumes --filter "label!=keep" -f
# cd into folders and yarn build
cd container
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
docker-compose up --build