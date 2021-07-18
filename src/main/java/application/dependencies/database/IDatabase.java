package application.dependencies.database;

import domain.models.Message;
import domain.models.Task;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Manages interaction with a dbms. Can be used
 * to store or read data.
 */
public interface IDatabase {

    <T> List<T> getAll(Class<T> clazz) throws DatabaseConnectionException;

    <T> List<T> saveAll(List<T> objects, Class<T> clazz) throws DatabaseConnectionException;

    <T> T save(T object, Class<T> clazz) throws DatabaseConnectionException;

    <T> void delete(T object, Class<T> clazz) throws DatabaseConnectionException;

    <T> void deleteAll(List<T> objects, Class<T> clazz) throws DatabaseConnectionException;

}
