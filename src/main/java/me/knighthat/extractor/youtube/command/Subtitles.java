package me.knighthat.extractor.youtube.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.extractor.youtube.response.subtitle.Subtitle;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.logging.Logger;
import me.knighthat.youtubedl.response.ListResponse;

/**
 * YouTube's subtitles extractor.
 */
public class Subtitles extends me.knighthat.youtubedl.command.Subtitles {

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder( url ); }

    private Subtitles(
        @NotNull String url, 
        @NotNull Set<Flag> flags, 
        @NotNull Set<Header> headers,
        @Nullable UserAgent userAgent, 
        @Nullable GeoConfig geoConfig
        ) {
        super(url, flags, headers, userAgent, geoConfig);
    }

    @Override
    public @NotNull ListResponse<me.knighthat.youtubedl.response.subtitle.Subtitle> execute() {
        List<me.knighthat.youtubedl.response.subtitle.Subtitle> subtitles = new ArrayList<>();

        /*
            Available automatic captions for videoId:
            Language formats
            af       vtt, ttml, srv3, srv2, srv1, json3
            ak       vtt, ttml, srv3, srv2, srv1, json3
            sq       vtt, ttml, srv3, srv2, srv1, json3

            Available subtitles for videoId:
            Language formats
            zh-Hans  vtt, ttml, srv3, srv2, srv1, json3
            en       vtt, ttml, srv3, srv2, srv1, json3
            hi       vtt, ttml, srv3, srv2, srv1, json3

            videoId has no subtitles
        */
        List<String> outputs = super.outputs();

        Boolean isAutomatic = null;
        for (int i = 0 ; i < outputs.size() ; i++) {
            // af       vtt, ttml, srv3, srv2, srv1, json3
            String output = outputs.get( i );

            if ( NO_SUBTITLE_PATTERN.matcher( output ).matches() )
                // Stop iteration if output is "videoId has no subtitles"
                break;
            if ( AUTOMATIC_SUBTITLE_PATTERN.matcher( output ).matches() ) {
                /*
                Set isAutomatic to true if line matches 
                "Available subtitles for videoId:", then
                skip "Language formats".
                */
                isAutomatic = true;
                i++;
                continue;
            } else if ( HUMAN_SUBTITLE_PATTERN.matcher( output ).matches() ) {
                /*
                Set isAutomatic to false if line matches 
                "Available subtitles for videoId:", then
                skip "Language formats".
                */
                isAutomatic = false;
                i++;
                continue;
            }

            if ( isAutomatic != null ) {
                // ["af", "vtt, ttml, srv3, srv2, srv1, json3"]
                String[] parts = output.split( "(?<!,)\\s+" ); // Split string by spaces but not if it's right after coma
                // [vtt,  ttml,  srv3,  srv2,  srv1,  json3]
                String[] formats = parts[1].split( "," );

                for (String f : formats) {
                    try {
                        boolean automatic_caption = isAutomatic;
                        subtitles.add(
                            new Subtitle() {
                                @Override
                                public @NotNull String language() { return parts[0].trim(); }

                                @Override
                                public @NotNull Format format() { return Subtitle.Format.match( f.trim() ); }

                                @Override
                                public boolean isAutomatic() { return automatic_caption; }
                            }
                        );
                    } catch ( UnsupportedOperationException e ) {
                        Logger.exception( "failed to parse subtitle!", e, Level.WARNING );
                    }
                }
            }
        }

        return () -> subtitles;
    }

    @NotNull
    private static final Pattern AUTOMATIC_SUBTITLE_PATTERN = Pattern.compile( "Available automatic captions for \\w+:" );

    public static class Builder extends me.knighthat.youtubedl.command.Subtitles.Builder {

        private Builder(@NotNull String url) { super(url); }

        @Override
        public @NotNull Builder flags( @NotNull Flag... flags ) { return (Builder) super.flags( flags ); }

        @Override
        public @NotNull Builder headers( @NotNull Header... headers ) { return (Builder) super.headers( headers ); }

        @Override
        public @NotNull Builder userAgent( @Nullable UserAgent userAgent ) { return (Builder) super.userAgent( userAgent ); }

        @Override
        public @NotNull Builder geoConfig( @Nullable GeoConfig geoConfig ) { return (Builder) super.geoConfig( geoConfig ); }

        @Override
        public @NotNull Subtitles build() {
            return new Subtitles(getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig());
        }
    }
}