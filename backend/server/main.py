import logging
import requests
from fastapi import Depends, FastAPI, HTTPException, Request, Response
from sqlalchemy.orm import Session
from fastapi.encoders import jsonable_encoder
from fastapi.responses import JSONResponse
from . import crud, models, schemas
from .database import SessionLocal, engine


logging.basicConfig(
    level=logging.INFO,
    filename="/var/log/server/server.log",
    filemode="w",
    format="%(asctime)s %(levelname)s %(message)s"
)

bot_url = f"http://bot-app:8081"

file_id_len = 255
file_name_len = 255
file_description_len = 65535

try:
    models.Base.metadata.create_all(bind=engine)
except Exception:
    exit(1)

app = FastAPI()


@app.middleware("http")
async def db_session_middleware(request: Request, call_next):
    response = Response("Internal server error", status_code=500)
    try:
        request.state.db = SessionLocal()
        response = await call_next(request)
    finally:
        request.state.db.close()
    return response


# Dependency
def get_db(request: Request):
    return request.state.db


@app.post("/find_file")
def find_file(request: schemas.FindFilesServerDBRequest, db: Session = Depends(get_db)): # -> JSONResponse:
    if len(request.file_name) > file_name_len:
        logging.warning(f"File name '{request.file_name}' is too long")
        return JSONResponse(content=jsonable_encoder([]))

    logging.info(f"File search with file_name: {request.file_name}")
    db_response = crud.find_files_by_name_pattern(db, request)
    logging.info(f"File search response")
    return JSONResponse(content=jsonable_encoder(db_response))


@app.post("/upload_file")
def upload_file(request: schemas.UploadFileServerDBRequest, db: Session = Depends(get_db)) -> None:
    if len(request.file_name) > file_name_len:
        logging.warning(f"File name '{request.file_name}' is too long")
        return

    if len(request.file_id) > file_id_len:
        logging.warning(f"File id '{request.file_id}' is too long")
        return

    if len(request.file_description) > file_description_len:
        logging.warning(f"File description '{request.file_description}' is too long")
        return

    logging.info(f"Check file existence with owner_id: {request.owner_id}, file_name: {request.file_name}")

    if crud.find_file(db, schemas.FindFileDBRequest(owner_id=request.owner_id, file_name=request.file_name)):
        logging.info(f"File with owner_id: {request.owner_id}, file_name: {request.file_name} exists")
        logging.info(f"Record updating")
        crud.update_record(db, request)
        logging.info(f"Record updated")
    else:
        logging.info(f"File with owner_id: {request.owner_id}, file_name: {request.file_name} does not exist")
        logging.info(f"Record creating")
        crud.create_record(db, request)
        logging.info(f"Record created")

    return


@app.post("/download_file")
def download_file(request: schemas.SendFileServerRequest, db: Session = Depends(get_db)) -> None:
    if len(request.file_name) > file_name_len:
        logging.warning(f"File name '{request.file_name}' is too long")
        return

    logging.info(f"Permission request")
    response = requests.post(
        bot_url + "/request_permission",
        jsonable_encoder(
            schemas.SetPermissionBotRequest(
                file_name=request.file_name,
                owner_id=request.owner_id,
                id=request.receiver_id
            )
        )
    )

    if response.status_code == 200:
        logging.info(f"Permission received")
        logging.info(f"File id request")
        db_response = crud.find_file(db, schemas.FindFileDBRequest(owner_id=request.owner_id, file_name=request.file_name))
        logging.info(f"Send file request")
        requests.post(
            bot_url + "/send_file",
            jsonable_encoder(
                schemas.SendFileBotRequest(
                    owner_id=request.owner_id,
                    id=request.receiver_id,
                    file_id=db_response.file_id
                )
            )
        )
        logging.info(f"File sent")
        return

    logging.info(f"Permission denied")
    return
