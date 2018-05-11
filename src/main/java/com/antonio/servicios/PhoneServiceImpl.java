package com.antonio.servicios;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antonio.dao.PhoneDao;
import com.antonio.modelo.Employee;
import com.antonio.modelo.Phone;

@Service("phoneService")
@Transactional
public class PhoneServiceImpl implements PhoneService {

	final static Logger logger = Logger.getLogger("PhoneServiceImpl");

	@Autowired
	private PhoneDao dao;
	
	@Autowired
	EmployeeService employeeService;
		
	@Override
	public Phone findById(Long id) {
		return dao.findById(id);
	}
	
	@Override
	public void savePhone (Phone phone){
		dao.savePhone(phone);
	}
	
	@Override 
	public Phone mergePhone (Phone phone, Long id){
		Phone currentPhone = findById(id);
		if (currentPhone == null) throw new RuntimeException("El telefono ya no existe");
		currentPhone.setContactnumber(phone.getContactnumber());
        currentPhone.setPhonetype(phone.getPhonetype());
        
        Phone phoneNew = dao.mergePhone(currentPhone); 
    //    employeeService.registrarMovimiento("MODIFICACION", currentPhone.getEmployee());
        return phoneNew;
	}
	
	@Override
	public boolean cratePhone (Phone phone, int EmployeeId){

        Employee e = employeeService.findById(EmployeeId);
		Phone currentPhone = dao.findPhoneByContactnumber(phone.getContactnumber());
		if (currentPhone ==null){
	        List<Employee> le = phone.getEmployees(); 
	        le.add(e);
            dao.savePhone(phone);
		} else {
			List<Employee> le = currentPhone.getEmployees();
			le.add(e);
            dao.mergePhone(currentPhone);
			//employeeService.addPhoneToEmployee(EmployeeId, currentPhone);
		}
        return true;               	  
	}
	
	@Override
	public void deletePhone (Phone phone, int employeeId){
        Phone currentPhone = findById(phone.getId());
        
        if (currentPhone==null) {
            logger.info ("deletePhone : phone not found");
            return;
        }		
        List<Employee> listaEmployee = currentPhone.getEmployees();
        
        Iterator<Employee> it = listaEmployee.iterator();
        Boolean found = false;
        while (it.hasNext() && !found) {
        	Employee  employeeItem = it.next();
            if (employeeItem.getId() == employeeId) {
                it.remove();
                found = true;
            }
        }
        if (found == true){
        	 dao.mergePhone(currentPhone);
        	 if (listaEmployee.isEmpty()) {
        		 dao.deletePhone(currentPhone.getId());
        	 }
        }

	}
}
