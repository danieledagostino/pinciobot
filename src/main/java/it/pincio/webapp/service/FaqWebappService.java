package it.pincio.webapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.pincio.persistence.bean.Faq;
import it.pincio.persistence.dao.FaqRepository;
import it.pincio.webapp.formbean.FaqFormBean;
import javassist.bytecode.stackmap.BasicBlock.Catch;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FaqWebappService implements GenericCrudService<FaqFormBean>{
	
	@Autowired
	FaqRepository faqRepository;

	@Override
	public void update(FaqFormBean object) {
		Faq faq = toFaq(object);
		
		faqRepository.save(faq);
	}

	@Override
	public void insert(FaqFormBean object) {

		Faq faq = toFaq(object);
		faq.setActive("Y");
		
		faqRepository.save(faq);
		
	}

	@Override
	public boolean delete(Integer id) {
		try {
			faqRepository.deleteById(id);
			return true;
		}catch(Exception e) {
			log.error("Error during deletion", e);
			return false;
		}
	}

	@Override
	public List<FaqFormBean> list() {
		List<Faq> list = faqRepository.findAll();
		
		return copyTo(list);
	}

	@Override
	public FaqFormBean detail(Integer id) {
		try {
			Faq faq = faqRepository.getOne(id);
			return toFormBean(faq);
		}catch(NumberFormatException e) {
			log.error("Error during convertion of a number froma string", e);
			return null;
		}
	}
	
	private Faq toFaq(FaqFormBean bean) {
		Faq faq = new Faq();
		faq.setId(Integer.valueOf(bean.getId()));
		faq.setKeywords(bean.getParole());
		faq.setHint(bean.getDomanda());
		faq.setAnswer(bean.getRisposta());
		faq.setActive(bean.getAttivo());
		
		return faq;
	}
	
	private FaqFormBean toFormBean(Faq faq) {
		FaqFormBean bean = new FaqFormBean();
		bean.setId(String.valueOf(faq.getId()));
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
