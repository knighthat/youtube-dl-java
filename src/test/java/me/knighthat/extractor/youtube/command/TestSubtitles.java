package me.knighthat.extractor.youtube.command;

import me.knighthat.extractor.youtube.YouTube;
import me.knighthat.youtubedl.command.Subtitles;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class TestSubtitles extends CommandTestTemplate {

    @Test
    @Override
    public void testBuilderInit() {
        Subtitles subtitles = YouTube.subtitles( URL ).build();
        Set<String> actualSet = super.cmdToSet( subtitles );
        Assertions.assertTrue( actualSet.contains( "--list-subs" ) );

        Set<String> expectedSet = super.commandContains( "--list-subs" );
        Assertions.assertEquals( expectedSet, actualSet );
    }

    @Override
    protected void testBuilderAddFlags() {
        @NotNull final String toBeIncluded = "--force-ipv4";

        Subtitles subtitles = YouTube.subtitles( URL )
                                     .flags( Flag.noValue( toBeIncluded ) )
                                     .build();
        Set<String> actualSet = super.cmdToSet( subtitles );
        Assertions.assertTrue( actualSet.contains( toBeIncluded ) );

        Set<String> expectedSet = super.commandContains( "--force-ipv4" );
        Assertions.assertEquals( expectedSet, actualSet );
    }

    @Override
    protected void testBuilderAddHeaders() {
        @NotNull final String field = "ContentType", value = "application/json", combined = field + ":" + value;

        Subtitles subtitles = YouTube.subtitles( URL )
                                     .headers( Header.key( field ).value( value ) )
                                     .build();
        Set<String> actualSet = super.cmdToSet( subtitles );
        Assertions.assertTrue( actualSet.contains( "--add-header" ) );
        Assertions.assertTrue( actualSet.contains( combined ) );

        Set<String> expectedSet = super.commandContains( "--add-header", combined );
        Assertions.assertEquals( expectedSet, actualSet );
    }

    @Override
    protected void testBuilderAddUserAgent() {
        @NotNull final String key = "--user-agent", value = UserAgent.FIREFOX_LINUX.toString();

        Subtitles subtitles = YouTube.subtitles( URL )
                                     .userAgent( UserAgent.FIREFOX_LINUX )
                                     .build();
        Set<String> actualSet = super.cmdToSet( subtitles );
        Assertions.assertTrue( actualSet.contains( "--user-agent" ) );
        Assertions.assertTrue( actualSet.contains( value ) );

        Set<String> expectedSet = super.commandContains( key, value );
        Assertions.assertEquals( expectedSet, actualSet );
    }

    @Override
    protected void testBuilderAddGeoConfig() {
        String key = "--geo-bypass-country", value = "CA";

        GeoConfig geoConfig = GeoConfig.builder().countryCode( value ).build();
        Subtitles subtitles = YouTube.subtitles( URL )
                                     .geoConfig( geoConfig )
                                     .build();
        Set<String> actualSet = super.cmdToSet( subtitles );
        Assertions.assertTrue( actualSet.contains( key ) );
        Assertions.assertTrue( actualSet.contains( value ) );

        Set<String> expectedSet = super.commandContains( key, value );
        Assertions.assertEquals( expectedSet, actualSet );
    }
}
