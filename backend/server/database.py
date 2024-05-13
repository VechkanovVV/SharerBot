from os import getenv
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.ext.declarative import declarative_base

DATABASE_URL = getenv("DATABASE_URL", "postgresql://bot:change@db:5432/bot_database")

engine = create_engine(
    DATABASE_URL,
    connect_args={
        "application_name": "bot_server"
    }
)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()
