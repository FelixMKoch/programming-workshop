package persistence.database;

import application.dependencies.database.DatabaseConnectionException;
import application.dependencies.database.IDatabase;
import domain.models.Message;
import domain.models.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;
import org.postgresql.util.PSQLException;
import persistence.modelmapper.IModelMapper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PsqlDatabaseImpl implements IDatabase {

    private final IModelMapper modelMapper;
    /**
     * This map contains mapping for domain types to database types.
     * Since we need to work with the db objects internally, we cannot publish
     * them to the outside. However, the caller only provides domain objects.
     * Therefore we require a dynamic mapping.
     */
    private final Map<Class<?>, Class<?>> dbMappingTypes = new HashMap<>();

    public PsqlDatabaseImpl(IModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.dbMappingTypes.put(Message.class, DbMessage.class);
        this.dbMappingTypes.put(Task.class, DbTask.class);
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz) throws DatabaseConnectionException {
        Class<?> dbType = lookUpDbType(clazz);
        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            sessionFactory.openSession();
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<?> criteriaQuery = criteriaBuilder.createQuery(dbType);
            criteriaQuery.from(dbType);
            List<T> objects = session
                    .createQuery(criteriaQuery)
                    .getResultList()
                    .stream()
                    .map(dbMessage -> this.modelMapper.getMapper().map(dbMessage, clazz))
                    .collect(Collectors.toList());
            session.getTransaction().commit();
            return objects;
        } catch (ServiceException e) {
            throw new DatabaseConnectionException(e);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }

    @Override
    public <T> List<T> saveAll(List<T> objects, Class<T> clazz) throws DatabaseConnectionException {
        List<T> dbObjects = new ArrayList<>();
        for (T o : objects) {
            dbObjects.add(save(o, clazz));
        }
        return dbObjects;
    }

    @Override
    public <T> T save(T object, Class<T> clazz) throws DatabaseConnectionException {
        Class<?> dbType = lookUpDbType(clazz);
        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            sessionFactory.openSession();
            Session session = sessionFactory.getCurrentSession();
            Object dbObject = this.modelMapper.getMapper().map(object, dbType);
            session.beginTransaction();
            session.save(dbObject);
            session.getTransaction().commit();
            return this.modelMapper.getMapper().map(dbObject, clazz);
        } catch (ServiceException e) {
            throw new DatabaseConnectionException(e);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }

    }

    @Override
    public <T> void delete(T object, Class<T> clazz) throws DatabaseConnectionException {
        Class<?> dbType = lookUpDbType(clazz);
        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            sessionFactory.openSession();
            Session session = sessionFactory.getCurrentSession();
            Object dbObject = this.modelMapper.getMapper().map(object, dbType);
            session.beginTransaction();
            sessionFactory.getCurrentSession().delete(dbObject);
            session.getTransaction().commit();
        } catch (ServiceException e) {
            throw new DatabaseConnectionException(e);
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }

    @Override
    public <T> void deleteAll(List<T> objects, Class<T> clazz) throws DatabaseConnectionException {
        for (T object : objects) {
            delete(object, clazz);
        }
    }

    private Class<?> lookUpDbType(Class<?> domainType) {
        if (this.dbMappingTypes.containsKey(domainType)) {
            return this.dbMappingTypes.get(domainType);
        }
        throw new IllegalArgumentException("Provided domain type has not been mapped to a database type");
    }

}
