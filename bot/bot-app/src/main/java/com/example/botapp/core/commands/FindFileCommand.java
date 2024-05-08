package com.example.botapp.core.commands;


import org.springframework.stereotype.Component;

@Component
public class FindFileCommand implements Command {
    private static final String COMMAND = "search_file";
    private static final String DESCRIPTION = "Command to find file if it exists";
    private static final String MESSAGE = """
            You need to send file name or prefix which you want to find
            """;

}
