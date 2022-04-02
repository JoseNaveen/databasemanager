package com.joey.databasemanager;

import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.joey.databasemanager.classicmodelsbeans.Order;
import com.joey.databasemanager.classicmodelsbeans.Orderdetail;
import com.joey.databasemanager.classicmodelsbeans.ProductLine;
import com.joey.databasemanager.dto.Info;
import com.joey.databasemanager.dto.User;
import com.joey.databasemanager.repository.nosql.UserNosqlRepository;
import com.joey.databasemanager.repository.sql.CustomerRepository;
import com.joey.databasemanager.repository.sql.OrderDetailRepository;
import com.joey.databasemanager.repository.sql.OrderRepository;
import com.joey.databasemanager.repository.sql.ProductLineRepository;
import com.joey.databasemanager.repository.sql.ProductRepository;

@SpringBootTest
public class DatabaseTests {
	
	
	@Autowired
	private ProductLineRepository productLineRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private UserNosqlRepository userNosqlRepo;
	
	@Test
	public void test1() throws Exception {
		
		Iterable<ProductLine> list = productLineRepo.findAll();
		
		Iterator<ProductLine> it = list.iterator();
		
		list.forEach(item-> {
			System.out.println(item);
		});
		
		ProductLine pline = productLineRepo.searchByplinename("Vintage Cars");
		System.out.println("=============================================================");
		/*pline.forEach(item-> {
			System.out.println(item);
		});*/
		System.out.println(pline);
		
		/**Iterable<Order> orderlist = orderRepository.findAll();
		
		orderlist.forEach(item-> {
			System.out.println(item);
		});**/
		
		short ans = productRepo.findMax(1);
		System.out.println("max: " + ans);
		
		
		var vals = customerRepo.getCustomersTotalNumOfPayments();
		vals.forEach(item-> {
			System.out.println(item.getcontact_first_name() + " " + item.getcontact_last_name() + " " + item.gettot_num_payments());
		});
		
		userNosqlRepo.findAll().forEach(item-> {
			System.out.println(item.getEmail());
		});
		
		User user = new User();
		Info info = new Info();
		user.setEmail("jalphons@microsoft.com");
		user.setGoogleId("adsadsadsa");
		user.setVerified(false);
		info.setBio("vetti");
		info.setFavoriteFood("pizza");
		info.setHairColor("black");
		user.setInfo(info);
		userNosqlRepo.insert(user);
	}
}
