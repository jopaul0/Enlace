package  br.edu.fatec.controllers;


import br.edu.fatec.dao.MotherDAO;
import br.edu.fatec.model.Mother;

import java.util.List;

public class  MotherController{
    // Setting the MotherDAO as attribute
    // Making it static and final , so I do not have to instance the class;
    private final static MotherDAO motherDAO = new MotherDAO();

    // Using empty constructor that can be used if no attributes is received like a static class
    public MotherController(){}

    // ------ Introducing the methods of this class ----- //
    public boolean addMother(Mother  mother) throws Exception {
        motherDAO.insert(mother);
        return true;
    }

    public boolean deleteMother(Mother  mother) throws Exception {
        Mother existingMother = motherDAO.findById(mother.getId().intValue());
        if ( existingMother == null ) {
            System.out.println("Mother not found");
            return false;
        }
        else{
            motherDAO.delete(existingMother);
            return true;
        }
    }

    public boolean updateMother(Mother  mother) throws Exception {
       Mother existingMother = motherDAO.findById(mother.getId().intValue());
       if ( existingMother == null ) {
           System.out.println("Mother not found");
           return false;
       }
       else{
           motherDAO.update(mother);
           return true;
       }


    }

    public List<Mother> getAll () throws  Exception{
        return motherDAO.findAll();

    }

    public List<Mother> getAllActives() throws  Exception{
        return motherDAO.findAllActives();
    }

    public List<Mother> findBirthdayMother () throws Exception{
        return  motherDAO.findBirthdayMother();
    }

    public boolean softDelete(Mother mother) throws Exception {
        Mother existingMother = motherDAO.findById(mother.getId().intValue());
        if ( existingMother == null ) {
            System.out.println("Mother not found");
            return false;
        }
        motherDAO.softDelete(mother);
        return true;
    }


}