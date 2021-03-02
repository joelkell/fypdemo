FROM node:13.12.0-alpine
COPY content content
EXPOSE 3000
RUN npm install -g serve
CMD ["serve", "-s", "content", "-l", "3000", "--cors"]