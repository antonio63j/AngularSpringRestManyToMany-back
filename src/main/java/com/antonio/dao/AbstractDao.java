package com.antonio.dao;

import java.io.Serializable;

import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.log4j.Logger;
import com.antonio.controladores.PhoneController;

public abstract class AbstractDao<PK extends Serializable, T> {
	
	final static Logger logger = Logger.getLogger(AbstractDao.class);
	
	private final Class<T> persistentClass;
	
	@SuppressWarnings("unchecked")
	public AbstractDao(){
		this.persistentClass =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession(){
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public T getByKey(PK key) {
		return (T) getSession().get(persistentClass, key);
	}

	public void update (T entity){
		System.out.println("update :" + entity.toString());
		getSession().update(entity);
	}
	
	public T merge (T entity){
		logger.debug("merge:"  + entity.toString());
		T e = (T) getSession().merge(entity);
		getSession().flush();
		return e;
	}
	
	public void persist(T entity) {
	    logger.debug("persist:"  + entity.toString());
		getSession().persist(entity);
		getSession().flush();
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}
	
	protected Criteria createEntityCriteria(){
		return getSession().createCriteria(persistentClass);
	}

}
