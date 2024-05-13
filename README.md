# Sharerbot

Sharerbot is a Telegram bot that allows users to manage files on a server. The bot provides a set of commands for selecting, searching, and uploading files, as well as managing permissions for file uploads.

## Commands

### /choose_file
Command to choose a file from a list of available files on the server.

### /find_file
Command to search for a file by its name.

### /help
Displays a list of available commands.

### /reject_permission
Command to reject a file upload request.

### /set_permission
Command to allow a file upload.

### /upload_file
Command to upload your file to the server.

## Usage

To start using the bot, simply find it on Telegram and click "Start". Then you can use the available commands to manage files.

## Implementation

The project is implemented using Spring to create the bot and server for storing files. The bot handles user commands and interacts with the server to perform file operations.

## Running the Project

To run the project, follow these steps:
1. Clone the project repository to your local machine.
2. Install all dependencies listed in the `build.gradle.kts` file.
3. Start the server for storing files.
4. Start the Telegram bot.

## Requirements

- Java 8 or higher
- Gradle

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
