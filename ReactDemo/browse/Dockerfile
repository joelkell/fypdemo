FROM node:13.12.0-alpine
COPY build build
EXPOSE 3000
RUN npm install -g serve
CMD ["serve", "-s", "build", "-l", "3000", "--cors"]

# FROM node:13.12.0-alpine
# WORKDIR /app
# ENV PATH /app/node_modules/.bin:$PATH
# COPY package.json ./
# COPY yarn.lock ./
# RUN yarn install --ignore-engines
# COPY . ./
# EXPOSE 3000
# CMD ["yarn", "start"]

# FROM node:13.12.0-alpine as build
# WORKDIR /app
# ENV PATH /app/node_modules/.bin:$PATH
# COPY package.json ./
# COPY yarn.lock ./
# RUN yarn install --frozen-lockfile --ignore-engines
# COPY . ./
# RUN yarn build

# # production environment
# FROM nginx:stable-alpine
# COPY --from=build /app/build /usr/share/nginx/html
# COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf
# EXPOSE 80
# CMD ["nginx", "-g", "daemon off;"]