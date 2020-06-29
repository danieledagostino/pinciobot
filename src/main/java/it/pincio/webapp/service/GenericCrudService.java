package it.pincio.webapp.service;

import java.util.List;

public interface GenericCrudService<T> {
	
	public void update(T object);
	
	public void insert(T object);
	
	public boolean delete(String id);
	
	public List<T> list();
	
	public T detail(String id);

}
