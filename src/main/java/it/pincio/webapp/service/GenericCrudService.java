package it.pincio.webapp.service;

import java.util.List;

public interface GenericCrudService<T> {
	
	public void update(T object);
	
	public void insert(T object);
	
	public void delete(String id);
	
	public List<T> list();
	
	public void detail(String id);

}
