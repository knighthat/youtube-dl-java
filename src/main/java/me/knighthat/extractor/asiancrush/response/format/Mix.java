package me.knighthat.extractor.asiancrush.response.format;

import org.jetbrains.annotations.NotNull;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.knighthat.youtubedl.exception.InsufficientElementsException;
import me.knighthat.youtubedl.response.format.Format;
import me.knighthat.youtubedl.response.format.MixFormat;

/**
 * Represents media from AsianCrush
 */
@Getter
@Accessors( fluent = true )
public class Mix implements MixFormat {

    @NotNull
    @ToString.Exclude
    private final Format.Type type;
    @NotNull
    private final String code;
    @NotNull
    private final String extension;
    private final int tbr;
    @NotNull
    private final String vCodec;
    @NotNull
    private final String aCodec;
    private final int sampleRate;
    @NotNull
    private final String resolution;
    private final float fps;

    {
        this.type = me.knighthat.youtubedl.response.format.Format.Type.MIXED;
    }

    public Mix( String @NotNull [] arr ) {
        // [hls-361, mp4, 426x240, 361k, avc1.64001F, 23.976fps, mp4a.40.2]
        if ( arr.length < 7 )
            throw new InsufficientElementsException( "Mix", arr.length, 7 );

        this.code = arr[0];
        this.extension = arr[1];
        this.tbr = FormatUtils.tbrParser( arr, 3 );
        this.vCodec = arr[4];
        this.aCodec = arr[6];
        this.sampleRate = 0;    // Doesn't have sample rate
        this.resolution = FormatUtils.reolutionParser( arr, 2 );
        this.fps = FormatUtils.fpsParser( arr, 5 );
    }

    public Mix( @NotNull JsonObject json ) {
        /*
        "format_id": "hls-361",
        "url": "https://asiancrush-asiancrush.cdn-ak.matchpoint.tv/cinedigm/1000000030080/650b488ef1039d36ff29b1b1/hls/235.m3u8",
        "manifest_url": "https://asiancrush-asiancrush.cdn-ak.matchpoint.tv/cinedigm/1000000030080/650b488ef1039d36ff29b1b1/hls/master.m3u8",
        "tbr": 361.96,
        "ext": "mp4",
        "fps": 23.976,
        "protocol": "m3u8",
        "preference": null,
        "width": 426,
        "height": 240,
        "vcodec": "avc1.64001F",
        "acodec": "mp4a.40.2",
        "http_headers": {},
        "format": "hls-361 - 426x240" 
        */
        for ( String key : new String[] { "format_id", "ext", "tbr", "vcodec", "acodec", "fps", "height" } ) 
            if ( !json.has(key) )
                throw new NullPointerException(key + " does not exist!");

        this.code = json.get( "format_id" ).getAsString();
        this.extension = json.get( "ext" ).getAsString();
        this.tbr = json.get( "tbr" ).getAsInt();
        this.vCodec = json.get( "vcodec" ).getAsString();
        this.aCodec = json.get( "acodec" ).getAsString();
        this.sampleRate = 0;
        this.resolution = json.get( "height" ).getAsString() + "p";
        this.fps = json.get( "fps" ).getAsFloat();
    }
}