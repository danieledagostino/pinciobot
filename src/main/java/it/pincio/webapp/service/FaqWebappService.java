package it.pincio.webapp.service;

import java.util.ArrayList;
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

		Faq faq = toFaq(object);
		
		faqRepository.save(faq);
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<FaqFormBean> list() {
		List<Faq> list = faqRepository.findAll();
		
		return copyTo(list);
	}

	@Override
	public void detail(String id) {
		// TODO Auto-generated method stub
		
	}
	
	private Faq toFaq(FaqFormBean bean) {
		Faq faq = new Faq();
		faq.setKeywords(bean.getParole());
		faq.setHint(bean.getDomanda());
		faq.setAnswer(bean.getRisposta());
		faq.setActive(bean.getAttivo());
		
		return faq;
	}
	
	private FaqFormBean toFormBean(Faq faq) {
		FaqFormBean bean = new FaqFormBean();
		bean.setParole(faq.getKeywords());
		bean.setDomanda(faq.getHint());
		bean.setRisposta(faq.getAnswer());
		bean.setAttivo(faq.getActive());
		
		return bean;
	}
	
	private List<FaqFormBean> copyTo(List<Faq> list) {
		List<FaqFormBean> result = new ArrayList<FaqFormBean>();
		
		list.forEach(faq -> result.add(toFormBean(faq)));
		
		return result;
	}

}
