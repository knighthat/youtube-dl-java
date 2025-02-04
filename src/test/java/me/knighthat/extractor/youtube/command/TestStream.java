package me.knighthat.extractor.youtube.command;

import me.knighthat.extractor.youtube.YouTube;
import me.knighthat.youtubedl.command.Stream;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class TestStream extends CommandTestTemplate {

    @Test
    @Override
    public void testBuilderInit() {
        Stream stream = YouTube.stream( URL ).build();
        Set<String> actualSet = super.cmdToSet( stream );
        Assertions.assertTrue( actualSet.contains( "-o" ) );
        Assertions.assertTrue( actualSet.contains( "-" ) );


        Set<String> expectedSet = super.commandContains( "-o", "-" );
        Assertions.assertEquals( expectedSet, actualSet );
    }

    @Override
    protected void testBuilderAddFlags() {
        @NotNull final String toBeIncluded = "--force-ipv4";

        Stream stream = YouTube.stream( URL )
                               .flags( Flag.noValue( toBeIncluded ) )
                               .build();
        Set<String> actualSet = super.cmdToSet( stream );
        Assertions.assertTrue( actualSet.contains( toBeIncluded ) );

        Set<String> expectedSet = super.commandContains( "--force-ipv4", "" );
        Assertions.assertEquals( expectedSet, actualSet );
    }

    @Override
    protected void testBuilderAddHeaders() {
        @NotNull final String field = "ContentType", value = "application/json", combined = field + ":" + value;

        Stream stream = YouTube.stream( URL )
                               .headers( Header.key( field ).value( value ) )
                               .build();
        Set<String> actualSet = super.cmdToSet( stream );
        Assertions.assertTrue( actualSet.contains( "--add-header" ) );
        Assertions.assertTrue( actualSet.contains( combined ) );

        Set<String> expectedSet = super.commandContains( "--add-header", combined );
        Assertions.assertEquals( expectedSet, actualSet );
    }

    @Override
    protected void testBuilderAddUserAgent() {
        @NotNull final String key = "--user-agent", value = UserAgent.FIREFOX_LINUX.toString();

        Stream stream = YouTube.stream( URL )
                               .userAgent( UserAgent.FIREFOX_LINUX )
                               .build();
        Set<String> actualSet = super.cmdToSet( stream );
        Assertions.assertTrue( actualSet.contains( "--user-agent" ) );
        Assertions.assertTrue( actualSet.contains( value ) );

        Set<String> expectedSet = super.commandContains( key, value );
        Assertions.assertEquals( expectedSet, actualSet );
    }

    @Override
    protected void testBuilderAddGeoConfig() {
        String key = "--geo-bypass-country", value = "CA";

        GeoConfig geoConfig = GeoConfig.builder().countryCode( value ).build();
        Stream stream = YouTube.stream( URL )
                               .geoConfig( geoConfig )
                               .build();
        Set<String> actualSet = super.cmdToSet( stream );
        Assertions.assertTrue( actualSet.contains( key ) );
        Assertions.assertTrue( actualSet.contains( value ) );

        Set<String> expectedSet = super.commandContains( key, value );
        Assertions.assertEquals( expectedSet, actualSet );
    }
}
