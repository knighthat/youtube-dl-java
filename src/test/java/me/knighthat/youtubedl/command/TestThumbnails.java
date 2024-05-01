package me.knighthat.youtubedl.command;

import me.knighthat.youtubedl.YoutubeDL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestThumbnails extends CommandTestTemplate {

    @Test
    @Override
    void testBuilder() {
        Thumbnails formats = YoutubeDL.thumbnails( CommandTestTemplate.URL ).build();
        String[] actual = formats.command();
        Set<String> actualSet = new HashSet<>( Arrays.asList( actual ) );

        String[] expected = {
                YoutubeDL.getPythonPath(),
                YoutubeDL.getYtdlPath(),
                "--list-thumbnails", "",
                CommandTestTemplate.URL
        };
        Set<String> expectedSet = new HashSet<>( Arrays.asList( expected ) );

        Assertions.assertEquals( expected.length, actual.length );
        Assertions.assertEquals( expectedSet, actualSet );
    }
}