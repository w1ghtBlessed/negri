package run.lovehulo.negri.table;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import org.primefaces.PrimeFaces;
import run.lovehulo.negri.utils.ConnectionToDB1;

@Named("tableWorker")
@ViewScoped
public class TableWorker implements Serializable {

    @Inject
    private ConnectionToDB1 dbWorker;
    private List<Person> peoples;
    private Person selectedPeople;

    public void openNew() {
        this.selectedPeople = new Person();
    }

    public Person getSelectedPeople() {
        return selectedPeople;
    }

    public void setSelectedPeople(Person selectedPeople) {
        this.selectedPeople = selectedPeople;
    }

    public List<Person> getPeoples() {
        return peoples;
    }

    public void setPeoples(List<Person> peoples) {
        this.peoples = peoples;
    }

    @PostConstruct
    public void init() {
        java.util.Date date = new java.util.Date();
        System.out.println(date);
        try (Connection con = dbWorker.getConnection()) {
            String selectQuery = "Select * from public.people where p_delete_date isnull";
            PreparedStatement ps = con.prepareStatement(selectQuery);
            ResultSet rs = ps.executeQuery();
            peoples = new ArrayList<>();

            while (rs.next()) {
                Person peop = new Person();
                peop.setId(rs.getString("p_id"));
                peop.setName(rs.getString("p_name"));
                peop.setSurname(rs.getString("p_surname"));
                peop.setEyeColor(rs.getString("p_eyecolor"));
                peoples.add(peop);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        peoples.add(new Person("1", "Oleg", "Senya", "Green"));
//        peoples.add(new Person("2", "Nikita", "Sasha", "Black"));
//        peoples.add(new Person("3", "Jeanne", "Balakshina", "Green"));
//        peoples.add(new Person("4", "Arina", "Builova", "Blue-Grey"));
//        peoples.add(new Person("5", "Nikita", "Hill", "Brown"));
    }

    public void deletePeople() {
        java.util.Date utilDate = new java.util.Date();
        Date sqlDate = new Date(utilDate.getTime());
        try (Connection con = dbWorker.getConnection()) {
            String deleteQuery = "Update public.people set p_delete_date=? where p_id=?";
            PreparedStatement ps = con.prepareStatement(deleteQuery);
            System.out.println(sqlDate);
            ps.setDate(1, sqlDate);
            ps.setString(2, this.selectedPeople.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        peoples.remove(selectedPeople);
        selectedPeople = null;
        PrimeFaces.current().ajax().update("tableInf:peoples");
    }

    public void viewPeople() {
        // no-op
    }

    //replaceAll("-", "").substring(0, 9)
    public void savePeople() {

        if (this.selectedPeople.getId() == null) {
            this.selectedPeople.setId(UUID.randomUUID().toString());
            peoples.add(new Person(this.selectedPeople.getId(), this.selectedPeople.getName(), this.selectedPeople.getSurname(), this.selectedPeople.getEyeColor()));
            try (Connection con = dbWorker.getConnection()) {
                String insertQuery = "Insert into public.people(p_id,p_name, p_surname, p_eyecolor) values(?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(insertQuery);
                ps.setString(1, this.selectedPeople.getId());
                ps.setString(2, this.selectedPeople.getName());
                ps.setString(3, this.selectedPeople.getSurname());
                ps.setString(4, this.selectedPeople.getEyeColor());
                ps.execute();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(this.selectedPeople.getName() + " Added"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try (Connection con = dbWorker.getConnection()) {
                String updateQuery = "Update public.people set p_name=?,p_surname=?,p_eyecolor=? where p_id=?";
                PreparedStatement ps = con.prepareStatement(updateQuery);
                ps.setString(1, this.selectedPeople.getName());
                ps.setString(2, this.selectedPeople.getSurname());
                ps.setString(3, this.selectedPeople.getEyeColor());
                ps.setString(4, this.selectedPeople.getId());
                ps.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(this.selectedPeople.getName() + " Updated"));
        }
        PrimeFaces.current().ajax().update("tableInf:growl", "tableInf:peoples");
    }

//     public void updatePeople() {
//        PrimeFaces.current().ajax().update("tableInf:growl", "tableInf:peoples");
//    }
}
