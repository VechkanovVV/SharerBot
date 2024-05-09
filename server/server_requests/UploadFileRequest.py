from pydantic import BaseModel


class UploadFileRequest(BaseModel):
    owner_id: int
    file_id: str
    file_description: str
    file_name: str