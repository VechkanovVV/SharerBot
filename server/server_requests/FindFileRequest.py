from pydantic import BaseModel


class FindFileRequest(BaseModel):
    file_name: str
