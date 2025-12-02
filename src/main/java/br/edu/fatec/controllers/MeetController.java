package br.edu.fatec.controllers;

import java.util.List;

import br.edu.fatec.model.Meet;
import br.edu.fatec.dao.MeetDAO;
import br.edu.fatec.model.Service;
import br.edu.fatec.model.Mother;


public class MeetController {

    private final static MeetDAO meetDAO = new MeetDAO();


    // ---- Empty constructor as usual ---- //
    public MeetController() {
    }


    // -------- Introducing the MeetController methods ------- //

    public boolean addMeet(Meet meet) throws Exception {
        meetDAO.insert(meet);
        return true;
    }

    public void updateMeet(Meet meet) throws Exception {
        Meet existingMeet = findById(meet.getId());
        if (existingMeet == null) {
            System.out.println("Meet not found");
        } else {
            meetDAO.update(meet);
            meetDAO.deleteEnlacesByMeetId(meet.getId());

            if (meet.getEnlaces() != null && !meet.getEnlaces().isEmpty()) {
                meetDAO.reinsertEnlaces(meet.getId(), meet.getEnlaces());
            }

        }
    }

    public boolean softDelete(Meet meet) throws Exception {
        Meet existingMeet = findById(meet.getId());
        if (existingMeet == null) {
            System.out.println("Meet not found");
            return false;
        } else {
            meetDAO.softDelete(meet);
            return true;
        }
    }

    public boolean setCompleted(Meet meet) throws Exception {
        Meet existingMeet = findById(meet.getId());
        if (existingMeet == null) {
            System.out.println("Meet not found");
            return false;

        } else {
            meetDAO.setCompleted(meet);
            return true;
        }
    }

    public List<Meet> getAllMeets() throws Exception {
        return meetDAO.findAll();
    }

    public Meet findById(Long id) throws Exception {
        if (id == null) return null;
        return meetDAO.findById(id.intValue());
    }

}