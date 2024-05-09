from pydantic import BaseModel


class SendFileRequest(BaseModel):
    owner_id: int
    file_name: str