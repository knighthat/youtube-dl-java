package me.knighthat.extractor.asiancrush.response.format;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;

import me.knighthat.youtubedl.exception.PatternMismatchException;

/**
 * FormatUtils
 */
class FormatUtils {

    @NotNull
    private static String SIZE_FORMAT = "^\\d+(\\.\\d+)?";
    @NotNull
    private static String SIZE_UNIT = "[KMGTPEZY]?iB$";

    @NotNull
    static final Pattern FPS_PATTERN        = Pattern.compile( "^\\d+(\\.\\d+)?\s*fps$" );
    @NotNull
    static final Pattern RESOLUTION_PATTERN = Pattern.compile( "^\\d+x\\d+$" );
    @NotNull
    static final Pattern QUALITY_PATTERN = Pattern.compile( "^\\d+k$" );
    @NotNull
    static final Pattern SIZE_PATTERN = Pattern.compile( SIZE_FORMAT + SIZE_UNIT );
    @NotNull
    static final Pattern SAMPLING_RATE_PATTERN = Pattern.compile( "\\d+\s*Hz" );

    @NotNull
    static final Gson GSON = new Gson();

    static float fpsParser( String @NotNull [] arr, int pos ) throws PatternMismatchException {
        String fpsStr = arr[pos].trim();
        if ( FPS_PATTERN.matcher( fpsStr ).matches() ) {
            String framePerSecond = fpsStr.replace( "fps", "" );
            return Float.parseFloat( framePerSecond );
        } else
            throw new PatternMismatchException( fpsStr, "fps", arr );
    }

    static @NotNull String reolutionParser( String @NotNull [] arr, int pos ) throws PatternMismatchException {
        String resStr = arr[pos].trim();
        if ( RESOLUTION_PATTERN.matcher( resStr ).matches() )
            return resStr.split( "x" )[1] + "p";
        else
            throw new PatternMismatchException( resStr, "resolution", arr );
    }

    static @NotNull int tbrParser( String @NotNull [] arr, int pos ) {
        String tbrStr = arr[pos].trim();
        if ( QUALITY_PATTERN.matcher( tbrStr ).matches() ) {
            // Remove the letter 'k'
            tbrStr = tbrStr.substring( 0, tbrStr.length() - 1 );
            return Integer.parseInt( tbrStr );
        } else 
            throw new PatternMismatchException( tbrStr, "quality", arr );
    }

    static @NotNull BigInteger sizeParser( String @NotNull [] arr, int pos ) {
        String sizeStr = arr[pos].trim();
        if ( !SIZE_PATTERN.matcher( sizeStr ).matches() )
            throw new PatternMismatchException( sizeStr, "file's size", arr );

        int pow = switch ( sizeStr.replaceAll( SIZE_FORMAT, "" ) ) {
            case "KiB" -> 1;
            case "MiB" -> 2;
            case "GiB" -> 3;
            case "TiB" -> 4;
            case "PiB" -> 5;
            case "EiB" -> 6;
            case "ZiB" -> 7;
            case "YiB" -> 8;
            default -> 0;
        };

        String actualSize = sizeStr.replaceAll( SIZE_UNIT, "" );
        double unconvertedSize = Double.parseDouble( actualSize );
        if ( pow != 0 ) 
            unconvertedSize *= Math.pow( 1024, pow );

        return BigInteger.valueOf( Math.round( unconvertedSize ) );
    }

    static int sampleRateParser( String @NotNull [] arr, int pos ) {
        String smplStr = arr[pos].trim();
        Matcher matcher = SAMPLING_RATE_PATTERN.matcher( smplStr );
        if ( !matcher.find() )
            throw new PatternMismatchException( smplStr, "sampling rate", arr );

        smplStr = smplStr.substring( matcher.start(), matcher.end() - 2 );
        return Integer.parseInt( smplStr );
    }
}