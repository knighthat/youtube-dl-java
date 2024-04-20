package me.knighthat.deps.response.format;

import lombok.Getter;
import me.knighthat.deps.exception.InsufficientElementsException;
import org.jetbrains.annotations.NotNull;

@Getter
public final class AudioOnly extends Format implements Audio {

    private final int samplingRate;

    /**
     * A class that represents this array
     * [140, m4a, audio, only, audio_quality_medium, 129k, m4a_dash container, mp4a.40.2 (44100Hz), 3.71MiB]
     *
     * @param arr must follow stated format
     *
     * @throws InsufficientElementsException when number of elements inside provided arr less than known template
     */
    public AudioOnly( String @NotNull [] arr ) {
        super( Type.AUDIO_ONLY, arr );

        if ( arr.length < 9 )
            throw new InsufficientElementsException( "AudioOnly", arr.length, 9 );

        this.samplingRate = parseSamplingRate( arr, 7 );
    }
}