FROM postgres:latest

COPY init.sql /docker-entrypoint-initdb.d/init.sql

COPY postgresql.conf /etc/postgresql/postgresql.conf

RUN mkdir -p /var/log/database

RUN chown -R postgres:postgres /var/log/database

# Указываем файл логирования в конфигурационном файле PostgreSQL
RUN echo "logging_collector = on" >> /etc/postgresql/postgresql.conf
RUN echo "log_destination = 'stderr'" >> /etc/postgresql/postgresql.conf
RUN echo "log_directory = '/var/log/database'" >> /etc/postgresql/postgresql.conf
RUN echo "log_filename = 'database.log'" >> /etc/postgresql/postgresql.conf

ENV PGLOG /var/log/database/database.log

