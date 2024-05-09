from pydantic import BaseModel


class SetPermissionRequest(BaseModel):
    file_name: str
    owner_id: int
    id: int