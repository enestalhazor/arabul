FROM ubuntu

RUN apt update && apt install -y postgresql postgresql-contrib openjdk-17-jdk maven nodejs npm
RUN npm install -g serve
COPY ./productphotos /productphotos
COPY ./dump.sql /home/dump.sql
COPY ./start.sh /home/start.sh
COPY ./target/arabul-0.0.1-SNAPSHOT.jar /home/app.jar

ENV DB_NAME="arabul"
ENV DB_USER="postgres"
ENV DUMP_FILE="/home/dump.sql"

USER postgres
RUN service postgresql start && service postgresql status && psql -c "CREATE DATABASE $DB_NAME" && psql -d $DB_NAME -f $DUMP_FILE

USER root
RUN chmod +x /home/start.sh
ENTRYPOINT ["/home/start.sh"]
