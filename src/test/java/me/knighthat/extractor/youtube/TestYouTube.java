package me.knighthat.extractor.youtube;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import me.knighthat.extractor.youtube.response.YouTubeVideo;
import me.knighthat.youtubedl.YoutubeDL;
import me.knighthat.youtubedl.exception.UnsupportedVersionException;
import me.knighthat.youtubedl.response.format.Format;

/*
 * This class tests "formats", "thumbnails", "subtitles", and "video"
 * methods of {@link me.knighthat.extractor.youtube.YouTube} class.
 * 
 * "stream" and "json" are excluded because the methods use default
 * retrieve methods instead of making their own.
 */
public class TestYouTube {

    @NotNull
    private static final String URL = "https://www.youtube.com/watch?v=Xs0Exw7O7B0";

    @BeforeAll
    static void setUp() {
        try {
            YoutubeDL.init();
        } catch (UnsupportedVersionException | IOException e) {
            fail( "failed to setup python/youtube-dl!", e);
        }
    }

    @Test
    void testFormatsExtractor() {
        List<Format> formats = YouTube.formats( URL ).execute().items();

        for( Format format : formats ) {
            if ( 
                format instanceof YouTube.Format.Audio
                || format instanceof YouTube.Format.Video
                || format instanceof YouTube.Format.Mix
            )
                continue;
            else
                fail(format.getClass().getName() + " is not a predefined YouTube's format!");
        }
    }

    @Test
    void testVideoExtractor() {
        Optional<YouTubeVideo> video = YouTube.video( URL ).execute().result();

        assertDoesNotThrow( () -> video.get() );
        Assertions.assertInstanceOf(
            me.knighthat.extractor.youtube.response.YouTubeVideo.class, 
            video.get()
        );
    }
}