from pydantic import BaseModel


class FindFileResponse(BaseModel):
    owner_id: int
    file_description: str
    file_name: str