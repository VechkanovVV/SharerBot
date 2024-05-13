from pydantic import BaseModel


class FindFilesServerDBRequest(BaseModel):
    file_name: str


class FindFilesServerDBResponse(FindFilesServerDBRequest):
    owner_id: int
    file_description: str

    class Config:
        orm_mode = True


class UploadFileServerDBRequest(BaseModel):
    owner_id: int
    file_id: str
    file_description: str
    file_name: str


class SendFileServerRequest(BaseModel):
    file_name: str
    owner_id: int
    receiver_id: int


class SetPermissionBotRequest(BaseModel):
    file_name: str
    owner_id: int
    id: int


class FindFileDBRequest(BaseModel):
    owner_id: int
    file_name: str


class FindFileDBResponse(BaseModel):
    file_id: int

    class Config:
        orm_mode = True


class SendFileBotRequest(BaseModel):
    owner_id: int
    id: int
    file_id: str
