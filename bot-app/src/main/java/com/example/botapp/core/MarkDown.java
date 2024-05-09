package com.example.botapp.core;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MarkDown {
    private MarkDown() {
    }
    @NotNull
    @Contract(pure = true)
    public static String process(@NotNull String str) {
        String s;
        s = str.replaceAll("\\.", "\\\\.");
        s = s.replaceAll("!", "\\\\!");
        s = s.replaceAll("-", "\\\\-");
        s = s.replaceAll("\\{", "\\\\{");
        s = s.replaceAll("}", "\\\\}");
        s = s.replaceAll("_", "\\\\_");
        s = s.replaceAll("\\+", "\\\\+");
        s = s.replaceAll("#", "\\\\#");
        return s;
    }
}