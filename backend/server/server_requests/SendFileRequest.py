from pydantic import BaseModel


class SendFileRequest(BaseModel):
    file_name: str
    owner_id: int
    receiver_id: int