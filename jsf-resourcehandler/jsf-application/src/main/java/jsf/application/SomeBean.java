package jsf.application;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class SomeBean {
    public String getHello(){
        return "Hello ResourceHandler";
    }
}
