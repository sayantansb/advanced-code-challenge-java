package com.statista.code.challenge.domain;

import org.springframework.stereotype.Component;

@Component
public class DepartmentFactory implements AbstractFactory<Department>{
    @Override
    public Department create(String name) {
        Department department = null;
        if(name.hashCode()%2==0){
            department = new AutomatedSalesDepartment();
        }else{
            department = new ManualSalesDepartment();
        }
        department.setName(name);
        return department;
    }
}
