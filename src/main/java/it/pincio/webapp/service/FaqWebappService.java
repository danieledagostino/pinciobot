package it.pincio.webapp.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.pincio.persistence.bean.Faq;
import it.pincio.persistence.dao.FaqRepository;
import it.pincio.webapp.formbean.FaqFormBean;

@Service
public class FaqWebappService implements GenericCrudService<FaqFormBean>{
	
	@Autowired
	FaqRepository faqRepository;

	@Override
	public void update(FaqFormBean object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insert(FaqFormBean object) {

		Faq faq = new Faq();
		
		copyTo(faq, object);
		
		faqRepository.save(faq);
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<FaqFormBean> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void detail(String id) {
		// TODO Auto-generated method stub
		
	}
	
	private void copyTo(Faq faq, FaqFormBean bean) {
		faq.setKeywords(bean.getParole());
		faq.setHint(bean.getDomanda());
		faq.setAnswer(bean.getRisposta());
		faq.setActive(bean.getAttivo());
	}

}
