package br.edu.fatec.controllers;


import br.edu.fatec.dao.ServiceDAO;
import br.edu.fatec.model.Service;

import java.util.List;

public class ServiceController {


    private final static ServiceDAO serviceDAO = new ServiceDAO();

    public ServiceController(){}

    // ------- Introducing the methods for the service DAO ----- /
    public boolean addService(Service service) throws  Exception{
        serviceDAO.insert(service);
        return true;
    }


    public boolean deleteService(Service service) throws  Exception{
        Service existingService = serviceDAO.findById(service.getId().intValue());

        if ( existingService == null ){
            System.out.println("No service was found");
            return false;
        }
        serviceDAO.delete(service);
        return true;

    }

    public boolean updateService(Service service) throws Exception{
        Service existingService = serviceDAO.findById(service.getId().intValue());

        if ( existingService == null ){
            System.err.println("There was an issue while updating your service: " + service.getId());
            return false;
        }
        serviceDAO.update(service);
        return true;
    }

    public List<Service> findAllActives() throws Exception{
        return serviceDAO.findAllActives();
    }

    public List<Service> findAllServices() throws  Exception{
        return serviceDAO.findAll();
    }

    public boolean softDelete(Service service) throws Exception{
        Service existingService = serviceDAO.findById(service.getId().intValue());
        if(existingService == null){
            System.out.println("No service was found");
            return false;
        }
        serviceDAO.softDelete(service);
        return true;
    }


}