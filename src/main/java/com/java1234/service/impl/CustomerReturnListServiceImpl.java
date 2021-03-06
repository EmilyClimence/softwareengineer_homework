package com.java1234.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java1234.entity.Drugs;
import com.java1234.entity.CustomerReturnList;
import com.java1234.entity.CustomerReturnListDrugs;
import com.java1234.repository.DrugsRepository;
import com.java1234.repository.DrugsTypeRepository;
import com.java1234.repository.CustomerReturnListDrugsRepository;
import com.java1234.repository.CustomerReturnListRepository;
import com.java1234.service.CustomerReturnListService;
import com.java1234.util.StringUtil;

/*
 * 销售单Service实现类
 * @author java1234_AT
 *
 */
@Service("customerReturnListService")
@Transactional
public class CustomerReturnListServiceImpl implements CustomerReturnListService{

	@Resource
	private CustomerReturnListRepository customerReturnListRepository;
	
	@Resource
	private CustomerReturnListDrugsRepository customerReturnListDrugsRepository;
	
	@Resource
	private DrugsRepository drugsRepository;
	
	@Resource
	private DrugsTypeRepository drugsTypeRepository;
	
	@Override
	public String getTodayMaxCustomerReturnNumber() {
		return customerReturnListRepository.getTodayMaxCustomerReturnNumber();
	}

	@Transactional
	public void save(CustomerReturnList customerReturnList, List<CustomerReturnListDrugs> customerReturnListDrugsList) {
		// 保存每个销售单药品
		for(CustomerReturnListDrugs customerReturnListDrugs:customerReturnListDrugsList){
			customerReturnListDrugs.setType(drugsTypeRepository.findOne(customerReturnListDrugs.getTypeId())); // 设置类别
			customerReturnListDrugs.setCustomerReturnList(customerReturnList); // 设置采购单
			customerReturnListDrugsRepository.save(customerReturnListDrugs);
			// 修改药品库存
			Drugs drugs=drugsRepository.findOne(customerReturnListDrugs.getDrugsId());
			drugs.setInventoryQuantity(drugs.getInventoryQuantity()+customerReturnListDrugs.getNum());
			drugs.setState(2);
			drugsRepository.save(drugs);
		}
		customerReturnListRepository.save(customerReturnList); // 保存销售单
	}

	@Override
	public List<CustomerReturnList> list(CustomerReturnList customerReturnList, Direction direction,
			String... properties) {
		return customerReturnListRepository.findAll(new Specification<CustomerReturnList>(){

			@Override
			public Predicate toPredicate(Root<CustomerReturnList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.conjunction();
				if(customerReturnList!=null){
					if(customerReturnList.getCustomer()!=null && customerReturnList.getCustomer().getId()!=null){
						predicate.getExpressions().add(cb.equal(root.get("customer").get("id"), customerReturnList.getCustomer().getId()));
					}
					if(StringUtil.isNotEmpty(customerReturnList.getCustomerReturnNumber())){
						predicate.getExpressions().add(cb.like(root.get("customerReturnNumber"), "%"+customerReturnList.getCustomerReturnNumber().trim()+"%"));
					}
					if(customerReturnList.getState()!=null){
						predicate.getExpressions().add(cb.equal(root.get("state"), customerReturnList.getState()));
					}
					if(customerReturnList.getbCustomerReturnDate()!=null){
						predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("customerReturnDate"), customerReturnList.getbCustomerReturnDate()));
					}
					if(customerReturnList.geteCustomerReturnDate()!=null){
						predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("customerReturnDate"), customerReturnList.geteCustomerReturnDate()));
					}
				}
				return predicate;
			}
		  },new Sort(direction, properties));
	}

	@Override
	public void delete(Integer id) {
		customerReturnListDrugsRepository.deleteByCustomerReturnListId(id);
		customerReturnListRepository.delete(id);
	}

	@Override
	public CustomerReturnList findById(Integer id) {
		return customerReturnListRepository.findOne(id);
	}

	@Override
	public void update(CustomerReturnList customerReturnList) {
		customerReturnListRepository.save(customerReturnList);
	}



}
