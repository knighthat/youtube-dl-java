package me.knighthat.youtubedl.command.flag;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface CommandFlag {

    @NotNull Map<String, String> arguments();
}