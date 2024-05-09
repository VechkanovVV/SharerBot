import logging
from fastapi import FastAPI
from fastapi.encoders import jsonable_encoder
from fastapi.responses import JSONResponse
import database.database as db
from FindFileRequest import FindFileRequest
from FindFileResponse import FindFileResponse
from UploadFileRequest import UploadFileRequest
# from UploadFileResponse import UploadFileResponse
from SendFileRequest import SendFileRequest
# from SendFileResponse import SendFileResponse

# TODO: logs
# TODO: optimisation; any balancers; any workers
# TODO: async?
# TODO: threads?
file_id_len = 255
file_name_len = 255
file_description_len = 65535
app = FastAPI()


# TODO:
@app.get("/find_file")
def find_file(request: FindFileRequest) -> JSONResponse:
    if len(request.file_name) > file_name_len:
        return jsonable_encoder([])

    db_response = db.find_file(request.file_name)
    response = jsonable_encoder(
        [
            FindFileResponse(owner_id=row[0], file_description=row[1], file_name=row[2])
            for row in db_response
        ]
    )
    return JSONResponse(content=response)


@app.get("/send_file")
def send_file(request: SendFileRequest):
    if len(request.file_name) > file_name_len:
        return

    db_response = db.send_file(request.owner_id, request.file_name)
    return


# TODO:
@app.post("/upload_file")
def upload_file(request: UploadFileRequest):
    if len(request.file_name) > file_name_len or len(request.file_id) > file_id_len or len(request.file_description) > file_description_len:
        return

    db_response = db.upload_file(request.owner_id, request.file_id, request.file_name, request.file_description)
    return
    # request