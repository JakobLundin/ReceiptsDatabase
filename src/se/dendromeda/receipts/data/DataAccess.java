package se.dendromeda.receipts.data;

import java.util.List;

public interface DataAccess <T> {
	
	List<T> getAll();
	
	T getById(int id);
	
	T getByName(String name);
	
	void update(T obj);
	
	void create(T obj);
	
	void delete(T obj);
	
	void deleteById(int id);
}
