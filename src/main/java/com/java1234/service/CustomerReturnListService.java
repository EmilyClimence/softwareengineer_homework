package com.java1234.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.java1234.entity.CustomerReturnList;
import com.java1234.entity.CustomerReturnListDrugs;

/*
 * 客户退货单Service接口
 * @author java1234_AT
 *
 */
public interface CustomerReturnListService {

	/*
	 * 根据id查询实体
	 */
    CustomerReturnList findById(Integer id);
	
	/*
	 * 获取当天最大客户退货单号
	 */
    String getTodayMaxCustomerReturnNumber();
	
	/*
	 * 添加客户退货单 以及所有客户退货单药品
	 */
    void save(CustomerReturnList customerReturnList, List<CustomerReturnListDrugs> customerReturnListDrugsList);
	
	/*
	 * 根据条件查询客户退货单信息
	 */
    List<CustomerReturnList> list(CustomerReturnList customerReturnList, Direction direction, String... properties);
	
	/*
	 * 根据id删除客户退货单信息 包括客户退货单里的药品
	 */
    void delete(Integer id);
	
	/*
	 * 更新退货单
	 */
    void update(CustomerReturnList customerReturnList);

}
