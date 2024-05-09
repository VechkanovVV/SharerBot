import logging
import psycopg2

# TODO: logs
# TODO: db names
connection = psycopg2.connect(
    dbname="bot_database",
    host="database",
    user="bot",
    password="db_pass",
    port="db_port"
)
cursor = connection.cursor()
table_name = f"FILES"
max_len = 5


def upload_file(owner_id: int, file_id: str, file_name: str, file_description: str):
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
    return response


def find_file(file_name: str):
    cursor.execute(
        f"SELECT file_name, description"
        f"FROM {table_name}"
        f"WHERE file_name LIKE %s"
        f"LIMIT {max_len}",
        ("%" + file_name + "%")
    )
    response = cursor.fetchall()
    return response


def download_file(owner_id: int, file_name: str):
    cursor.execute(
        f"SELECT file_id"
        f"FROM {table_name}"
        f"WHERE owner_id = %s AND file_name = %s",
        (owner_id, file_name)
    )
    response = cursor.fetchall()
    return response
