from sqlalchemy import Column, Integer, String, PrimaryKeyConstraint
from .database import Base


class Files(Base):
    __tablename__ = "FILES"

    owner_id = Column(Integer, primary_key=True)
    file_id = Column(String)
    file_name = Column(String, primary_key=True)
    file_description = Column(String)
