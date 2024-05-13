from sqlalchemy.orm import Session
from . import models
from . import schemas


def find_files_by_name_pattern(db: Session, file: schemas.FindFilesServerDBRequest, skip: int = 0, limit: int = 5):
    db_response = db.query(models.Files.owner_id, models.Files.file_description, models.Files.file_name).\
        filter(models.Files.file_name.like(f"%{file.file_name}%")).\
        offset(skip).\
        limit(limit).\
        all()
    return [schemas.FindFilesServerDBResponse(owner_id=resp.owner_id, file_description=resp.file_description, file_name=resp.file_name) for resp in db_response]


def find_file(db: Session, file: schemas.FindFileDBRequest):
    return db.query(models.Files.file_id).\
        filter(models.Files.file_name == file.file_name).\
        filter(models.Files.owner_id == file.owner_id).\
        first()


def update_record(db: Session, file: schemas.UploadFileServerDBRequest) -> None:
    db.query(models.Files).\
        filter(models.Files.owner_id == file.owner_id).\
        filter(models.Files.file_name == file.file_name).\
        update(
        {
            "file_id": file.file_id,
            "file_description": file.file_description
        }
    )
    db.commit()
    return


def create_record(db: Session, file: schemas.UploadFileServerDBRequest) -> None:
    db.add(models.Files(**file.dict()))
    db.commit()
    return
