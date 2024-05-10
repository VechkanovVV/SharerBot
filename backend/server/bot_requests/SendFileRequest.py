from pydantic import BaseModel


class SendFileRequest(BaseModel):
    owner_id: int
    id: int
    file_id: str