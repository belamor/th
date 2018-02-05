package es.nitaur.exception;

import javax.persistence.PersistenceException;

public class EntityNotValidException extends PersistenceException {
    public EntityNotValidException(String message) {
        super(message);
    }
}
