import logging
import requests
import database.database as db
from fastapi import FastAPI
from fastapi.encoders import jsonable_encoder
from fastapi.responses import JSONResponse
from server_requests.FindFileRequest import FindFileRequest
from server_responses.FindFileResponse import FindFileResponse
from server_requests.UploadFileRequest import UploadFileRequest
from server_requests.SendFileRequest import SendFileRequest
from bot_requests.SetPermissionRequest import SetPermissionRequest as BotSetPermissionRequest
from bot_requests.SendFileRequest import SendFileRequest as BotSendFileRequest

logging.basicConfig(
    level=logging.INFO,
    filename="/var/log/server/server.log",
    filemode="w",
    format="%(asctime)s %(levelname)s %(message)s"
)
file_id_len = 255
file_name_len = 255
file_description_len = 65535
bot_url = f"http://localhost:8081"
app = FastAPI()


@app.get("/find_file")
async def find_file(request: FindFileRequest) -> JSONResponse:
    if len(request.file_name) > file_name_len:
        logging.warning(f"File name '{request.file_name}' is too long")
        return jsonable_encoder([])

    logging.info(f"File search with file_name: {request.file_name}")
    db_response = db.find_file(request.file_name)
    response = jsonable_encoder(
        [
            FindFileResponse(
                owner_id=row[0],
                file_description=row[1],
                file_name=row[2]
            )
            for row in db_response
        ]
    )
    logging.info(f"File search response")
    return JSONResponse(content=response)


@app.get("/download_file")
async def download_file(request: SendFileRequest) -> None:
    if len(request.file_name) > file_name_len:
        logging.warning(f"File name '{request.file_name}' is too long")
        return

    logging.info(f"Permission request")
    response = requests.post(
        bot_url + "/request_permission",
        jsonable_encoder(
            BotSetPermissionRequest(
                file_name=request.file_name,
                owner_id=request.owner_id,
                id=request.receiver_id
            )
        )
    )

    if response.status_code == 200:
        logging.info(f"Permission received")
        logging.info(f"File id request")
        db_response = db.download_file(
            request.owner_id,
            request.file_name
        )
        logging.info(f"Send file request")
        requests.post(
            bot_url + "/send_file",
            jsonable_encoder(
                BotSendFileRequest(
                    owner_id=request.owner_id,
                    id=request.receiver_id,
                    file_id=db_response[0]
                )
            )
        )
        logging.info(f"File sent")
        return

    logging.info(f"Permission denied")
    return


@app.post("/upload_file")
async def upload_file(request: UploadFileRequest) -> None:
    if len(request.file_name) > file_name_len:
        logging.warning(f"File name '{request.file_name}' is too long")
        return

    if len(request.file_id) > file_id_len:
        logging.warning(f"File id '{request.file_id}' is too long")
        return

    if len(request.file_description) > file_description_len:
        logging.warning(f"File description '{request.file_description}' is too long")
        return

    logging.info(f"File upload with owner_id: {request.owner_id}, file_name: {request.file_name}")
    db.upload_file(request.owner_id,
                   request.file_id,
                   request.file_name,
                   request.file_description)
    return
