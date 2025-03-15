package com.codekata.oceanprobe.probenavigationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ObstacleEncounteredException extends RuntimeException {

    public ObstacleEncounteredException(String message) {
        super(message);
    }
}
