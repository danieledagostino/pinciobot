package it.pincio.webapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.dao.EventRepository;
import it.pincio.persistence.dao.FaqRepository;
import it.pincio.webapp.formbean.EventFormBean;
import it.pincio.webapp.formbean.EventFormBean;
import javassist.bytecode.stackmap.BasicBlock.Catch;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventWebappService implements GenericCrudService<EventFormBean>{
	
	@Autowired
	EventRepository eventRepository;

	@Override
	public void update(EventFormBean object) {
		Event faq = toEvent(object);
		
		eventRepository.save(faq);
	}

	@Override
	public void insert(EventFormBean object) {

		Event event = toEvent(object);
		
		eventRepository.save(event);
		
	}

	@Override
	public boolean delete(Integer id) {
		try {
			eventRepository.deleteById(id);
			return true;
		}catch(Exception e) {
			log.error("Error during deletion", e);
			return false;
		}
	}

	@Override
	public List<EventFormBean> list() {
		List<Event> list = eventRepository.findAll();
		
		return copyTo(list);
	}

	@Override
	public EventFormBean detail(Integer id) {
		try {
			Event event = eventRepository.getOne(id);
			return toFormBean(event);
		}catch(NumberFormatException e) {
			log.error("Error during convertion of a number froma string", e);
			return null;
		}
	}
	
	private Event toEvent(EventFormBean bean) {
		Event event = new Event();
		event.setId(Integer.valueOf(bean.getId()));
		event.setTitle(bean.getName());
		event.setDescription(bean.getDescription());
		event.setStartDate(bean.getStartTime());
		
		return event;
	}
	
	private EventFormBean toFormBean(Event event) {
		EventFormBean bean = new EventFormBean();
		bean.setId(String.valueOf(event.getId()));
		
		return bean;
	}
	
	private List<EventFormBean> copyTo(List<Event> list) {
		List<EventFormBean> result = new ArrayList<EventFormBean>();
		
		list.forEach(event -> result.add(toFormBean(event)));
		
		return result;
	}

}
