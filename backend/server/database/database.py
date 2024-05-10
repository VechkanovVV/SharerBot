import logging
import psycopg2
from os import getenv

logging.basicConfig(
    level=logging.INFO,
    filename="/var/log/server/database.log",
    filemode="w",
    format="%(asctime)s %(levelname)s %(message)s"
)
connection = psycopg2.connect(
    dbname=f"{getenv("POSTGRES_DB")}",
    host=f"{getenv("POSTGRES_HOST")}",
    user=f"{getenv("POSTGRES_USER")}",
    password=f"{getenv("POSTGRES_PASSWORD")}",
    port=int(getenv("POSTGRES_PORT"))
)
cursor = connection.cursor()
table_name = f"FILES"
max_len = 5


def upload_file(owner_id: int, file_id: str, file_name: str, file_description: str):
    logging.info(f"Upload or update file response")
    cursor.execute(
        f""
        f"IF EXITS ("
        f"   SELECT 1"
        f"   FROM {table_name}"
        f"   WHERE owner_id = %s AND file_name = %s"
        f") THEN"
        f"       DELETE FROM {table_name}"
        f"       WHERE owner_id = %s AND file_name = %s;"
        f"END IF;"
        f""
        f"INSERT INTO {table_name}"
        f"VALUES ({owner_id}, %s, %s, %s);",
        (owner_id, file_name, file_id, file_name, file_description)
    )
    response = cursor.fetchall()
    logging.info(f"Upload or update file response")
    return response


def find_file(file_name: str):
    logging.info(f"Search file request")
    cursor.execute(
        f"SELECT file_name, description"
        f"FROM {table_name}"
        f"WHERE file_name LIKE %s"
        f"LIMIT {max_len}",
        ("%" + file_name + "%")
    )
    response = cursor.fetchall()
    logging.info(f"Response with list of files")
    return response


def download_file(owner_id: int, file_name: str):
    logging.info(f"Search file_id request")
    cursor.execute(
        f"SELECT file_id"
        f"FROM {table_name}"
        f"WHERE owner_id = %s AND file_name = %s",
        (owner_id, file_name)
    )
    response = cursor.fetchall()
    logging.info(f"Response wit file_id")
    return response
