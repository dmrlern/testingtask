package requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompleteExerciseRequest {

    @JsonProperty("exercise_result")
    public ExerciseResult exercise_result;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ExerciseResult {

        @JsonProperty("exercise_config_id")
        public int exercise_config_id;
        @JsonProperty("session_number")
        public int session_number;
        @JsonProperty("sets_count")
        public int sets_count;
        @JsonProperty("intensity_level")
        public int intensity_level;
        @JsonProperty("started_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Europe/Berlin")
        public LocalDateTime started_at;
        @JsonProperty("finished_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Europe/Berlin")
        public LocalDateTime finished_at;
    }
}
