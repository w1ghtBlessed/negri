package run.lovehulo.negri.input;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.PrimeFaces;
import static org.primefaces.component.api.UIChart.PropertyKeys.model;
import org.primefaces.event.SelectEvent;

@Named("worker")
@ViewScoped
public class Imputworker implements Serializable {

    private String name;
    private String surname;
    private String number;
    private boolean isRendered = false;
    private String country;
    private List<String> countryList;

    public List<String> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<String> countryList) {
        this.countryList = countryList;
    }
    
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void renderInf() {
        isRendered = true;
//        PrimeFaces.current().ajax().update("fgdfg:content");
        System.out.println(name);
        System.out.println(surname);
        System.out.println(number);
    }

    public boolean isIsRendered() {
        return isRendered;
        
    }

    public void setIsRendered(boolean isRendered) {
        this.isRendered = isRendered;
    }
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
    public void showInfo() {
        addMessage(FacesMessage.SEVERITY_INFO, "Current", country);
    }
   public void selectCountry(ValueChangeEvent event){
       addMessage(FacesMessage.SEVERITY_INFO, "Выбрана страна "+ event.getNewValue().toString(), null);
   }
   
   @PostConstruct
   public void init(){
       countryList = new ArrayList<>();
       for (int i = 0; i < 6; i++) {
            countryList.add("Страна " + i);
        }
   }
}   