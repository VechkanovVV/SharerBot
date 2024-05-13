FROM python:3.12
RUN mkdir -p /var/log/server
WORKDIR /backend
COPY ./server/requirements.txt requirements.txt
RUN pip install --no-cache-dir -r requirements.txt
COPY ./server /backend
WORKDIR /
CMD ["uvicorn", "backend.main:app", "--host", "0.0.0.0", "--port", "8000"]
