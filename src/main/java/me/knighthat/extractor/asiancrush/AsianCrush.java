package me.knighthat.extractor.asiancrush;

import org.jetbrains.annotations.NotNull;

import me.knighthat.youtubedl.command.Formats;
import me.knighthat.youtubedl.command.Json;
import me.knighthat.youtubedl.command.JsonImpl;
import me.knighthat.youtubedl.command.Stream;
import me.knighthat.youtubedl.command.StreamImpl;
import me.knighthat.youtubedl.command.Video;

/**
 * AsianCrush extractor
 */
public class AsianCrush {

    public static @NotNull Formats.Builder formats( @NotNull String url ) { 
        return me.knighthat.extractor.asiancrush.command.Formats.builder( url ); 
    }

    public static @NotNull Stream.Builder stream( @NotNull String url ) { 
        return StreamImpl.builder( url ); 
    }

    public static @NotNull Json.Builder json( @NotNull String url ) { 
        return JsonImpl.builder( url ); 
    }

    public static @NotNull Video.Builder video( @NotNull String url ) { 
        return me.knighthat.extractor.asiancrush.command.Video.builder( url ); 
    }
}