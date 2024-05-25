package me.knighthat.extractor.youtube.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.knighthat.extractor.youtube.response.format.Audio;
import me.knighthat.extractor.youtube.response.format.Mix;
import me.knighthat.extractor.youtube.response.format.Video;
import me.knighthat.youtubedl.command.flag.Flag;
import me.knighthat.youtubedl.command.flag.GeoConfig;
import me.knighthat.youtubedl.command.flag.Header;
import me.knighthat.youtubedl.command.flag.UserAgent;
import me.knighthat.youtubedl.response.ListResponse;
import me.knighthat.youtubedl.response.format.Format;

/**
 * Extract available streaming formats of a YouTube's video.
 */
public class Formats extends me.knighthat.youtubedl.command.Formats {

    public static @NotNull Builder builder( @NotNull String url ) { return new Builder(url); }

    private Formats(
        @NotNull String url, 
        @NotNull Set<Flag> flags,
        @NotNull Set<Header> headers,
        @NotNull UserAgent userAgent, 
        @NotNull GeoConfig geoConfig
        ) {
        super(url, flags, headers, userAgent, geoConfig);
    }

    @Override
    public @NotNull ListResponse<? extends Format> execute() {
        List<Format> formats = new ArrayList<>();

        /*
        Audio
        249          webm       audio only audio_quality_low   57k , webm_dash container, opus  (48000Hz), 2.48MiB

        Video
        248          webm       1080x1080  1080p   82k , webm_dash container, vp9, 25fps, video only, 3.59MiB

        Mix
        18           mp4        360x360    360p  169k , avc1.42001E, 25fps, mp4a.40.2 (44100Hz) (best)
        */
        for (String output : super.outputs()) {
            if ( !Character.isDigit( output.charAt( 0 ) ) )
                continue;

            // [249          webm       audio only audio_quality_low   57k ,  webm_dash container,  opus  (48000Hz),  2.48MiB]
            String[] parts = output.trim().split( "," );
            // [249, webm, audio, only, audio_quality_low, 57k]
            String[] info = parts[0].trim().split( "\\s+" );

            String[] all = new String[parts.length + info.length - 1];
            System.arraycopy( info, 0, all, 0, info.length );
            System.arraycopy( parts, 1, all, info.length, parts.length - 1 );

            Format format;
            if ( output.contains( "video only" ) )
                format = new Video( all );
            else if ( output.contains( "audio only" ) )
                format = new Audio( all );
            else
                format = new Mix( all );
            formats.add( format );
        }

        return () -> formats;
    }

    public static class Builder extends me.knighthat.youtubedl.command.Formats.Builder {

        private Builder(@NotNull String url) { super(url); }

        @Override
        public @NotNull Builder flags(@NotNull Flag... flags) { return (Builder) super.flags(flags); }

        @Override
        public @NotNull Builder headers(@NotNull Header... headers) { return (Builder) super.headers(headers); }

        @Override
        public @NotNull Builder userAgent(@Nullable UserAgent userAgent) { return (Builder) super.userAgent(userAgent); }

        @Override
        public @NotNull Builder geoConfig(@Nullable GeoConfig geoConfig) { return (Builder) super.geoConfig(geoConfig); }

        @Override
        public @NotNull Formats build() {
            return new Formats( getUrl(), getFlags(), getHeaders(), getUserAgent(), getGeoConfig() );
        }
    }    
}